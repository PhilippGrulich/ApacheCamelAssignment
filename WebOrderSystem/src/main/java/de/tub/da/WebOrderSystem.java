package de.tub.da;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.sparkrest.SparkConfiguration;
import org.apache.camel.component.sparkrest.SparkMessage;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.ProcessorEndpoint;
import org.apache.camel.model.rest.RestBindingMode;

import java.util.HashMap;

import static org.apache.activemq.camel.component.ActiveMQComponent.activeMQComponent;

/**
 * Created by philipp on 10.01.17.
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

                from("spark-rest:post:order")
                        .process(new WebOrderMessageTranslater())
                        .to("activemq:queue:NEW_ORDER");

            }
        });
        cc.start();
        ProducerTemplate template = cc.createProducerTemplate();
        //template.sendBody("direct:in","test");
        Thread.sleep(1000000);

    }

    class WebOrderMessageTranslater implements org.apache.camel.Processor {

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