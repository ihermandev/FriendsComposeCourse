package i.herman.app

import kotlinx.coroutines.Dispatchers

class DefaultDispatchers : CoroutineDispatchers {

    override val background = Dispatchers.IO
}