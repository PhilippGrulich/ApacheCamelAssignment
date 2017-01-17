package de.tub.da.callcenterChannelAdapter;

import de.tub.da.Order;
import org.apache.camel.Exchange;

/**
 * Created by philipp on 17.01.17.
 */
public class CallcenterMessageTranslator implements org.apache.camel.Processor {
    public void process(Exchange exchange) throws Exception {
        String callcenterMessage = exchange.getIn().getBody(String.class);
        String[] items = callcenterMessage.trim()
                .replace(">", "")
                .replace("<", "")
                .split(",");
        String result = "<" + items[0] + ","
                + items[1].split(" ")[0] + ","
                + items[1].split(" ")[1] + ","
                + items[2] + ","
                + items[2] + ","
                + items[3] + ","
                + "?" + ","
                + "?" + ","
                + "?>";
        // set the output to the file

        Order order = new Order(items[0], items[1].split(" ")[0], items[1].split(" ")[1], 0, items[2], items[3], null, true, true);
        exchange.getOut().setBody(order);


    }
}
