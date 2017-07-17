package uk.co.liamjdavison.kotlinsparkroutes.db

import com.sun.org.apache.xpath.internal.operations.Bool
import io.requery.kotlin.eq
import uk.co.liamjdavison.kotlinsparkroutes.db.model.UserDB

/**
 * Created by Liam Davison on 13/07/2017.
 */
class UserDaoImpl : UserDao, AbstractDao() {

	override fun getUser(id: Int): UserDB? {

		val result = data.invoke {
			select(UserDB::class) where (UserDB::id eq id) limit 1
		}

		return result.get().first()
	}

	override fun getAllUsers(): List<UserDB> {
		val result = data.invoke {
			select(UserDB::class)
		}
		if (result.get() != null) {

		}

		return emptyList()
	}

	override fun saveOrUpdate(user: UserDB): Boolean {
		data.withTransaction {
			 update(user)
		}
		return false
	}

	override fun deleteUser(user: UserDB): Boolean {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

}