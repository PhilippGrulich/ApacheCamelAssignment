package de.tub.da;

import org.apache.camel.builder.AdviceWithRouteBuilder;


/**
 * Created by philipp on 10.01.17.
 */
public class MyRouteBuilder extends AdviceWithRouteBuilder {
    public void configure() throws Exception {
        from("stream:in").process(new StringManipulate()).to("stream:out");


    }


}
