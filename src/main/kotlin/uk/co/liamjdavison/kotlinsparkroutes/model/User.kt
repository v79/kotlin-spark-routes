package uk.co.liamjdavison.kotlinsparkroutes.model

/**
 * Created by Liam Davison on 08/07/2017.
 */

data class User(val id: Int, val name: String, val age: Int?) {
//	constructor(name: String) : this(name, null)

	constructor (name: String, age: Int) : this(-1, name, age)

	fun prettyPrint(): String {
		if(age!= null) {
			return name + " " + age
		} else {
			return name + " (ageless)"
		}
	}
}
