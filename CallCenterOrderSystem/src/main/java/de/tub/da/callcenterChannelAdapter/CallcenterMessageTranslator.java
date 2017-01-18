package de.tub.da.callcenterChannelAdapter;

import de.tu.comon.Order;
import org.apache.camel.Exchange;

import java.util.HashMap;

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

        HashMap<String,String> map = new HashMap<String, String>();
        map.put("cid", items[0]);
        map.put("firstName", items[1].split(" ")[0]);
        map.put("lastName", items[1].split(" ")[1]);
        map.put("count", null);
        map.put("surf", items[2]);
        map.put("diving", items[3]);
        map.put("orderId", null);
        map.put("valid", null);
        map.put("validation", null);

        exchange.getOut().setBody(map);


    }
}
