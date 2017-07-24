package uk.co.liamjdavison.kotlinsparkroutes.db

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.with

val dbModule = Kodein.Module {

	val jdbcUsername: String? = System.getenv("JDBC_DATABASE_USERNAME")
	println("++++++ dbModule jdbcUsername: " + jdbcUsername)
	val jdbcPassword: String? = System.getenv("JDBC_DATABASE_PASSWORD")
	println("++++++ dbModule jdbcPassword: " + jdbcPassword)

	// jdbc:postgresql://ec2-79-125-118-221.eu-west-1.compute.amazonaws.com:5432/dcci0ig3ehbqud?user=wnqtuvuspzaqzs&password=8baeae3bb483fb98b0d645b1cf6d3cb98f8021bb6a324e0c7f8a0eb91360a56c&sslmode=require
	val jdbcUrl: String? = System.getenv("JDBC_URL")



	if (jdbcUsername != null && jdbcPassword != null && jdbcUrl != null && jdbcUrl.startsWith("jdbc", true)) {
		val split: List<String> = jdbcUrl?.split(":")
		split.forEach {
			println("++++++++++++++++++++ " + it)
		}
		// 0: jdbc
		// 1: postgresql
		// 2: domain
		// 3: port
		val connectionString = split[0] + ":" + split[1] + ":" + split[2] + ":" + split[3]
		println("++++++++++ " + connectionString)
		// 4: schema name etc
		val schemaSplit = split[4].split("?")
		val schemaName = schemaSplit[0]


		constant("dbDriverClass") with "org.postgresql.Driver"
		constant("dbConnectionString") with connectionString
		constant("dbDatabase") with schemaName
		constant("dbPassword") with jdbcPassword
		constant("dbUser") with jdbcUsername
	} else {
		constant("dbDriverClass") with "org.mariadb.jdbc.Driver"
		constant("dbConnectionString") with "jdbc:mysql://127.0.0.1:3306/"
		constant("dbDatabase") with "kotlinsparkroutes"
		constant("dbPassword") with "indy25tlx"
		constant("dbUser") with "liam"
	}
}

