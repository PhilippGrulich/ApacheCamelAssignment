package de.tub.da;

import javax.jms.ObjectMessage;
import java.io.Serializable;

/**
 * Created by philipp on 17.01.17.
 */
public class Order  implements ObjectMessage {
    String CustomerID;
    String FirstName;
    String LastName;
    int OverallITems;
    String NumberOfDrivingSuits;
    String NumberOfSurfboards;
    String OrderID;
    boolean Valid;
    boolean validationResult;

    public Order(String customerID, String firstName, String lastName, int overallITems, String numberOfDrivingSuits, String numberOfSurfboards, String orderID, boolean valid, boolean validationResult) {
        CustomerID = customerID;
        FirstName = firstName;
        LastName = lastName;
        OverallITems = overallITems;
        NumberOfDrivingSuits = numberOfDrivingSuits;
        NumberOfSurfboards = numberOfSurfboards;
        OrderID = orderID;
        Valid = valid;
        this.validationResult = validationResult;
    }

    @Override
    public String toString() {
        return "Order{" +
                "CustomerID='" + CustomerID + '\'' +
                ", FirstName='" + FirstName + '\'' +
                ", LastName='" + LastName + '\'' +
                ", OverallITems=" + OverallITems +
                ", NumberOfDrivingSuits='" + NumberOfDrivingSuits + '\'' +
                ", NumberOfSurfboards='" + NumberOfSurfboards + '\'' +
                ", OrderID='" + OrderID + '\'' +
                ", Valid=" + Valid +
                ", validationResult=" + validationResult +
                '}';
    }
}
