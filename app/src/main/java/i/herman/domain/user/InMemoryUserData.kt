package i.herman.domain.user

class InMemoryUserData(
    private val loggedInUserId: String
) {

    fun loggedInUserId() = loggedInUserId
}