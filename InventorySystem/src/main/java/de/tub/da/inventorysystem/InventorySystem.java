package de.tub.da.inventorysystem;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import java.util.HashMap;

/**
 * This is the implementation of the InventorySystem, whereby the main handles the camel integration and the
 * Inventory itself is handed int the Repository.
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
                        .process(new InventoryRepository());
            }
        });
        cc.start();
        System.out.println("------The Inventory System is running-------");
        Thread.sleep(1000000);
    }

    static class InventoryRepository implements Processor {
        // Local State for the Inventory.
        // In a real use-case there would be some kind of database access.
        int surfCount = 100;
        int diveCount = 10;

        public void process(Exchange exchange) throws Exception {
            HashMap<String, String> in = exchange.getIn().getBody(HashMap.class);
            Integer numberOfDrivingSuits = Integer.valueOf(in.get("NumberOfDrivingSuits"));
            Integer numberOfSurfboards = Integer.valueOf(in.get("NumberOfSurfboards"));
            if (numberOfDrivingSuits > diveCount || numberOfSurfboards > surfCount){
                in.put("Valid","false");
                in.put("validationResult","false");
                exchange.getOut().setHeader("valid",false);
            }else{
                surfCount= surfCount - numberOfSurfboards;
                diveCount= diveCount - numberOfDrivingSuits;
            }

            System.out.println("Current Inventory:" + surfCount + ", "+ diveCount);
            exchange.getOut().setBody(in);
        }
    }
}
