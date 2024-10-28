package pl.org.qwerty

import io.kotest.matchers.collections.shouldHaveSize

/**
 * Check if none of the tasks returned any Exception during processing
 */
fun <T> ParallelExecutionResult<T>.shouldHaveAllSuccesses() {
    this.exceptions shouldHaveSize 0
}

/**
 * Checks if the number of successful executions matches the expected count.
 *
 * @param count The expected number of successes.
 */
infix fun <T> ParallelExecutionResult<T>.shouldHaveSuccesses(count: Int) {
    this.successes shouldHaveSize count
}

/**
 * Asserts that the number of exceptions (failures) in the ParallelExecutionResult matches the expected count.
 *
 * @param count The expected number of failures.
 */
infix fun <T> ParallelExecutionResult<T>.shouldHaveFailures(count: Int) {
    this.exceptions shouldHaveSize count
}
