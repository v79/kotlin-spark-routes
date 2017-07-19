package uk.co.liamjdavison.kotlinsparkroutes.services.users

import uk.co.liamjdavison.kotlinsparkroutes.model.User

/**
 * Created by Liam Davison on 08/07/2017.
 */
interface UserService{

	/**
	 * Return a list of all the users
	 */
	fun getAllUsers(): List<User>

	/**
	 * Add the given user
	 */
	fun addUser(user: User): Int

	/**
	 * Remove the given user
	 */
	fun deleteUser(user: User): Boolean

	/**
	 * Find all users which match the given name
	 */
	fun findUsersByName(name: String): List<User>?

	/**
	 * Find just one user which matches the given name
	 */
	fun findUserByName(name: String): User?

}