import org.apache.spark._

object SparkCoreApp {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("spark-app").setMaster("local[*]")
    val sparkContext = new SparkContext(sparkConf)
    val sum = sparkContext.parallelize(1 to 10).fold(0)(_ + _)
    sparkContext.stop()
  }
}
