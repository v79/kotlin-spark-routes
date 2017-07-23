package uk.co.liamjdavison.kotlinsparkroutes.db

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import uk.co.liamjdavison.kotlinsparkroutes.model.User

/**
 * Created by Liam Davison on 13/07/2017.
 */

object UserCompanion : IntIdTable() {
	val name = varchar("name", 50).index()
	val age = integer("age")
}

class UserDB(id: EntityID<Int>) : IntEntity(id) {
	companion object : IntEntityClass<UserDB>(UserCompanion)

	var name by UserCompanion.name;
	var age by UserCompanion.age
}

class UserDao : AbstractDao(), Dao {

	init {
		transaction {
			//			SchemaUtils.createMissingTablesAndColumns(UserCompanion)
			SchemaUtils.create(UserCompanion)
		}
	}

	fun getAll(): List<User> {
		val userList = mutableListOf<User>()
		transaction {
			UserDB.all().forEach {
				userList.add(convertToModel(it))
			}

		}
		return userList
	}

	fun add(user: User): Int {
		val newId: Int = transaction {
			val newUserDB = UserDB.new {
				name = user.name
				if (user.age != null) {
					age = user.age
				} else {
					age = -1
				}
			}
			newUserDB.id.value
		}
		return newId
	}

	fun delete(user: User): Boolean {
		val userToDelete = findById(user.id)
		if (userToDelete != null) {
			transaction {
				userToDelete.delete()
			}
			return true
		} else {
			return false
		}
	}

	fun get(id: Int): User {
		return convertToModel(findById(id)!!)
	}

	private fun findById(id: Int): UserDB? {
		return transaction {
			UserDB.findById(id)
		}
	}

	private fun convertToModel(userdb: UserDB): User {
		val user: User = User(userdb.id.value, userdb.name, userdb.age)
		return user
	}

}