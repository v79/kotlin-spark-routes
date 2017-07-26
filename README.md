# kotlin-spark-routes
Playground for combining kotlin-spark, Thymleaf, custom annotations etc

This is where I play around with Kotlin, kotlin-sparkjava, and other technologies. I'm hoping that this repository will at least compile, if not actually do anything useful.

A running version of the app _may_ be found [hosted on Heroku](https://kotlin-spark-routes.herokuapp.com/users/).

For commentary and thoughts on my Kotlin experiments, [see my blog](http://www.liamjdavison.co.uk/2017/07/kotlin-web-development-experiments/).

## Goals and technologies:

* HTTP server through [spark-kotlin](https://github.com/perwendel/spark-kotlin)
* Page layouts and templating with [Thymeleaf](http://www.thymeleaf.org/); others supported by spark-kotlin may follow later
* The Dependency Injection framework [Kodein](https://github.com/SalomonBrys/Kodein) - bit of learning curve
* Database handling and ORM with JetBrain's own [Exposed](https://github.com/JetBrains/Exposed), which is odd but interesting
* Reflection and annotation processing
* Unit testing with JUnit and [nhaarman's Kotlin Mockito](https://github.com/nhaarman/mockito-kotlin)
* Making it pretty through [Materializecss](https://github.com/Dogfalo/materialize)

I'm avoiding all the traditional big hitters for Java - Spring Boot, Hibernate, JSF2, JSP - as I want to learn more about alternative approaches. I'd like to use as much Kotlin as possible, but I'm not dogmatic about it. Java libraries are allowed.
