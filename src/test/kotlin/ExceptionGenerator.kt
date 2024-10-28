class ExceptionGenerator<T : Any>(
    private val element: T,
    vararg exceptionsAt: Int,
    private val exception: Throwable = IllegalStateException(),
) {

    private val exceptionsAtSet = exceptionsAt.toSet()
    private var index = 0

    fun next(): () -> T {
        val currentIndex = index
        index++
        return if (exceptionsAtSet.contains(currentIndex)) {
            { throw exception }
        } else {
            { element }
        }
    }

}