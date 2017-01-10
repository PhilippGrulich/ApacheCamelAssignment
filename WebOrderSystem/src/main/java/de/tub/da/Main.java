package de.tub.da;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.ProcessorEndpoint;

/**
 * Created by philipp on 10.01.17.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        CamelContext cc = new DefaultCamelContext();

        ProcessorEndpoint pe = new ProcessorEndpoint("", cc, new StringManipulate());

        cc.addEndpoint("test",pe);


        cc.addRoutes(new MyRouteBuilder());


        cc.start();
        ProducerTemplate template = cc.createProducerTemplate();
        //template.sendBody("direct:in","test");
        Thread.sleep(1000000);

    }
}
