package uk.co.liamjdavison.kotlinsparkroutes.db

import com.sun.org.apache.xpath.internal.operations.Bool
import uk.co.liamjdavison.kotlinsparkroutes.db.model.UserDB

/**
 * Created by Liam Davison on 13/07/2017.
 */
interface UserDao {

	fun getUser(id:Int): UserDB

	fun getAllUsers(): List<UserDB>

	fun updateUser(user: UserDB): Bool

	fun deleteUser(user: UserDB): Bool
}