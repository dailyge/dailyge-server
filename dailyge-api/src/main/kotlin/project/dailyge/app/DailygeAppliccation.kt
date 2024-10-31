package project.dailyge.app

import org.slf4j.LoggerFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Import
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import project.dailyge.common.configuration.RedisConfig
import project.dailyge.configuration.JpaConfig
import project.dailyge.configuration.QueryDslConfig
import project.dailyge.document.configuration.MongoConfig
import project.dailyge.lock.configuration.RedissonConfig

@EnableAsync
@EnableWebMvc
@EnableScheduling
@SpringBootApplication(scanBasePackages = ["project.dailyge"])
@Import(JpaConfig::class, MongoConfig::class, RedisConfig::class, RedissonConfig::class, QueryDslConfig::class)
open class DailygeApplication

inline fun <reified T> T.logger() = LoggerFactory.getLogger(T::class.java)!!

fun main(args: Array<String>) {
    SpringApplication.run(DailygeApplication::class.java, *args)
}
