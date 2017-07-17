package uk.co.liamjdavison.kotlinsparkroutes.db

import io.requery.Persistable
import io.requery.sql.Configuration
import io.requery.sql.KotlinConfiguration
import io.requery.sql.KotlinEntityDataStore
import org.slf4j.LoggerFactory

/**
 * Created by Liam Davison on 13/07/2017.
 */
abstract class AbstractDao {
	open val logger = LoggerFactory.getLogger(AbstractDao::class.java)

	val mySQLDataSource = org.mariadb.jdbc.MySQLDataSource()
	init {
		mySQLDataSource.serverName = "127.0.0.1"
		mySQLDataSource.databaseName = "Employees"
		mySQLDataSource.port = 3306
		mySQLDataSource.user = "root"
		mySQLDataSource.setPassword("indy25tlx")
	}

	val configuration: Configuration = KotlinConfiguration(dataSource = mySQLDataSource, model = uk.co.liamjdavison.kotlinsparkroutes.db.model.Models.DEFAULT)
	val data = KotlinEntityDataStore<Persistable>(configuration)
}