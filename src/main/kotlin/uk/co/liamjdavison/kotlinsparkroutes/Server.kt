package uk.co.liamjdavison.kotlinsparkroutes

import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.reflections.Reflections
import org.reflections.scanners.MethodAnnotationsScanner
import org.reflections.scanners.SubTypesScanner
import org.reflections.scanners.TypeAnnotationsScanner
import org.slf4j.LoggerFactory
import spark.kotlin.get
import spark.kotlin.port
import spark.kotlin.staticFiles
import spark.servlet.SparkApplication
import uk.co.liamjdavison.kotlinsparkroutes.annotations.SparkController
import java.util.Date


/**
 * Root class representing our embedded Jetty SparkJava server. The server is initialised, and each controller is constructed.
 * The server port number defaults to 4567; to change it, set the JAVA environment variable server.port (e.g. -Dserver.port=8000).
 * Individual controllers are responsible for their own routes.
 */

object Wibble : Table() {
	val id = integer("id").autoIncrement().primaryKey() // Column<String>
	val name = varchar("name", length = 50) // Column<String>
	val age = integer(name = "age")
	val homeId = (integer("hometown_id") references HomeTown.id).nullable() // Column<Int?>
}

object HomeTown : Table() {
	val id = integer("id").autoIncrement().primaryKey() // Column<String>
	val name = varchar("name", length = 50) // Column<String>
}

object Books : LongIdTable() {
	val title = varchar(name = "title", length = 256)
	val pages = integer(name = "pages")
	val author = reference("author", Authors)
}

object Authors : IntIdTable() {
	val fname = varchar(name = "fname", length = 128).nullable()
	val sname = varchar(name = "sname", length = 128)
}

class Book(id: EntityID<Long>) : LongEntity(id) {
	companion object : LongEntityClass<Book>(table = Books)

	var title by Books.title
	var pages by Books.pages
	var author by Author referencedOn Books.author
}

class Author(id: EntityID<Int>) : IntEntity(id) {
	companion object : IntEntityClass<Author>(table = Authors)

	var fname by Authors.fname
	var sname by Authors.sname

}


//class User(id: EntityID<Int>) : IntEntity(id) {
//	companion object : IntEntityClass<User>(DSLUsers)
//
//	var name by DSLUsers.name
//	var city by City referencedOn DSLUsers.city
//	var age by DSLUsers.age
//}

class Server : SparkApplication {
	val logger = LoggerFactory.getLogger(Server::class.java)
	val thisPackage = this.javaClass.`package`

	constructor(args: Array<String>) {
		val portNumber: String? = System.getProperty("server.port")
		port(number = portNumber?.toInt() ?: 4567)

		staticFiles.location("/public")

		println("=============   SQL DSL ============")
		Database.connect("jdbc:mysql://127.0.0.1:3306/Employees?user=root&password=indy25tlx", driver = "org.mariadb.jdbc.Driver")

		transaction {

			SchemaUtils.drop(Wibble, HomeTown)
			SchemaUtils.createMissingTablesAndColumns(Wibble, HomeTown)

			val dunfId = HomeTown.insert { it[name] = "Dunfermline" } get HomeTown.id
			val edinId = HomeTown.insert { it[name] = "Edinburgh" } get HomeTown.id
			val ceresId = HomeTown.insert { it[name] = "Ceres" } get HomeTown.id

			val liamId = Wibble.insert { it[name] = "Liam"; it[age] = 38; it[homeId] = edinId } get Wibble.id
			val mumId = Wibble.insert { it[name] = "Eliza"; it[age] = 63; it[homeId] = ceresId } get Wibble.id
			val dadId = Wibble.insert { it[name] = "Thomas"; it[age] = 61; it[homeId] = ceresId } get Wibble.id
			val neilId = Wibble.insert { it[name] = "Neil"; it[age] = 35; it[homeId] = dunfId } get Wibble.id
			val charlotteId = Wibble.insert { it[name] = "Charlotte"; it[age] = 8; it[homeId] = dunfId } get Wibble.id



			println("All wibbles:")
			for (wibble in Wibble.selectAll()) {
				println("wibble : ${wibble[Wibble.id]} is ${wibble[Wibble.name]} and ${wibble[Wibble.age]} years young.")
			}

			println("All home towns:")
			for (home in HomeTown.selectAll()) {
				println("Town : ${home[HomeTown.id]} '${home[HomeTown.name]}'")
			}

			println("Grandparents:")
			val grandparents = Wibble.select { Wibble.age.greaterEq(60) }
			for (g in grandparents) {
				println("wibble : ${g[Wibble.id]} is ${g[Wibble.name]} and ${g[Wibble.age]} years young.")
			}

			print("People in Dunfermline: ")
			val dunfermliners = (Wibble innerJoin HomeTown).slice(Wibble.id, Wibble.name, Wibble.age, Wibble.homeId, HomeTown.name).
					select { HomeTown.name.eq("Dunfermline") }
			print(dunfermliners.count())
			println()
			for (d in dunfermliners) {
				println("wibble : ${d[Wibble.id]} is ${d[Wibble.name]} and ${d[Wibble.age]} years young living in ${d[HomeTown.name]}.")
			}

		}

	/*	println("=============   DAOS  ============")
		Database.connect("jdbc:mysql://127.0.0.1:3306/Employees?user=root&password=indy25tlx", driver = "org.mariadb.jdbc.Driver")
		transaction {

			SchemaUtils.drop(Authors, Books)
			SchemaUtils.createMissingTablesAndColumns(Authors, Books)

			val iainB = Author.new { fname = "Iain"
				sname = "Banks" }
			val emForster = Author.new { fname = "E.M."
				sname = "Forster"}
			val mAtwood = Author.new { fname = "Margaret"
				sname = "Atwood"}

			println("All authors:")
			Author.all().forEach { println("${it.fname} ${it.sname}") }
		}*/


//
//			(DSLUsers innerJoin DSLCities).slice(DSLUsers.name, DSLUsers.cityId, DSLCities.name).
//					select {DSLCities.name.eq("St. Petersburg") or DSLUsers.cityId.isNull()}.forEach {
//				if (it[DSLUsers.cityId] != null) {
//					println("${it[DSLUsers.name]} lives in ${it[DSLCities.name]}")
//				}
//				else {
//					println("${it[DSLUsers.name]} lives nowhere")
//				}
//			}

//		}


		// initialize controllers
		val reflections = Reflections(thisPackage.name, MethodAnnotationsScanner(), TypeAnnotationsScanner(), SubTypesScanner())
		val controllers = reflections.getTypesAnnotatedWith(SparkController::class.java)
		controllers.forEach {
			logger.info("Instantiating controller " + it.simpleName)
			it.newInstance()
		}

		displayStartupMessage()

		get(path = "/") {
			redirect(location = "/users/")
		}
	}

	override fun init() {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	private fun displayStartupMessage() {
		logger.info("=============================================================")
		logger.info("Kotlin Spark Route Tester Started")
		logger.info("Date: " + Date().toString())
		logger.info("OS: " + System.getProperty("os.name"))
		logger.info("Port: " + port())
		logger.info("=============================================================")
	}
}