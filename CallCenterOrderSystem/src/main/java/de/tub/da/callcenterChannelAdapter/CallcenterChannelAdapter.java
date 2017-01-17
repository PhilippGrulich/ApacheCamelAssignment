package de.tub.da.callcenterChannelAdapter;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

/**
 * Created by philipp on 17.01.17.
 */
public class CallcenterChannelAdapter {

    public static void main(String[] args) throws Exception {

        String tempdir = System.getProperty("user.home");
        final String fileDir = tempdir+"/callcenter/";

        System.out.println(fileDir);
        CamelContext cc = new DefaultCamelContext();
        ActiveMQComponent activeMQComponent = ActiveMQComponent.activeMQComponent();
        activeMQComponent.setTrustAllPackages(true);
        cc.addComponent("activemq", activeMQComponent);
        cc.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("file://"+fileDir+"?move=.done")
                        .process(new CallcenterMessageTranslator())
                        .multicast()
                        .to("activemq:queue:NEW_ORDER")
                        .to("stream:out");
            }
        });


        cc.start();
        Thread.sleep(10000000);

    }
}
