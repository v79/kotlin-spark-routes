package uk.co.liamjdavison.kotlinsparkroutes.db

import com.sun.org.apache.xpath.internal.operations.Bool
import uk.co.liamjdavison.kotlinsparkroutes.db.model.UserDB

/**
 * Created by Liam Davison on 13/07/2017.
 */
class UserDaoImpl : UserDao, AbstractDao() {

	override fun getUser(id: Int): UserDB {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun getAllUsers(): List<UserDB> {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun updateUser(user: UserDB): Bool {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun deleteUser(user: UserDB): Bool {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

}