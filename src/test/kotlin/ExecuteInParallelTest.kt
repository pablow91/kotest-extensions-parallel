import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.should
import kotlinx.coroutines.delay
import pl.org.qwerty.executeInParallel
import pl.org.qwerty.shouldHaveAllSuccesses
import pl.org.qwerty.shouldHaveFailures
import pl.org.qwerty.shouldHaveSuccesses
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class ExecuteInParallelTest : FunSpec({

    test("should execute in parallel") {
        executeInParallel(
            repetitions = 10,
            block = { true }
        ).shouldHaveAllSuccesses()
    }

    test("should count successes and failures") {
        val gen = ExceptionGenerator(true, 5)
        executeInParallel(
            repetitions = 10,
            block = { gen.next().invoke() }
        ).should {
            it shouldHaveSuccesses 9
            it shouldHaveFailures 1
        }
    }

    test("should have count exception") {
        executeInParallel(
            repetitions = 10,
            block = { error("I got exception while executing") }
        ) shouldHaveFailures 10
    }

    test("should expire when timeout is set count exception").config(timeout = 2.seconds) {
        executeInParallel(
            repetitions = 10,
            block = { delay(1.minutes) },
            timeout = 1.seconds
        ) shouldHaveFailures 10
    }

})
