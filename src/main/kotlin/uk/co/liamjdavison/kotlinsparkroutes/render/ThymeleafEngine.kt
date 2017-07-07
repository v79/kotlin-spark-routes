package uk.co.liamjdavison

/**
 * Created by Liam Davison on 06/06/2017.
 */


import org.thymeleaf.context.Context
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import org.thymeleaf.templateresolver.ITemplateResolver

import java.util.Locale

import spark.ModelAndView
import spark.TemplateEngine

/**
 * Defaults to the 'templates' directory under the resource path

 * @author David Vaillant https://github.com/dvaillant
 * @author Maarten Mulders https://github.com/mthmulders
 */
class ThymeleafEngine : TemplateEngine {

	private lateinit var templateEngine: org.thymeleaf.TemplateEngine

	/**
	 * Constructs a thymeleaf template engine with specified prefix and suffix

	 * @param prefix the prefix (template directory in resource path)
	 * *
	 * @param suffix the suffix (e.g. .html)
	 */
	@JvmOverloads constructor(prefix: String = DEFAULT_PREFIX, suffix: String = DEFAULT_SUFFIX) {
		val defaultTemplateResolver = createDefaultTemplateResolver(prefix, suffix)
		initialize(defaultTemplateResolver)
	}

	/**
	 * Constructs a thymeleaf template engine with a proprietary initialize

	 * @param templateResolver the template resolver.
	 */
	constructor(templateResolver: ITemplateResolver) {
		initialize(templateResolver)
	}

	/**
	 * Initializes and sets the template resolver
	 */
	private fun initialize(templateResolver: ITemplateResolver) {
		templateEngine = org.thymeleaf.TemplateEngine()
		templateEngine.setTemplateResolver(templateResolver)
	}

	override fun render(modelAndView: ModelAndView): String {
		return render(modelAndView, Locale.getDefault())
	}

	/**
	 * Process the specified template (usually the template name).
	 * Output will be written into a String that will be returned from calling this method,
	 * once template processing has finished.
	 * @param modelAndView model and view
	 * *
	 * @param locale A Locale object represents a specific geographical, political, or cultural region
	 * *
	 * @return processed template
	 */
	fun render(modelAndView: ModelAndView, locale: Locale): String {
		val model = modelAndView.model
		if (model is Map<*, *>) {
			val context = Context(locale)
			context.setVariables(model as Map<String, Any>)
			return templateEngine.process(modelAndView.viewName, context)
		} else {
			throw IllegalArgumentException("modelAndView.getModel() must return a java.util.Map; is " + model.javaClass)
		}
	}

	companion object {

		private val DEFAULT_PREFIX = "templates/"
		private val DEFAULT_SUFFIX = ".html"
		private val DEFAULT_CACHE_TTL_MS = 3600000L

		private fun createDefaultTemplateResolver(prefix: String?, suffix: String?): ITemplateResolver {
			val templateResolver = ClassLoaderTemplateResolver()
			templateResolver.templateMode = TemplateMode.HTML

			templateResolver.prefix = prefix ?: DEFAULT_PREFIX

			templateResolver.suffix = suffix ?: DEFAULT_SUFFIX

			templateResolver.cacheTTLMs = DEFAULT_CACHE_TTL_MS
			return templateResolver
		}
	}
}
/**
 * Constructs a default thymeleaf template engine.
 * Defaults prefix (template directory in resource path) to templates/ and suffix to .html
 */