package uk.co.liamjdavison.kotlinsparkroutes.services

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.provider
import org.slf4j.LoggerFactory
import uk.co.liamjdavison.kotlinsparkroutes.db.Dao
import uk.co.liamjdavison.kotlinsparkroutes.db.UserDao

/**
 * Created by Liam Davison on 08/07/2017.
 */
abstract class AbstractService {
	open val logger = LoggerFactory.getLogger(AbstractService::class.java)

	val injectDao = Kodein {
		bind<Dao>() with provider { UserDao() }
	}
}