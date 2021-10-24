package tolya.ash

class LRU<T, R>(private val size: Int, private val fn: (T) -> R) {
    private val cache = mutableMapOf<T, LLEntry<T, R>>()
    private val head: LLEntry<T, R>
    private val tail: LLEntry<T, R>

    init {
        check(size > 0) { "Size has to be positive" }
        head = LLEntry(null)
        tail = LLEntry(null)
        head.r = tail
        tail.le = head
    }

    operator fun get(arg: T): R {
        val toInsert = cache[arg]?.also { entry ->
            connect(entry.le, entry.r)
        } ?: run {
            LLEntry(arg to fn(arg)).also {
                cache[arg] = it
            }
        }
        connect(toInsert, head.r)
        connect(head, toInsert)
        if (cache.size > size) {
            val toDelete = requireNotNull(tail.le) { "Expected left neighbor of tail" }
            cache.remove(requireNotNull(toDelete.argResult) {
                "Unexpected empty linked-list entry. Probably, list is empty"
            }.first)
            connect(toDelete.le, tail)
        }
        return requireNotNull(toInsert.argResult){ "Invalid entry for arg" }.second
    }
}


class LLEntry<T, R>(val argResult: Pair<T, R>?) {
    var le: LLEntry<T, R>? = null
    var r: LLEntry<T, R>? = null
}

fun <T, R> connect(entryLe: LLEntry<T, R>?, entryR: LLEntry<T, R>?) {
    entryLe?.run { r = entryR }
    entryR?.run { le = entryLe }
}
