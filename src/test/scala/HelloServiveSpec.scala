import org.scalatest._

class HelloServiceSpec extends FlatSpec with Matchers {
  "Hello Service" should "say hello" in {
    val helloService = new HelloService
    helloService.sayHello("Paul") should be("Hello Paul!")
  }
}
