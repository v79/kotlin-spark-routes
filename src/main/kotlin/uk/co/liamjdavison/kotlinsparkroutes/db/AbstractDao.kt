package uk.co.liamjdavison.kotlinsparkroutes.db

import org.slf4j.LoggerFactory

/**
 * Created by Liam Davison on 13/07/2017.
 */
abstract class AbstractDao {
	open val logger = LoggerFactory.getLogger(AbstractDao::class.java)
}