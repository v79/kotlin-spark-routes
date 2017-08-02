package uk.co.liamjdavison.kotlinsparkroutes.controllers

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.provider
import org.slf4j.LoggerFactory
import spark.Session
import spark.kotlin.after
import spark.kotlin.before
import spark.kotlin.notFound
import uk.co.liamjdavison.ThymeleafEngine
import uk.co.liamjdavison.kotlinsparkroutes.services.users.InMemoryUserService
import uk.co.liamjdavison.kotlinsparkroutes.services.users.UserDBService
import uk.co.liamjdavison.kotlinsparkroutes.services.users.UserService

/**
 * Base class for all controllers. It defines a logger and the rendering engine, plus before and after filters,
 * notFound routes, and other common routes
 */
abstract class AbstractController(path: String) {
	open val logger = LoggerFactory.getLogger(AbstractController::class.java)
	protected val engine: ThymeleafEngine = ThymeleafEngine()

	var session: Session? = null
	open lateinit var path: String
	val controllerHome: String = path + "/"
	val model: MutableMap<String, Any> = hashMapOf<String, Any>()

	// Service dependency injection
	open var injectServices = Kodein {
		bind<UserService>("inmemory") with provider { InMemoryUserService() }
		bind<UserService>("db") with provider { UserDBService() }
	}

	init {
		this.path = path
		// put before and after filters here
		before {
			session = request.session(true)


			logger.info("BEFORE: model contains:- ")
			model.entries.forEach {
				logger.info("\t" + it)
			}

			val errorsFlash: Map<String, String>? = request.session().attribute("errors")
			var errorCount: Int? = request.session().attribute("errorCount")
			logger.info("BEFORE: errors is: " + errorsFlash)
			if (errorsFlash != null) {
				model.put("errorMap", errorsFlash)
				if (errorCount == null) {
					errorCount = 1
				} else {
					errorCount++
				}
				request.session().attribute("errorCount", errorCount)
				logger.error("BEFORE: errorCount = $errorCount")
			} else {
				model.remove("errorMap")
			}

		}

		after {
			val errorCount: Int? = request.session().attribute("errorCount")
			if (errorCount != null && errorCount > 1) {
				logger.info("AFTER: clearingErrors")
				request.session().attribute("errorCount", null)
				request.session().attribute("errors", null)
				model.remove("errorMap")
			}
		}

		notFound { "404 not found?" }

	}


}