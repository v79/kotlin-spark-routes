package uk.co.liamjdavison.kotlinsparkroutes.db

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import org.jetbrains.exposed.sql.Database
import org.slf4j.LoggerFactory

/**
 * Created by Liam Davison on 13/07/2017.
 */
abstract class AbstractDao : Dao {
	open val logger = LoggerFactory.getLogger(AbstractDao::class.java)

	open var dbConnections = Kodein {
		import(dbModule)
	}

	init {
		val dbConnectionString: String = dbConnections.instance("dbConnectionString")
		val dbName: String = dbConnections.instance("dbDatabase")
		val dbUser: String = dbConnections.instance("dbUser")
		val dbPassword: String = dbConnections.instance("dbPassword")
		val dbDriver: String = dbConnections.instance("dbDriverClass")

		logger.info("Connecting to DB: ${dbConnectionString}${dbName}")  // ?user=${dbUser}&password=${dbPassword}
		logger.info("Using driver: ${dbDriver}")
		Database.connect(url = dbConnectionString + dbName, user = dbUser, password = dbPassword, driver = dbDriver)
	}
}