package de.tub.da.callcenterChannelAdapter;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import java.util.HashMap;

/**
 * This is the callcenter channel adapter. It observes the callcenter file, transforms the orders and pushes the files to the queue.
 */
public class CallcenterChannelAdapter {


    CallcenterChannelAdapter() throws Exception {
        String tempdir = System.getProperty("user.home");
        final String fileDir = tempdir + "/callcenter/";

        CamelContext cc = new DefaultCamelContext();
        ActiveMQComponent activeMQComponent = ActiveMQComponent.activeMQComponent();
        activeMQComponent.setTrustAllPackages(true);
        cc.addComponent("activemq", activeMQComponent);
        cc.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("file://" + fileDir + "?move=.done")
                        .process(new CallcenterMessageTranslator())  // Transformes the order string to a hashmap
                        .multicast()
                        .to("activemq:queue:NEW_ORDER")
                        .to("stream:out");
            }
        });

        System.out.println("Observe FileDir: " + fileDir);
        cc.start();
        System.out.println("------The Callcenter Adapter is running-------");
        Thread.sleep(10000000);
    }


    /**
     * Intern message translator.
     */
    class CallcenterMessageTranslator implements Processor {
        public void process(Exchange exchange) throws Exception {
            String callcenterMessage = exchange.getIn().getBody(String.class);
            String[] items = callcenterMessage.trim()
                    .replace(">", "")
                    .replace("<", "")
                    .split(",");

            // set the output to the file
            HashMap<String, String> map = new HashMap<>();
            map.put("CustomerID", items[0]);
            map.put("FirstName", items[1].split(" ")[0]);
            map.put("LastName", items[1].split(" ")[1]);
            map.put("OverallITems", null);
            map.put("NumberOfDrivingSuits", items[2]);
            map.put("NumberOfSurfboards", items[3]);
            map.put("OrderID", null);
            map.put("Valid", null);
            map.put("validationResult", null);
            exchange.getOut().setBody(map);
        }
    }

    public static void main(String[] args) throws Exception {
        new CallcenterChannelAdapter();
    }
}