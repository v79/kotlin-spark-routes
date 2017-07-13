package uk.co.liamjdavison.kotlinsparkroutes.services.users

import uk.co.liamjdavison.kotlinsparkroutes.model.User

/**
 * Created by Liam Davison on 08/07/2017.
 */
class InMemoryUserService : UserService {

	var userList: MutableList<User> = mutableListOf()

	init {
		val liam: User = User(name = "Liam", age = 38)
		userList.add(liam)
	}

	override fun getAllUsers(): List<User> {
		if(userList == null) {
			userList = mutableListOf()
		}
		return userList
	}

	override fun addUser(user: User): Int {
		if(userList == null) {
			userList = mutableListOf()
		}
		userList.add(user)
		return userList.size
	}

	override fun deleteUser(user: User): Boolean {
		if(userList != null) {
			return userList.remove(user)
		}
		return false
	}

	override fun findUsers(name: String): List<User>? {
		var resultList: MutableList<User> = mutableListOf()
		if(userList != null) {
			for(u in userList) {
				if (u.name.equals(name,false)) {
					resultList.add(u)
				}
			}
		}

		return userList
	}
}