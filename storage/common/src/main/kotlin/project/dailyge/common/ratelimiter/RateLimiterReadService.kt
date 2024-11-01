package project.dailyge.common.ratelimiter

interface RateLimiterReadService {
    fun findNoteHistoryByUserId(userId: Long): Boolean
}
