package uk.co.liamjdavison.kotlinsparkroutes.controllers

import spark.kotlin.get
import uk.co.liamjdavison.annotations.SparkController

/**
 * Created by Liam Davison on 01/07/2017.
 */
//@SparkController
class BookController : AbstractController() {

	init {
		get("/books/") {
			"here be books"
		}

		get("/books/new") {
			"create new book"
		}
	}
}