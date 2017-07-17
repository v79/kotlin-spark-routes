package uk.co.liamjdavison.kotlinsparkroutes.dsl

/**
 * Created by Liam Davison on 17/07/2017.
 */
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.SchemaUtils.drop
import org.jetbrains.exposed.sql.transactions.transaction

object DSLUsers : Table() {
	val id = varchar("id", 10).primaryKey() // Column<String>
	val name = varchar("name", length = 50) // Column<String>
	val cityId = (integer("city_id") references DSLCities.id).nullable() // Column<Int?>
}

object DSLCities : Table() {
	val id = integer("id").autoIncrement().primaryKey() // Column<Int>
	val name = varchar("name", 50) // Column<String>
}

fun main(args: Array<String>) {
	Database.connect("jdbc:mysql://127.0.0.1:3306/Employees?user=root&password=indy25tlx", driver = "org.mariadb.jdbc.Driver")

	transaction {
		create (DSLCities, DSLUsers)

		val saintPetersburgId = DSLCities.insert {
			it[name] = "St. Petersburg"
		} get DSLCities.id

		val munichId = DSLCities.insert {
			it[name] = "Munich"
		} get DSLCities.id

		DSLCities.insert {
			it[name] = "Prague"
		}

		DSLUsers.insert {
			it[id] = "andrey"
			it[name] = "Andrey"
			it[cityId] = saintPetersburgId
		}

		DSLUsers.insert {
			it[id] = "sergey"
			it[name] = "Sergey"
			it[cityId] = munichId
		}

		DSLUsers.insert {
			it[id] = "eugene"
			it[name] = "Eugene"
			it[cityId] = munichId
		}

		DSLUsers.insert {
			it[id] = "alex"
			it[name] = "Alex"
			it[cityId] = null
		}

		DSLUsers.insert {
			it[id] = "smth"
			it[name] = "Something"
			it[cityId] = null
		}

		DSLUsers.update({ DSLUsers.id eq "alex"}) {
			it[name] = "Alexey"
		}

		DSLUsers.deleteWhere{ DSLUsers.name like "%thing"}

		println("All cities:")

		for (city in DSLCities.selectAll()) {
			println("${city[DSLCities.id]}: ${city[DSLCities.name]}")
		}

		println("Manual join:")
		(DSLUsers innerJoin DSLCities).slice(DSLUsers.name, DSLCities.name).
				select {(DSLUsers.id.eq("andrey") or DSLUsers.name.eq("Sergey")) and
						DSLUsers.id.eq("sergey") and DSLUsers.cityId.eq(DSLCities.id)}.forEach {
			println("${it[DSLUsers.name]} lives in ${it[DSLCities.name]}")
		}

		println("Join with foreign key:")


		(DSLUsers innerJoin DSLCities).slice(DSLUsers.name, DSLUsers.cityId, DSLCities.name).
				select { DSLCities.name.eq("St. Petersburg") or DSLUsers.cityId.isNull()}.forEach {
			if (it[DSLUsers.cityId] != null) {
				println("${it[DSLUsers.name]} lives in ${it[DSLCities.name]}")
			}
			else {
				println("${it[DSLUsers.name]} lives nowhere")
			}
		}

		println("Functions and group by:")

		((DSLCities innerJoin DSLUsers).slice(DSLCities.name, DSLUsers.id.count()).selectAll().groupBy(DSLCities.name)).forEach {
			val cityName = it[DSLCities.name]
			val userCount = it[DSLUsers.id.count()]

			if (userCount > 0) {
				println("$userCount user(s) live(s) in $cityName")
			} else {
				println("Nobody lives in $cityName")
			}
		}

		drop (DSLUsers, DSLCities)

	}
}