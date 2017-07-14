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
 * Controller for users. Responds to requests under the /users/ path
 */
@SparkController
class UserController() : AbstractController("/users") {

	lateinit var userService: UserService

	init {
		Spark.path(path) {

			// inject userService at this point; any earlier and it can't be overridden in tests (unless I can get lazy injection working?
			userService = kodein.instance()

			get("/") {
				logger.info("in users with session " + session?.id())
				val model: MutableMap<String, List<User>> = hashMapOf<String, List<User>>()
				model.put("users", getAllUsers())
				engine.render(ModelAndView(model, "users"))
			}

			get("/add") {
				logger.info("In users/add")
				val model: MutableMap<String, String> = hashMapOf<String, String>()
				engine.render(ModelAndView(model, "addUser"))
			}

			post("/add-submit") {
				val u: User = User(request.queryParams("name"), request.queryParams("age").toInt())
				logger.info("Submitting user information for user ${u}")
				userService.addUser(u)

				redirect("/")
			}
		}
	}

	private fun getAllUsers(): List<User> {
		return userService.getAllUsers()
	}

	fun sayHello(): String {
		val listSize = userService.getAllUsers().size
		return "Hello " + listSize
	}

}

