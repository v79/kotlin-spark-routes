package uk.co.liamjdavison.kotlinsparkroutes.controllers

import spark.ModelAndView
import spark.Spark
import spark.kotlin.get
import spark.kotlin.post
import uk.co.liamjdavison.kotlinsparkroutes.annotations.SparkController
import uk.co.liamjdavison.kotlinsparkroutes.model.User
import uk.co.liamjdavison.kotlinsparkroutes.services.users.InMemoryUserService
import uk.co.liamjdavison.kotlinsparkroutes.services.users.UserService

/**
 * Created by Liam Davison on 17/06/2017.
 */


/**
 * Controller for users. Responds to requests under the /users/ path
 */
@SparkController
class UserController() : AbstractController("/users") {
	var users: MutableList<User> = mutableListOf()
//	val neil: User = User(name="Neil",age = 35)
//	val ageless: User = User(name ="Unknown")

	val userService: UserService = InMemoryUserService()
	init {


//		users.add(neil)
//		users.add(ageless)

		Spark.path(path) {
			get("/") {
				logger.info("in users with session " + session?.id())

				users = userService.getAllUsers() as MutableList<User>
//				if (users.isEmpty()) {
//					users.add(User("Liam", 38))
//				}
				val model: MutableMap<String, List<User>> = hashMapOf<String, List<User>>()
				model.put("users", users)
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

//				users.add(u)
				redirect("/")
			}
		}
	}

}