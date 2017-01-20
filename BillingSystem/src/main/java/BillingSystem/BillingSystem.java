package BillingSystem;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import java.util.HashMap;

/**
 * This is the implementation of the BillingSystem, whereby the main handles the camel integration and the
 * Billing validation itself is handed int the Repository.
 */
public class BillingSystem {


    public static void main(String[] args) throws Exception {
        CamelContext cc = new DefaultCamelContext();
        ActiveMQComponent activeMQComponent = ActiveMQComponent.activeMQComponent();
        activeMQComponent.setTrustAllPackages(true);
        cc.addComponent("activemq", activeMQComponent);
        cc.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("activemq:queue:BillingSystem")
                        .process(new BillingRepository());
            }
        });
        cc.start();
        Thread.sleep(1000000);
    }

    static class BillingRepository implements Processor {
        // Local State for the Billing validation.
        // In a real use-case there would be some kind of database access.
        private final boolean[] userIDValidataion = {true,false,true,true};

        public void process(Exchange exchange) throws Exception {
            HashMap<String, String> in = exchange.getIn().getBody(HashMap.class);
            Integer userID = Integer.valueOf(in.get("CustomerID"));
            if (!userIDValidataion[userID]){
                in.put("Valid","false");
                in.put("validationResult","false");
                exchange.getOut().setHeader("valid",false);
            }

            System.out.println("Current User:" + userID + " is valid:" + userIDValidataion[userID]);

            exchange.getOut().setBody(in);
        }
    }
}
