package uk.co.liamjdavison.kotlinsparkroutes.services

import org.slf4j.LoggerFactory

/**
 * Created by Liam Davison on 08/07/2017.
 */
abstract class AbstractService {
	open val logger = LoggerFactory.getLogger(AbstractService::class.java)
}