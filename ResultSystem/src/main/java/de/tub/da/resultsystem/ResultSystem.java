package de.tub.da.resultsystem;

import de.tu.comon.Order;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import java.util.HashMap;

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
                errorHandler(loggingErrorHandler("mylogger.name").level(LoggingLevel.INFO));
                from("activemq:queue:NEW_ORDER").errorHandler(loggingErrorHandler("mylogger.name").level(LoggingLevel.INFO))
                        .multicast(new AggregationStrategy() {
                            public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
                                System.out.println("Message agg");
                                HashMap oldMessage = oldExchange ==null ? null : oldExchange.getIn().getBody(  HashMap.class);
                                HashMap newMessage = newExchange.getIn().getBody(HashMap.class);
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
