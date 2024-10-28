package pl.org.qwerty

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withTimeout
import kotlin.time.Duration

/**
 * Executes the given block in parallel for the specified number of repetitions.
 * Each execution can be subject to a timeout.
 *
 * Execution in parallel is not guaranteed, especially when there is:
 * * a high number of repetitions compared to available cores
 * * passed function uses blocking IO operations without dispatching them to IO dispatcher
 *
 * @param repetitions The number of times to repeat the block execution in parallel.
 * @param block The suspending block to be executed in parallel.
 * @param timeout The timeout duration for each block execution defaults to infinite.
 * @return [ParallelExecutionResult] containing the results of the successful executions and any exceptions encountered.
 */
suspend fun <T> executeInParallel(
    repetitions: Int,
    block: suspend () -> T,
    timeout: Duration = Duration.INFINITE,
): ParallelExecutionResult<T> =
    supervisorScope {
        val tasks = (1..repetitions).map {
            async(start = CoroutineStart.LAZY) {
                withTimeout(timeout) {
                    block()
                }
            }
        }

        tasks.forEach { it.start() }

        tasks.joinAll()

        if (!tasks.all { it.isCompleted }) {
            error("All tasks should be completed after join!")
        }
        val exceptions: MutableList<Throwable> = mutableListOf()
        val results = tasks.mapNotNull {
            try {
                it.await()
            } catch (e: Throwable) {
                exceptions.add(e)
                null
            }
        }

        ParallelExecutionResult(
            successes = results,
            exceptions = exceptions.toList(),
        )
    }
