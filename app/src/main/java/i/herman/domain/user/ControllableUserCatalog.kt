package i.herman.domain.user

import i.herman.domain.friends.ToggleFollowing

class ControllableUserCatalog(
    private val userCreate: suspend (String, String, String) -> User = { email, _, about ->
        User(email.takeWhile { it == '@' } + "Id", email, about)
    },
    private val followedByLoad: suspend () -> List<String> = { emptyList() },
    private val followToggle: suspend (String, String) -> ToggleFollowing = { userId, followingId ->
        ToggleFollowing(Following(userId, followingId), true)
    },
    private val friendsLoad: suspend () -> List<Friend> = { emptyList() }
) : UserCatalog {

    override suspend fun createUser(email: String, password: String, about: String): User {
        return userCreate(email, password, about)
    }

    override suspend fun toggleFollowing(userId: String, followerId: String): ToggleFollowing {
        return followToggle(userId, followerId)
    }

    override suspend fun followedBy(userId: String): List<String> {
        return followedByLoad()
    }

    override suspend fun loadFriendsFor(userId: String): List<Friend> {
        return friendsLoad()
    }
}