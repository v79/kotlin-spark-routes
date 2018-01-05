package uk.co.liamjdavison.kotlinsparkroutes.controllers

import spark.ModelAndView
import spark.Spark
import spark.kotlin.get
import spark.kotlin.post
import uk.co.liamjdavison.kotlinsparkroutes.annotations.SparkController

@SparkController
class ValidatorController : AbstractController("/validtest") {

	init {
		Spark.path(path) {
			get("/") {
				model.put("title", "Validation Tests")
				engine.render(ModelAndView(model, "validtest"))
			}

			get("/success") {
				logger.info("in /validtest/success")
				engine.render(ModelAndView(model, "valid-success"))
			}

			Spark.path("/ajax/") {
				post("validate") {
					val hours: String? = request.queryParams("hours")
					val minutes: String? = request.queryParams("minutes")
					val errorMap = validateHoursAndMinutes(hours, minutes)
					if (errorMap.isEmpty()) {
						logger.info("No errors found")
						response.status(200)
						response.body("")
						submit(hours, minutes)
//						redirect(path + "/success")
					} else {
						logger.info("Errors found: " + errorMap)
						model.put("errors", errorMap)
						model.put("hours", hours!!)
						model.put("minutes", minutes!!)
						engine.render(ModelAndView(model, "fragments/valid-time-form"))
					}
				}

				post("submit") {
					val hours: String? = request.queryParams("hours")
					val minutes: String? = request.queryParams("minutes")
					logger.info("Submitting hours=$hours and minutes=$minutes")
					model.clear()
					redirect(path + "/success")
//					engine.render(ModelAndView(model,"valid-success"))
				}
			}
		}
	}

	fun submit(h: String?, m: String?) {
//		val hours: String? = request.queryParams("hours")
//		val minutes: String? = request.queryParams("minutes")
		logger.info("Submitting hours=$h and minutes=$m")
		model.clear()

	}

	fun validateHoursAndMinutes(h: String?, m: String?): Map<String, String> {
		val errors: MutableMap<String, String> = mutableMapOf()

		if (h.isNullOrEmpty()) {
			errors.put("hours", "Must not be empty")
		} else {
			try {
				val toInt: Int? = h?.toInt()
				toInt?.let {
					if (it < 0 || it > 23) {
						errors.put("hours", "Hours must be in range 0 to 23")
					}
				}
			} catch (nfe: NumberFormatException) {
				errors.put("hours", "That's not even a number")
			}
		}

		if (m.isNullOrEmpty()) {
			errors.put("minutes", "Must not be empty")
		} else {
			try {
				val toInt: Int? = m?.toInt()
				toInt?.let {
					if (it < 0 || it > 59) {
						errors.put("minutes", "Minutes must be in range 0 to 59")
					}
				}
			} catch (nfe: NumberFormatException) {
				errors.put("hours", "That's not even a number")
			}
		}

		return errors
	}
}