import com.holdenkarau.spark.testing.DatasetSuiteBase
import org.scalatest.{FlatSpec, Matchers}
import org.apache.spark.sql.expressions.scalalang.typed

case class Stock(name: String, quantity: Int)

class SparkDatasetSpec extends FlatSpec with Matchers with DatasetSuiteBase {
  "Dataset" should "" in {
    val sql = sqlContext
    import sql.implicits._

    val stocks = sc.parallelize(Seq(1, 2, 3, 1, 2, 3)).toDS.map(id => Stock(s"Name$id", id * 5))

    val totals = stocks
      .groupByKey(_.name)
      .agg(typed.sum[Stock](_.quantity))
      .head
  }
}
