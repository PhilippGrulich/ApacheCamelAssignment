

README

This folder contains all applications for the distributed and integrated system.


The System includes five applications:
1. CallcenterSystem:
    This is the callcenter applications which write an order every 10 seconds to the following file:
    ~/callcenter/out.txt    
2. CallcenterChannelAdapter:
    This application is the adapter for the callcenter applications.
    It observes the callcenter file and pushes new orders to the queue.
3. WebOrderSystem:
    This application adds the possibility to place an order by a rest request.
4.Inventory System
    This application checks the inventory
5.Billing System
    This application checks the user permission
6. Result System
    This application displays the result of an order.


Start the system:
The following scripts start the individual components of the distibuted system:

startBillingSystem.sh
startCallcenterChannelAdapter.sh
startInventorySystem.sh
startResultSystem.sh
startWebOrderSystem.sh


Test:
There are two ways to test the system.

1.
WebOrdersystem interface:
Send a Post request to http.//localhost:4567/order.
The request should contain the message "Alice,Test,2,0,0" for a valid request.
All CustomerIDs < 4 are valid.

You can try it out with:
    curl -i -X POST -H "Content-Type:text/plain" -d 'Alice,Test,2,0,0' 'http://localhost:4567/order'

Or just use the validRequest.sh or invalidRequest.sh script.

2.
You can also use the Callcenter Application to create random order request every 10s.
Just run startCallcenterApplication.sh

