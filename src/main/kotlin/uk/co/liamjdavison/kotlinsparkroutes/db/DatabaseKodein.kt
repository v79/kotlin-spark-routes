package uk.co.liamjdavison.kotlinsparkroutes.db

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.with
import java.net.URI


val dbModule = Kodein.Module {

	if (System.getenv("DATABASE_URL") != null) {
		// jdbc:postgresql://ec2-79-125-118-221.eu-west-1.compute.amazonaws.com:5432/dcci0ig3ehbqud?user=wnqtuvuspzaqzs&password=8baeae3bb483fb98b0d645b1cf6d3cb98f8021bb6a324e0c7f8a0eb91360a56c&sslmode=require
		val dbUri = URI(System.getenv("DATABASE_URL"))

		val jdbcUsername = dbUri.userInfo.split(":")[0]
		val jdbcPassword = dbUri.userInfo.split(":")[1]
		val dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() // + dbUri.getPath()

		//println("++++++++++++ extracted dbUrl: " + dbUrl)
		//println("++++++++++++ extracted jdbcUsername: " + jdbcUsername)
		//println("++++++++++++ extracted jdbcPassword: " + jdbcPassword)
		//println("++++++++++++ extracted path: " + dbUri.path)

		constant("dbDriverClass") with "org.postgresql.Driver"
		constant("dbConnectionString") with dbUrl
		constant("dbDatabase") with dbUri.path
		constant("dbPassword") with jdbcPassword
		constant("dbUser") with jdbcUsername
	} else {
		// use our locally configured MariaDB instance
		constant("dbDriverClass") with "org.mariadb.jdbc.Driver"
		constant("dbConnectionString") with "jdbc:mysql://127.0.0.1:3306/"
		constant("dbDatabase") with "kotlinsparkroutes"
		constant("dbPassword") with "indy25tlx"
		constant("dbUser") with "liam"
	}
}

