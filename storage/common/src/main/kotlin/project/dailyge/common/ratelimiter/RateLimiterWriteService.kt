package project.dailyge.common.ratelimiter

interface RateLimiterWriteService {
    fun save(userId: Long, time: Long)

    fun deleteById(userId: Long)
}
