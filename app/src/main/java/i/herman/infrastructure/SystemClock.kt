package i.herman.infrastructure

class SystemClock : Clock {

    override fun now(): Long {
        return System.currentTimeMillis()
    }
}