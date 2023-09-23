package i.herman.app

import kotlinx.coroutines.Dispatchers

class TestDispatchers : CoroutineDispatchers {

    override val background = Dispatchers.Unconfined
}