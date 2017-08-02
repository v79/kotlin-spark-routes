package uk.co.liamjdavison.kotlinsparkroutes.controllers

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.provider
import org.slf4j.LoggerFactory
import spark.Request
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

	private val FLASH_COUNT_MAX = 3


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


			// update all flash item counts
			val flashAttr: MutableMap<String, Any>? = request.session().attribute("flash")
			if (flashAttr != null && flashAttr.isNotEmpty()) {
				flashAttr.forEach {
					//					logger.info("Flash key: " + it.key + " -> " + it.value)
					model.put(it.key, it.value) // store the flashed thing on the model
//					logger.info("Stored " + it.key + " on model -> " + model.get(it.key))
					val flashKeyCount: Int? = request.session().attribute(getFlashKeyCountName(it.key))
					if (flashKeyCount == null) {
						request.session().attribute(getFlashKeyCountName(it.key), 1)
					} else {
						logger.info("Updated count $flashKeyCount to " + (flashKeyCount + 1))
						request.session().attribute(getFlashKeyCountName(it.key), (flashKeyCount + 1))
					}
				}
			}
		}

		after {
			val flashAttr: MutableMap<String, Any>? = request.session().attribute("flash")
			if (flashAttr != null && flashAttr.isNotEmpty()) {
				flashAttr.forEach {
					val flashKeyCount: Int? = request.session().attribute(getFlashKeyCountName(it.key))
					if (flashKeyCount != null && flashKeyCount > FLASH_COUNT_MAX) {
						logger.info("AFTER: clearing flash for " + it.key)
						clearFlashForKey(request, it.key)
						model.remove(it.key)
					}
				}
			}
		}

		notFound { "404 not found?" }

	}

	fun flash(request: Request, key: String, value: Any) {
		//request.session().attribute("errors", errorMap)
		val keyValueMap: MutableMap<String, Any> = mutableMapOf<String, Any>()
		keyValueMap.put(key, value)
		request.session().attribute("flash", keyValueMap)
		request.session().attribute(getFlashKeyCountName(key), 1)
	}

	fun clearFlashForKey(request: Request, key: String) {
		val flashAttr: MutableMap<String, Any> = request.session().attribute("flash")
		if (flashAttr.containsKey(key)) {
			flashAttr.remove(key)
		}
		request.session().attribute(getFlashKeyCountName(key), null)
	}

	fun emptyFlash(request: Request) {
		request.session().attribute("flash", null)
	}

	fun getFlashKeyCountName(key: String): String {
		return "flash" + key + "Count"
	}

}