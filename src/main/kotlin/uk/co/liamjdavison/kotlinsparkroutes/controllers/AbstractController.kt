package uk.co.liamjdavison.kotlinsparkroutes.controllers

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.provider
import org.slf4j.LoggerFactory
import spark.Session
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

	// Service dependency injection
	open var injectServices = Kodein {
		bind<UserService>("inmemory") with provider { InMemoryUserService() }
		bind<UserService>("db") with provider { UserDBService() }
	}

	open lateinit var path: String

	init {
		this.path = path
		// put before and after filters here
		before {
			session  = request.session(true)
		}

		notFound { "404 not found?" }
	}

}