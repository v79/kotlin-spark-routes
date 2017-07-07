package uk.co.liamjdavison.kotlinsparkroutes.annotations

/**
 * Classes annotated with @SparkController will be initiated with the server is started, so that all their corresponding routes will exist
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class SparkController