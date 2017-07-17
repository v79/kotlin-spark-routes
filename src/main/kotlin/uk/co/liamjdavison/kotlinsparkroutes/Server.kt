package uk.co.liamjdavison.kotlinsparkroutes

import io.requery.Persistable
import io.requery.kotlin.eq
import io.requery.sql.Configuration
import io.requery.sql.KotlinConfiguration
import io.requery.sql.KotlinEntityDataStore
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
import uk.co.liamjdavison.kotlinsparkroutes.db.model.UserDB
import java.util.*


//import uk.co.liamjdavison.kotlinsparkroutes.db.model.Models



/**
 * Root class representing our embedded Jetty SparkJava server. The server is initialised, and each controller is constructed.
 * The server port number defaults to 4567; to change it, set the JAVA environment variable server.port (e.g. -Dserver.port=8000).
 * Individual controllers are responsible for their own routes.
 */
class Server : SparkApplication {
	val logger = LoggerFactory.getLogger(Server::class.java)
	val thisPackage = this.javaClass.`package`




	constructor(args: Array<String>) {
		val portNumber: String? = System.getProperty("server.port")
		port(number = portNumber?.toInt() ?: 4567)

		staticFiles.location("/public")




		// initialize controllers
		val reflections = Reflections(thisPackage.name, MethodAnnotationsScanner(), TypeAnnotationsScanner(), SubTypesScanner())
		val controllers = reflections.getTypesAnnotatedWith(SparkController::class.java)
		controllers.forEach {
			logger.info("Instantiating controller " + it.simpleName)
			it.newInstance()
		}





		val mySQLDataSource = org.mariadb.jdbc.MySQLDataSource()
		mySQLDataSource.serverName = "127.0.0.1"
		mySQLDataSource.databaseName = "Employees"
		mySQLDataSource.port = 3306
		mySQLDataSource.user = "root"
		mySQLDataSource.setPassword("indy25tlx")

		val configuration: Configuration = KotlinConfiguration(dataSource = mySQLDataSource, model = uk.co.liamjdavison.kotlinsparkroutes.db.model.Models.DEFAULT)
		val data = KotlinEntityDataStore<Persistable>(configuration)

		val result = data.invoke {
			select(UserDB::class) where (UserDB::name eq "Liam") limit 5

		}

		val first = result.get().first()
		println(first)


		val all = data.invoke {
			select(UserDB::class)
		}
		all.get().forEach { println(it) }

		var mum: UserDB = uk.co.liamjdavison.kotlinsparkroutes.db.model.UserDBEntity()
		mum.name = "Eliza"
		mum.age = 63

		val insert  = data.insert(mum)

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