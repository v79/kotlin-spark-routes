package uk.co.liamjdavison.kotlinsparkroutes.db

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.with

val dbModule = Kodein.Module {

	val jdbcUsername = System.getenv("JDBC_DATABASE_USERNAME")
	System.out.println("++++++ dbModule jdbcUsername: " + jdbcUsername)
	val jdbcPassword = System.getenv("JDBC_DATABASE_PASSWORD")
	System.out.println("++++++ dbModule jdbcPassword: " + jdbcPassword)


	constant("dbDriverClass") with "org.mariadb.jdbc.Driver"
	constant("dbConnectionString") with "jdbc:mysql://127.0.0.1:3306/"
	constant("dbDatabase") with "kotlinsparkroutes"
	constant("dbPassword") with "indy25tlx"
	constant("dbUser") with "liam"
}