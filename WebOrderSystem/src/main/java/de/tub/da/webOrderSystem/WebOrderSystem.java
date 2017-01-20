package de.tub.da.webOrderSystem;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import java.util.HashMap;

/**
 * The simple WebOrderSystem.
 * It just creates a rest interface on  http://localhost:4567/order.
 * Witch can be queried with the following call:
 * curl -i -X POST -H "Content-Type:text/plain" -d 'Alice,Test,2,0,0' 'http://localhost:4567/order'
 */
public class WebOrderSystem {

    WebOrderSystem() throws Exception {
        CamelContext cc = new DefaultCamelContext();
        ActiveMQComponent activeMQComponent = ActiveMQComponent.activeMQComponent();
        activeMQComponent.setTrustAllPackages(true);
        cc.addComponent("activemq", activeMQComponent);
        cc.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {

                restConfiguration().component("spark-rest");

                from("spark-rest:post:order") // Create rest interface
                        .process(new WebOrderMessageTranslate()) // process request data
                        .to("activemq:queue:NEW_ORDER") // send to new order queue
                        .to("stream:out");

            }
        });
        cc.start();
        System.out.println("------The WebOrder System is running-------");
        Thread.sleep(1000000);
    }

    class WebOrderMessageTranslate implements org.apache.camel.Processor {

        public void process(Exchange exchange) throws Exception {
            String webMessage = exchange.getIn().getBody(String.class);
            String[] items = webMessage.trim()
                    .split(",");

            //  <First Name, Last Name, Number of orderedsurfboards, Number of ordered diving suits, Customer­ID>
            HashMap<String,String> map = new HashMap<>();
            map.put("CustomerID", items[4].trim());
            map.put("FirstName", items[0].trim());
            map.put("LastName", items[1].trim());
            map.put("OverallITems", null);
            map.put("NumberOfDrivingSuits", items[3].trim());
            map.put("NumberOfSurfboards", items[2].trim());
            map.put("OrderID", null);
            map.put("Valid", null);
            map.put("validationResult", null);

            exchange.getOut().setBody(map);
        }
    }
    public static void main(String[] args) throws Exception {
        new WebOrderSystem();
    }
}