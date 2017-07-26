import com.holdenkarau.spark.testing.DataFrameSuiteBase
import org.scalatest.{FlatSpec, Matchers}
import org.apache.spark.sql.functions._

class SparkDataFrameSpec extends FlatSpec with Matchers with DataFrameSuiteBase {
  "DataFrame" should "sum" in {
    val sql = sqlContext
    import sql.implicits._

    val df = sc.parallelize(Seq(1, 2, 3)).toDF("value")
    val total = df.agg(sum($"value").as("total")).head
    total.getAs[Int]("total") should be (6)
  }
}
