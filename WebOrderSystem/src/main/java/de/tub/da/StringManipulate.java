package de.tub.da;

import org.apache.camel.*;
import org.apache.camel.impl.DefaultComponent;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.impl.ProcessorEndpoint;

import java.util.Map;

/**
 * Created by philipp on 10.01.17.
 */
public class StringManipulate implements Processor {


    public void process(Exchange exchange) throws Exception {
        exchange.getIn().setBody("message:"+exchange.getIn().getBody());
    }
}
