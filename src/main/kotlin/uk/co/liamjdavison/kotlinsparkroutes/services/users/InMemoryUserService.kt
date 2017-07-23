package uk.co.liamjdavison.kotlinsparkroutes.services.users

import uk.co.liamjdavison.kotlinsparkroutes.model.User

/**
 * The simplest implementation of UserService. All users are stored in a standard mutable list.
 */
class InMemoryUserService : UserService {

	var userList: MutableList<User> = mutableListOf()

	init {
		val liam: User = User(name = "Liam", age = 38)
		userList.add(liam)
	}

	override fun getAllUsers(): List<User> {
		if (userList == null) {
			userList = mutableListOf()
		}
		return userList
	}

	override fun addUser(user: User): Int {
		if (userList == null) {
			userList = mutableListOf()
		}
		userList.add(user)
		return userList.size
	}

	override fun deleteUser(user: User): Boolean {
		println("in userservice and attempting to delete user ${user}")
		var result: Boolean = false
		if (userList != null) {

			result = userList.remove(user)
		}
		return result
	}

	override fun findUsersByName(name: String): List<User>? {
		var resultList: MutableList<User> = mutableListOf()
		if (userList != null) {
			for (u in userList) {
				if (u.name.equals(name, false)) {
					resultList.add(u)
				}
			}
		}

		return userList
	}

	override fun findUserByName(name: String): User? {
		for (u in userList) {
			if (u.name.equals(name, false))
				return u
		}
		return null
	}

	override fun getUser(id: Int): User? {
		return userList.get(id)
	}

}