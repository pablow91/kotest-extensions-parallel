package pl.org.qwerty

@ConsistentCopyVisibility
data class ParallelExecutionResult<T> internal constructor(
    val successes: List<T>,
    val exceptions: List<Throwable>,
)
