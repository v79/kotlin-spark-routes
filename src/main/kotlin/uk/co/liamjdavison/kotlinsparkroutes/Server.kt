package uk.co.liamjdavison.kotlinsparkroutes

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
import java.util.*


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
		get(path = "/") {
			redirect(location = "/users/")
		}

		displayStartupMessage(portNumber?.toInt())

		// initialize controllers
		val reflections = Reflections(thisPackage.name, MethodAnnotationsScanner(), TypeAnnotationsScanner(), SubTypesScanner())
		val controllers = reflections.getTypesAnnotatedWith(SparkController::class.java)
		controllers.forEach {
			logger.info("Instantiating controller " + it.simpleName)
			it.newInstance()
		}
	}

	override fun init() {
		TODO("Not really necessary; the work is done in the constructor")
	}

	private fun displayStartupMessage(portNumber: Int?) {
		logger.info("=============================================================")
		logger.info("Kotlin Spark Route Tester Started")
		logger.info("Date: " + Date().toString())
		logger.info("OS: " + System.getProperty("os.name"))
		logger.info("Port: " + portNumber)
		logger.info("JDBC URL: " + System.getenv("JDBC_DATABASE_URL"))
		logger.info("=============================================================")
	}

}