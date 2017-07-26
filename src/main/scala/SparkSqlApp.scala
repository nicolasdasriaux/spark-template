import org.apache.spark.sql._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.expressions.scalalang.typed

case class Customer(id: Int, firstName: String, lastName: String)
case class OrderLine(customerId: Int, itemId: Int)
case class Item(id: Int, name: String)

object SparkSqlApp {
  def main(args: Array[String]): Unit = {
    val sparkSession = SparkSession.builder()
      .appName("spark-sql-app")
      .master("local[*]")
      .enableHiveSupport()
      .getOrCreate()

    import sparkSession.implicits._

    // ------ Data Frames

    val customers = sparkSession.sparkContext.parallelize(1 to 10).map(id => Customer(id, s"FirstName$id", s"LastName$id")).toDF

    val orderLineSeq = for {
      customerId <- 1 to 10
      itemId <- 1 to 10
    } yield OrderLine(customerId, (customerId - 1) * 10 + itemId)

    val orderLines = sparkSession.sparkContext.parallelize(orderLineSeq).toDF
    val items = sparkSession.sparkContext.parallelize(1 to 100).map(id => Item(id, s"Name$id")).toDF

    customers.write.mode(SaveMode.Overwrite).parquet("customers.parquet")
    customers.select($"firstName").filter($"id" === 1).show()

    customers.createOrReplaceTempView("customers")
    orderLines.createOrReplaceTempView("orderLines")
    items.write.mode(SaveMode.Overwrite).saveAsTable("items")

    sparkSession.table("customers").show()
    sparkSession.table("orderLines").show()
    sparkSession.table("items").show()

    sparkSession.table("customers").filter($"id" === 2).select($"firstName").show()
    sparkSession.table("items").show()

    val customersTable = sparkSession.table("customers")
    val orderLinesTable = sparkSession.table("orderLines")
    val itemsTable = sparkSession.table("items")

    val query = customersTable
      .withColumn("fullName", concat($"firstName", lit(" "), $"lastName"))
      .drop("firstName", "lastName")
      .join(orderLinesTable, customersTable("id") === orderLinesTable("customerId"))
      .join(itemsTable, orderLinesTable("itemId") === itemsTable("id"))

    query.show()
    query.explain()

    sparkSession.sql("select * from customers").show()

    // ------ Datasets

    val orderLinesDS: Dataset[OrderLine] = sparkSession.sparkContext.parallelize(orderLineSeq).toDS

    val counts: Dataset[(Int, Long)] = orderLinesDS
      .groupByKey(_.customerId)
      .agg(typed.count[OrderLine](_.itemId))

    counts.show()

    sparkSession.stop()
  }
}
