package uk.co.liamjdavison.kotlinsparkroutes.controllers

import spark.kotlin.get
import uk.co.liamjdavison.kotlinsparkroutes.annotations.SparkController

/**
 * Created by Liam Davison on 01/07/2017.
 */
@SparkController
class BookController : AbstractController("/books") {

	init {

		get("/books/") {
			"here be books"
		}

		get( "/books/new") {
			"create new book"
		}
	}
}