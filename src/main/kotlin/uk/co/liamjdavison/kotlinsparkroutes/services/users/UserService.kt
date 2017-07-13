package uk.co.liamjdavison.kotlinsparkroutes.services.users

import uk.co.liamjdavison.kotlinsparkroutes.model.User

/**
 * Created by Liam Davison on 08/07/2017.
 */
interface UserService{

	fun getAllUsers(): List<User>

	fun addUser(user: User): Int

	fun deleteUser(user: User): Boolean

	fun findUsers(name: String): List<User>?

}