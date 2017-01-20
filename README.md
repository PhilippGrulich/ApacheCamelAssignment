

README

This folder contains all applications for the distributed and integrated system.


Setup the system:



Test:

WebOrdersystem interface:
Sende a Post request to http.//localhost:4567/order.
The request should contain the message "Alice,Test,2,0,0" for a valid request.
All CustomerIDs < 4 are valid.

You can try it out with:
	curl -i -X POST -H "Content-Type:text/plain" -d 'Alice,Test,2,0,0' 'http://localhost:4567/order'

Or just use the validRequest.sh or invalidRequest.sh script.