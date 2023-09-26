package i.herman.domain.user

interface UserDataStore {
    fun loggedInUserId(): String
    fun storeLoggedInUserId(userId: String)
}