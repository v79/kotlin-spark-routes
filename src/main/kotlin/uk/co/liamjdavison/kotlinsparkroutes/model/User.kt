package uk.co.liamjdavison.kotlinsparkroutes.model

/**
 * Created by Liam Davison on 08/07/2017.
 */

data class User(val name: String, val age: Int? ){
	constructor(name: String) : this(name, null)

	fun prettyPrint(): String {
		if(age!= null) {
			return name + " " + age
		} else {
			return name + " (ageless)"
		}
	}
}
