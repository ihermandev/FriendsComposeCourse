package i.herman.app

import kotlinx.coroutines.CoroutineDispatcher

interface CoroutineDispatchers {
    val background: CoroutineDispatcher
}