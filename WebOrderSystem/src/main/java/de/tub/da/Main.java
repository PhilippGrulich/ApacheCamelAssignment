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

import static org.apache.activemq.camel.component.ActiveMQComponent.activeMQComponent;

/**
 * Created by philipp on 10.01.17.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        CamelContext cc = new DefaultCamelContext();
        ActiveMQComponent activeMQComponent = ActiveMQComponent.activeMQComponent();
        activeMQComponent.setTrustAllPackages(true);
        cc.addComponent("activemq", activeMQComponent);
        cc.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {

                restConfiguration().component("spark-rest");

                from("spark-rest:get:shop/order/:me").multicast().to("direct:console","direct:response");

                from("direct:response").transform().simple("You bought ${header.me}").end();
                from("direct:console").process(new Processor() {
                    public void process(Exchange exchange) throws Exception {
                        exchange.getIn().setBody("lala");
                    }
                }).to("activemq:queue:validationIn");
              //  .

            }
        });


        cc.start();
        ProducerTemplate template = cc.createProducerTemplate();
        //template.sendBody("direct:in","test");
        Thread.sleep(1000000);

    }
}
