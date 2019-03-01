package searcher

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext


class Sequencer(val maxOperations: Int) {

    init {
        if (maxOperations <= 0)
            throw IllegalArgumentException("Sequencer maxOperations should be higher than 0.")
    }

    internal val operations = mutableListOf<CoroutineContext>()

    /**
     * When an operation is added, and the maxOperations count is reached, the oldest job in the queue will be canceled
     * and removed from the queue.
     */
    fun addOperation(operation: CoroutineContext) {
        operations.add(operation)
        if (operations.size > maxOperations) {
            operations.first().cancel()
            operations.removeAt(0)
        }
    }

    fun addOperation(operation: CoroutineScope) {
        addOperation(operation.coroutineContext)
    }

    /**
     * When an operation completes, we cancel and remove operations operations from the queue that are older.
     */
    fun operationCompleted(operation: CoroutineContext) {
        val index = operations.indexOf(operation)

        (0 until index).forEach {
            operations[it].cancel()
            operations.removeAt(it)
        }
    }

    fun operationCompleted(operation: CoroutineScope) {
        operationCompleted(operation.coroutineContext)
    }

    /**
     * We cancel and clear all operations from the queue.
     */
    fun cancelAll() {
        operations.forEach { it.cancel() }
        operations.clear()
    }
}