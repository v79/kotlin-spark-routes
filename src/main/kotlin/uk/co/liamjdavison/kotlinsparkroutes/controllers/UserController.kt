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
class UserController : AbstractController("/users") {

	lateinit var userService: UserService
//	val model: MutableMap<String, Any> = hashMapOf<String, Any>()
//	val userControllerHome = path + "/"

	init {
		// inject userService at this point; any earlier and it can't be overridden in tests (unless I can get lazy injection working?
		userService = injectServices.instance("db")

		Spark.path(path) {

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

				val errorMap = u.validate()
				if (!errorMap.isEmpty()) {
					model.put("name", u.name)
					model.put("age", u.age)
//					request.session().attribute("errors", errorMap)
					flash(request, "errors", errorMap)
					redirect(controllerHome)

				} else {
					logger.info("Submitting user information for user ${u}")
					model.clear()
					userService.addUser(u)
					redirect(controllerHome)
				}
			}
			get("/delete") {
				logger.info("Attempting to delete user id " + request.queryParams("userId"))
				val userToDelete = userService.getUser(request.queryParams("userId").toInt())
				if (userToDelete != null) {
					userService.deleteUser(userToDelete)
				}
				redirect(controllerHome)
			}

			// Ajax paths all go here
			Spark.path("/ajax/") {
				get("delete/*") {
					val model: MutableMap<String, Any> = hashMapOf<String, Any>()
					logger.info("delete called with splat " + request.splat()[0])
					val userId: String? = request.splat()[0]

					userId?.let {
						val userToDelete = userService.getUser(it.toInt())
						userToDelete?.let { it1 -> model.put("userToDelete", it1) }
					}
					engine.render(ModelAndView(model, "modals/users-delete"))
				}
			}
		}
	}


	private fun getAllUsers(): List<User> {
		return userService.getAllUsers()
	}

}

