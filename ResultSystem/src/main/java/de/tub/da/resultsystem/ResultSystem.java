package de.tub.da.resultsystem;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.processor.aggregate.AggregationStrategy;

/**
 * Created by philipp on 17.01.17.
 */
public class ResultSystem {

    public static void main(String[] args) throws Exception {
        CamelContext cc = new DefaultCamelContext();
        ActiveMQComponent activeMQComponent = ActiveMQComponent.activeMQComponent();
        activeMQComponent.setTrustAllPackages(true);
        cc.addComponent("activemq", activeMQComponent);
        cc.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("activemq:queue:NEW_ORDER")
                        .multicast(new AggregationStrategy() {
                            public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
                                Order oldMessage = oldExchange ==null ? null : oldExchange.getIn().getBody(Order.class);
                                Order newMessage = newExchange.getIn().getBody(Order.class);
                                if (oldMessage==null)
                                    return newExchange;
                                return null;
                            }
                        })
                        .to(ExchangePattern.InOut,"activemq:queue:InventorySystem")
                        .end()
                        .to("direct:output");

                from("direct:output").process(new Processor() {
                    public void process(Exchange exchange) throws Exception {
                        exchange.getIn().setBody("resultSystem" + exchange.getIn().getBody());
                    }
                }).to("stream:out");

            }
        });


        cc.start();

        Thread.sleep(1000000);

    }
}