package uk.co.liamjdavison.annotations;

import java.lang.annotation.*;

/**
 * Classes annotated with @SparkController will be initiated with the server is started, so that all their corresponding routes will exist
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) //on class level
@Documented
public @interface SparkController {
}
