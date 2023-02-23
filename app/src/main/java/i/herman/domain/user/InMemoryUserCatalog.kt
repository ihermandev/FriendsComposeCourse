package i.herman.domain.user

import i.herman.domain.exceptions.DuplicateAccountException

class InMemoryUserCatalog(
    private val usersForPassword: MutableMap<String, MutableList<User>> = mutableMapOf(),
) {

    fun createUser(
        email: String,
        about: String,
        password: String,
    ): User {
        checkAccountExist(email)
        val userId = createUserIdFor(email)
        val user = User(userId, email, about)
        saveUser(password, user)
        return user
    }

    private fun saveUser(password: String, user: User) {
        usersForPassword.getOrPut(password) { mutableListOf() }.add(user)
    }

    private fun createUserIdFor(email: String): String {
        return email.takeWhile { it != '@' } + "Id"
    }

    private fun checkAccountExist(email: String) {
        if (usersForPassword.values.flatten().any { it.email == email }
        ) {
            throw DuplicateAccountException()
        }
    }
}