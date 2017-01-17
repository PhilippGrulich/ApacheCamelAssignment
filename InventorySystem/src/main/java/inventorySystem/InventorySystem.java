package inventorySystem;

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
public class InventorySystem {

    public static void main(String[] args) throws Exception {
        CamelContext cc = new DefaultCamelContext();
        ActiveMQComponent activeMQComponent = ActiveMQComponent.activeMQComponent();
        activeMQComponent.setTrustAllPackages(true);
        cc.addComponent("activemq", activeMQComponent);


        cc.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("activemq:queue:InventorySystem")
                        .process(new Processor() {
                            public void process(Exchange exchange) throws Exception {
                                Object in = exchange.getIn().getBody();
                                System.out.println("Inventory"+in);
                                exchange.getOut().setBody(in);

                            }
                        });

            }
        });
        cc.start();

        Thread.sleep(1000000);

    }
}
