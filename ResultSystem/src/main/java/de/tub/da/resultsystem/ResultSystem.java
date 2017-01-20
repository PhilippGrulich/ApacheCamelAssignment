package de.tub.da.resultsystem;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import javax.xml.transform.Result;
import java.util.HashMap;

/**
 * The ResultSystem has tow purposes on the one hand it displays the result data.
 * On the other hand it also integrates the NEW_ORDER queue with the Inventory and BillingSystem.
 */
public class ResultSystem {


    ResultSystem() throws Exception {
        CamelContext cc = new DefaultCamelContext();
        ActiveMQComponent activeMQComponent = ActiveMQComponent.activeMQComponent();
        activeMQComponent.setTrustAllPackages(true);
        cc.addComponent("activemq", activeMQComponent);
        cc.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {

                from("activemq:queue:NEW_ORDER")
                        .process(new OrderIDEnritcher())   // Sets a valid and unique orderID
                        .process(new ValidPreProcessing())  // Set the valid filed to true, because the inventory and Billing system will set it to false
                        .multicast(new ResponseAggregationStrategy()) // start multicast
                        .to(ExchangePattern.InOut, "activemq:queue:InventorySystem")
                        .to(ExchangePattern.InOut, "activemq:queue:BillingSystem")
                        .end()  // End multicast
                        .to("direct:outputPipeline");

                from("direct:outputPipeline")
                        .choice() // Content based router, so we process valid and invalid orders in a different way.
                        .when(header("valid").isEqualTo(false)).inOut("direct:displayInvalid")
                        .otherwise().inOut("direct:displayValid")
                        .end()
                        .to("stream:out");

                from("direct:displayInvalid").process(new InvalidProcessor());
                from("direct:displayValid").process(new ValidProcessor());
            }
        });
        cc.start();

        Thread.sleep(1000000);
    }

    class OrderIDEnritcher implements Processor {
        int currentOrderID = 1;

        @Override
        public void process(Exchange exchange) throws Exception {
            HashMap<String, String> order = exchange.getIn().getBody(HashMap.class);
            order.put("OrderID", String.valueOf(currentOrderID++));
            exchange.getOut().setBody(order);
        }
    }

    class ValidPreProcessing implements Processor {
        @Override
        public void process(Exchange exchange) throws Exception {
            HashMap<String, String> order = exchange.getIn().getBody(HashMap.class);
            order.put("Valid", "true");
            order.put("validationResult", "true");
            exchange.getOut().setBody(order);
        }
    }

    class ResponseAggregationStrategy implements AggregationStrategy {
        public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
            HashMap oldMessage = oldExchange == null ? null : oldExchange.getIn().getBody(HashMap.class);
            HashMap newMessage = newExchange.getIn().getBody(HashMap.class);
            if (oldMessage == null)
                return newExchange;

            if (newMessage.get("Valid").equals("false")) {
                return newExchange;
            }
            return oldExchange;
        }
    }

    class OutputPreProcessor implements Processor {
        public void process(Exchange exchange) throws Exception {
            HashMap<String, String> order = exchange.getIn().getBody(HashMap.class);
            exchange.getOut().setHeader("valid", order.get("valid"));
            exchange.getOut().setBody(exchange.getIn().getBody());
        }
    }

    public static void main(String[] args) throws Exception {
        new ResultSystem();
    }

    private class InvalidProcessor implements Processor {
        @Override
        public void process(Exchange exchange) throws Exception {
            String message = exchange.getIn().getBody(String.class);
            exchange.getOut().setBody("INVALID: " + message);
        }
    }

    private class ValidProcessor implements Processor {
        @Override
        public void process(Exchange exchange) throws Exception {
            String message = exchange.getIn().getBody(String.class);
            exchange.getOut().setBody("VALID: " + message);
        }
    }
}
