package uk.co.liamjdavison.kotlinsparkroutes.db.model

import io.requery.Entity
import io.requery.Generated
import io.requery.Key
import io.requery.Persistable

/**
 * Created by Liam Davison on 15/07/2017.
 */
@Entity
interface UserDB : Persistable {

	@get:Key
	@get:Generated
	var id: Int
	var name: String
	var age:Int

}