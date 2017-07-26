package uk.co.liamjdavison.kotlinsparkroutes.controllers

import com.github.salomonbrys.kodein.instance
import spark.ModelAndView
import spark.Spark
import spark.kotlin.get
import spark.kotlin.post
import uk.co.liamjdavison.kotlinsparkroutes.annotations.SparkController
import uk.co.liamjdavison.kotlinsparkroutes.model.User
import uk.co.liamjdavison.kotlinsparkroutes.services.users.UserService

/**
 * Controller for users. Responds to requests under the /users path
 */
@SparkController
class UserController() : AbstractController("/users") {

	lateinit var userService: UserService
	val model: MutableMap<String, Any> = hashMapOf<String, Any>()
	val userControllerHome = path + "/"

	init {
		Spark.path(path) {

			// inject userService at this point; any earlier and it can't be overridden in tests (unless I can get lazy injection working?
			userService = injectServices.instance("db")
			get("/") {
				logger.info("in users with session " + session?.id())
				model.put("title", "List of users")
				model.put("users", getAllUsers())
				session?.let { model.put("session", it) }
				engine.render(ModelAndView(model, "users"))
			}

			get("/add") {
				logger.info("In users/add")
				engine.render(ModelAndView(model, "addUser"))
			}

			post("/add-submit") {
				val u: User = User(request.queryParams("name"), request.queryParams("age").toInt())
				logger.info("Submitting user information for user ${u}")
				userService.addUser(u)
				redirect(userControllerHome)
			}
			get("/delete") {
				logger.info("Attempting to delete user id " + request.queryParams("userId"))
				val userToDelete = userService.getUser(request.queryParams("userId").toInt())
				if (userToDelete != null) {
					userService.deleteUser(userToDelete)
				}
				redirect(userControllerHome)
			}
		}
	}

	private fun getAllUsers(): List<User> {
		return userService.getAllUsers()
	}

}

