package uk.co.liamjdavison.kotlinsparkroutes.model

/**
 * Created by Liam Davison on 08/07/2017.
 */

data class User(val id: Int, val name: String, val age: Int) {
//	constructor(name: String) : this(name, null)

	constructor (name: String, age: Int) : this(-1, name, age)

	fun prettyPrint(): String {
		return name + " " + age
	}

	fun validate(): Map<String, String> {
		val errorMap = mutableMapOf<String, String>()

		if (age < 0) {
			errorMap.put("age", "Age must be greater than zero")
		}
		if (name.isNullOrEmpty() || name.isEmpty()) {
			errorMap.put("name", "Name must not be blank")
		}

		return errorMap
	}
}
