package uk.co.liamjdavison.kotlinsparkroutes.controllers

import org.slf4j.LoggerFactory
import spark.Session
import spark.kotlin.before
import spark.kotlin.notFound
import uk.co.liamjdavison.ThymeleafEngine

/**
 * Base class for all controllers. It defines a logger and the rendering engine, plus before and after filters,
 * notFound routes, and other common routes
 */
abstract class AbstractController(path: String) {
	open val logger = LoggerFactory.getLogger(AbstractController::class.java)
	protected val engine: ThymeleafEngine = ThymeleafEngine()

	var session: Session? = null

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