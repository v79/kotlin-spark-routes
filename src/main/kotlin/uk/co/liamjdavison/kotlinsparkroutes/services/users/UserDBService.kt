package uk.co.liamjdavison.kotlinsparkroutes.services.users

import com.github.salomonbrys.kodein.instance
import uk.co.liamjdavison.kotlinsparkroutes.db.UserDao
import uk.co.liamjdavison.kotlinsparkroutes.model.User
import uk.co.liamjdavison.kotlinsparkroutes.services.AbstractService

class UserDBService : UserService, AbstractService() {


	val userDao = injectDB.instance<UserDao>()

	override fun getAllUsers(): List<User> {
		return userDao.getAll()
	}

	override fun addUser(user: User): Int {
		return userDao.add(user)
	}

	override fun deleteUser(user: User): Boolean {
		return userDao.delete(user)
	}

	override fun findUsersByName(name: String): List<User>? {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun findUserByName(name: String): User? {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun getUser(id: Int): User? {
		return userDao.get(id)
	}
}