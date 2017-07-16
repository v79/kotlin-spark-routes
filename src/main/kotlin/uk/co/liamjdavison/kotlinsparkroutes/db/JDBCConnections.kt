package uk.co.liamjdavison.kotlinsparkroutes.db

import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement

/**
 * Created by Liam Davison on 16/07/2017.
 */
fun jdbcConnections() {
	val JDBC_DRIVER = "org.mariadb.jdbc.Driver"
	val DB_URL = "jdbc:mysql://localhost/Employees"

	//  Database credentials
	val USER = "root"
	val PASS = "indy25tlx"

	var connection: Connection? = null
	var stmt: Statement? = null

	try {
		Class.forName(JDBC_DRIVER)
//STEP 3: Open a connection
		println("Connecting to database...")
		connection = DriverManager.getConnection(DB_URL, USER, PASS)

		//STEP 4: Execute a query
		println("Creating statement...")
		stmt = connection.createStatement()
		val sql: String
		sql = "SELECT id, name, age FROM Employees"
		val rs = stmt.executeQuery(sql)


		//STEP 5: Extract data from result set
		while (rs.next()) {
			//Retrieve by column name
			val id = rs.getInt("id")
			val age = rs.getInt("age")
			val name = rs.getString("name")

			//Display values
			print("ID: " + id)
			print(", Age: " + age)
			println(", Name: " + name)
		}
		//STEP 6: Clean-up environment
		rs.close()
		stmt.close()
		connection.close()

	} catch (e: Exception) {
		println(e)
	} finally {
		stmt?.close()
		connection?.close()
	}
}