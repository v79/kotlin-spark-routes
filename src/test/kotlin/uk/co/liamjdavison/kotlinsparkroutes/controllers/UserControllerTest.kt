package uk.co.liamjdavison.kotlinsparkroutes.controllers

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.provider
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import org.junit.Assert
import org.junit.Test
import uk.co.liamjdavison.kotlinsparkroutes.model.User
import uk.co.liamjdavison.kotlinsparkroutes.services.users.UserService

class UserControllerTest {

	val controller = UserController()
	val mockUserService = mock<UserService> {
		on { getAllUsers() } doReturn emptyList<User>()
	}

	init {
		controller.injectServices = Kodein {
			bind<UserService>() with provider { mockUserService }
		}
	}

	@Test
	fun testSayHello_andGetEmptyList() {
		// setup
		controller.userService = controller.injectServices.instance()
		// execute
		val result = controller.sayHello()
		// verify
		Assert.assertEquals(result, "Hello 0")
	}

	@Test
	fun testMockUserServiceReturnsNoUsers() {
		// setup
		controller.userService = controller.injectServices.instance()
		// execute
		val result = controller.userService.getAllUsers()
		// verify
		Assert.assertTrue(result.isEmpty())
	}

}