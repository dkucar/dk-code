*/
info.txt contents:
M/G ID  ZIPCODE
M 43434 12345
M 89898 12345
G 09098 12345
G 20394 99999
M 11919 99999
...

can we output a list of 
zipcode: count (pairs) where the count represents the number of *unique* member-group pairs in that zipcode
*/

// easiest is to convert csv file to DataFrame, work with SQL

val infoRDD = sc.textFile("info.txt")
val meetupInfo = spark.read.option("inferSchema","true").csv("info.txt").toDF("code","id","zipcode")

meetupInfo.createOrReplaceTempView("meetup_stats")

// create new DataFrames
val bla = spark.sql("SELECT count(id), zipcode  FROM meetup_stats WHERE code='M' GROUP BY zipcode")
val bla2 = spark.sql("SELECT count(id), zipcode  FROM meetup_stats WHERE code='G' GROUP BY zipcode")

// rename column count(id) to number
val blarenamed = bla.toDF("number", "zipcode")
val bla2renamed = bla2.toDF("number", "zipcode")

blarenamed.createOrReplaceTempView("by_member")
bla2renamed.createOrReplaceTempView("by_group")

val groupCount = spark.sql("SELECT sum(number) FROM by_group")
val memberCount = spark.sql("SELECT sum(number) FROM by_member”)

val gc = groupCount.rdd.map(r => r(0)).collect()
val mc = memberCount.rdd.map(r => r(0)).collect()

val total = gc(0).asInstanceOf[Long] * mc(0).asInstanceOf[Long]

