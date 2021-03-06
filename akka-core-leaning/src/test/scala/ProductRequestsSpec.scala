import org.scalatest.{Matchers, WordSpec}
import akka.http.scaladsl.model.{ContentTypes, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.actor.{ActorSystem, Props}
import akka.event.{Logging, LoggingAdapter}
import akka.stream.ActorMaterializer
import org.biosphere.labs.akka.learning.actors.{GreetingFetcher, ProductCacher}
import org.biosphere.labs.akka.learning.domain.Product
import org.biosphere.labs.akka.learning.utils._

class ProductRequestsSpec extends WordSpec with Matchers with ScalatestRouteTest with Service {

  val productRoadRunner = Product("ACME", "RoadRunner")
  val productChaparral = Product("ACME", "Chaparral")

  override implicit val actorSystem = ActorSystem()
  //override implicit def executor: ExecutionContext = actorSystem.dispatcher
  override implicit val log: LoggingAdapter = Logging(actorSystem, getClass)
  override implicit val materializer: ActorMaterializer = ActorMaterializer()
  override val productCacher = actorSystem.actorOf(Props[ProductCacher], "product-cacher")
  override val productWriter = actorSystem.actorOf(Props[ProductCacher], "product-writer")
  override val greetingFetcher = actorSystem.actorOf(Props[GreetingFetcher], "greetingFetcher")

  "The service" should {
    "return a product for GET requests to the /product path" in {
      Get("/product") ~> routes ~> check {
        responseAs[Product] shouldEqual productRoadRunner
        contentType shouldBe ContentTypes.`application/json`
        status shouldBe StatusCodes.OK
      }
    }
  }

  it should {
    "respond with a HTML text if the request if of the Product type" in {
      Post(s"/product", Product(productChaparral.brand, productChaparral.name)) ~> routes ~> check {
        status shouldBe StatusCodes.OK
        contentType shouldBe ContentTypes.`application/json`
        responseAs[Product] shouldBe Product("ACME", "Chaparral-Persisted-1")
      }
    }
  }
/*
  it should {
    "respond with a GreetingResponse got from internet " in {
      Post(s"/product", GreetingRequest("Greeting!")) ~> routes ~> check {
        status shouldBe StatusCodes.OK
        contentType shouldBe ContentTypes.`application/json`
        responseAs[Product] some GreetingResponse
      }
    }
    */
}
