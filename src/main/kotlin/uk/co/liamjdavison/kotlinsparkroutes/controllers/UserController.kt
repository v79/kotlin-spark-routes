package uk.co.liamjdavison.kotlinsparkroutes.controllers

import spark.ModelAndView
import spark.kotlin.get
import spark.kotlin.post
import uk.co.liamjdavison.kotlinsparkroutes.annotations.SparkController
/**
 * Created by Liam Davison on 17/06/2017.
 */

data class User(val name: String, val age: Int)

/**
 * Controller for users. Responds to requests under the /users/ path
 */
@SparkController
class UserController : AbstractController() {

	val users: MutableList<User> = mutableListOf()

	init {

		get("/users") {
			logger.info("in users with session " + session?.id())

			if(users.isEmpty()) {
				users.add(User("Liam",38))
			}
			val model: MutableMap<String,List<User>> = hashMapOf<String,List<User>>()
			model.put("users",users)
			engine.render(ModelAndView(model,"users"))
		}

		get("/users/add") {
			logger.info("In users/add")
			val model: MutableMap<String,String> = hashMapOf<String,String>()
			engine.render(ModelAndView(model,"addUser"))
		}

		post("/users/add-submit") {
			val u:User = User(request.queryParams("name"),request.queryParams("age").toInt())
			logger.info("Submitting user information for user ${u}")
			users.add(u)
			redirect("/users")
		}
	}

}