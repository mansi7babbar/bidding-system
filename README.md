# Bidding System
- Layered architecture with model, view, and controller layers.

### Controller layer:
- Used to switch control between view and model layer

### View layer:
- Contains a listener actor where an HTTP server is created and listens to incoming requests from defined routes.
- Contains a publisher actor to send the response of the requests to the server.
- Contains a worker actor to send work to the controller for processing business logic and then send work success/failure status to the publisher.

### Model layer:
- Contains repository classes where DB operations are performed.
- Contains a service actor where business logic is defined.
- Contains a worker actor to receive work from the controller for processing and then send work success/failure status back to the controller.

### Testing:
- Created mock HTTP server.
- Added module tests for <em>BidRequest</em>.

### Commands to execute:
- Run Bidding System - sbt run
- Test Bidding System - sbt test

### Note:
- Modified Targeting case class with field <em>Seq[Site]</em> instead of <em>Seq[String]</em>.
- Using <em>Seq[String]</em> collection for <em>targetedSites</em> as it could be a very long list.
- Using empty string as <em>id</em> in default <em>BidResponse</em> as no details are specified for <em>id</em> in documentation.
