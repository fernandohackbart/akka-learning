Some curls requests to test the behavior
----------------------------------------

*curl -XPOST -H "Content-Type:application/json" -d '{"brand":"IKEA","name":"Malmo"}' http://localhost:9000/product
*curl -XPOST -H "Content-Type:application/json" -d '{"messageBody":"Greetings!"}' http://localhost:9000/product
*curl -XGET  http://localhost:9000/product
*curl -XGET  http://localhost:9000/status
