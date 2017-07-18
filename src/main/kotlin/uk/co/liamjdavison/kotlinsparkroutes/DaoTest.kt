package uk.co.liamjdavison.kotlinsparkroutes

/**
 * Created by Liam Davison on 17/07/2017.
 */
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.SchemaUtils.drop
import org.jetbrains.exposed.sql.transactions.transaction

object Users : IntIdTable() {
	val name = varchar("name", 50).index()
	val city = reference("city", Cities)
	val age = integer("age")
}

object Cities : IntIdTable() {
	val name = varchar("name", 50)
}

class User(id: EntityID<Int>) : IntEntity(id) {
	companion object : IntEntityClass<User>(Users)

	var name by Users.name
	var city by City referencedOn Users.city
	var age by Users.age
}

class City(id: EntityID<Int>) : IntEntity(id) {
	companion object : IntEntityClass<City>(Cities)

	var name by Cities.name
	val users by User referrersOn Users.city
}


fun main(args: Array<String>) {
	println("========== DAO Example with MariaDB ========")
	Database.connect("jdbc:mysql://127.0.0.1:3306/Employees?user=root&password=indy25tlx", driver = "org.mariadb.jdbc.Driver")
//	Database.connect("jdbc:postgresql://127.0.0.1:5434/Employees?user=postgres&password=indy25tlx", driver = "org.postgresql.Driver")

	transaction {
		//		logger.addLogger(StdOutSqlLogger())

		create(Cities, Users)

		println("===== Creating cities")
		val stPete = City.new {
			name = "St. Petersburg"
		}


		val munich = City.new {
			name = "Munich"
		}

		println("===== Creating users")
		val albert = User.new {
			name = "albert"
			city = stPete
			age = 5
		}

		val bob = User.new {
			name = "bob"
			city = stPete
			age = 27
		}

		val chris = User.new {
			name = "chris"
			city = munich
			age = 42
		}


		println("===== Running queries")
		println("DSLCities: ${City.all().joinToString { it.name }}")
		println("DSLUsers in ${stPete.name}: ${stPete.users.joinToString { it.name }}")
		println("Adults: ${User.find { Users.age greaterEq 18 }.joinToString { it.name }}")

		drop(Users, Cities)
	}
}