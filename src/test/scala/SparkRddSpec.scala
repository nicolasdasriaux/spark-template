import com.holdenkarau.spark.testing.SharedSparkContext
import org.scalatest.{FlatSpec, Matchers}

class SparkRddSpec extends FlatSpec with Matchers with SharedSparkContext {
  "RDD of Ints" should "calculate sum" in {
    val total = sc.parallelize(Seq(5, 7, 9)).reduce(_ + _)
    total should be(21)
  }
}
