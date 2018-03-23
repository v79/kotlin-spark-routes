import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

typealias IsValid = Pair<Boolean, Map<String, String>>

interface Valid {
	fun valid(): IsValid
}

class FakeRequest(val params: Map<String, String>) {
	override fun toString(): String {
		val sBuilder: StringBuilder = StringBuilder()
		for (k in params) {
			sBuilder.append("[" + k.key + "->" + k.value + "] ")
		}
		return sBuilder.toString()
	}
}

data class MyTime(val hours: Int, val minutes: Int) : Valid {
	override fun valid(): IsValid {
		val errors = mutableMapOf<String, String>()
		var v = true
//		var v: IsValid = IsValid(true,errors)
		if (!(hours in 0..23)) {
			errors.put("hours", "hours must be in range 0 to 23")
			v = false
		}
		if (!(minutes in 0..59)) {
			errors.put("minutes", "minutes must be in range 0 to 59")
			v = false
		}
		return IsValid(v, errors)
	}
}

data class MyUser(val name: String, val email: String) : Valid {
	constructor(n: String) : this(n, n + "@" + n + ".com")

	override fun valid(): IsValid {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

}

class FormField(val key: String, val value: String?)

class Form(val req: FakeRequest, val formClass: KClass<*>) {
	val fields = mutableSetOf<FormField>()
	var valid: Boolean = true

	init {

		println("All about my class ${formClass.simpleName}")
		val isData = formClass.isData
		val numOfConstructors = formClass.constructors.size
		val primaryConstructor = formClass.primaryConstructor
		val declaredMemberProperties = formClass.declaredMemberProperties

		println("\t isData: ${isData}")
		println("\t num constructors: $numOfConstructors")
		for (p in declaredMemberProperties) {
			println("\t\t property: " + p.name + ", " + p.returnType)
		}
		println("\t primary constructor: $primaryConstructor")

		println("---")
		println("FakeRequest: $req")

		val consParams: MutableMap<KParameter, Any?> = mutableMapOf()
		if (primaryConstructor != null) {
			for (consP in primaryConstructor.parameters) {
				println("\t primaryConstructor parameter: ${consP} with type jvmErasure ${consP.type.jvmErasure}")

				when (consP.type.jvmErasure.qualifiedName) {
					"kotlin.String" -> {
						println("do string thing")
						consParams.put(consP, req.params.get(consP.name))
					}
					"kotlin.Int" -> {
						println("do int thing")
						consParams.put(consP, req.params.get(consP.name)?.toInt())
					}
				}

//

			}
			val constructedThingy = primaryConstructor.callBy(consParams)

			println("built a ${constructedThingy}")
		}


//		formClass.constructors.forEach {
//			println("\t${it.name}")
//		}


/*		formClass.declaredMemberProperties.forEach {
//			if(it is KProperty) {
				if(req.params.containsKey(it.name)) {
					println("\tfound field ${it.name}")
					fields.add(FormField(it.name, req.params[it.name]))
				}
//			}
		}
		val primaryConstructor = formClass.primaryConstructor
//		primaryConstructor.callBy()
		var constructionParams = primaryConstructor?.parameters
		println("\tconstructionParams: ${constructionParams}")
		var thingy = constructionParams?.let { formClass.primaryConstructor?.callBy(it) }


		val validFunction = formClass.declaredFunctions.forEach {
			if(it.name.equals("valid")) {
				it
			}
		}

		println("this: $this")
//		println("thingy: $thingy")
		println("validation function is ${validFunction}")*/
	}


	override fun toString(): String {
		val sb: StringBuilder = StringBuilder()
		for (f in fields) {
			sb.append(f.key).append(" -> ").append(f.value).append(", ")
		}
		sb.append(" (valid $valid)")
		return sb.toString()
	}

}


fun main(args: Array<String>) {

	val stringOnly = false

	if (!stringOnly) {
		val nameParam = "hours" to "24"
		val ageParam = "minutes" to "60"
		val paramMap = mutableMapOf<String, String>()
		paramMap.put(nameParam.first, nameParam.second)
		paramMap.put(ageParam.first, ageParam.second)
		println("Constructing myFakeRequest with ${paramMap}")
		val myFakeRequest = FakeRequest(params = paramMap)

		val myTimeForm = Form(myFakeRequest, MyTime::class)

		println("myTimeForm is valid: ${myTimeForm.valid}")
	}

	val userParamMap = mutableMapOf(Pair("name", "liam"), Pair("email", "liam@davison.com"))
	val fakeUserRequest = FakeRequest(params = userParamMap)
	val myUserForm = Form(fakeUserRequest, MyUser::class)

}



