package tolya.ash

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LRUTest {
    @Test
    fun bigBigTest() {
        val lru = LRU<Int, Int>(100){ it }
        for (i in 0 until 1000000) {
            val v = Random.Default.nextInt(10000)
            assertEquals(v, lru[v])
        }
    }

    @Test
    fun smallBigTest() {
        val lru = LRU<Int, Int>(2){ it }
        for (i in 0 until 1000000) {
            val v = Random.Default.nextInt(10000)
            assertEquals(v, lru[v])
        }
    }

    @Test
    fun smallestBigTest() {
        val lru = LRU<Int, Int>(1){ it }
        for (i in 0 until 1000000) {
            val v = Random.Default.nextInt(10000)
            assertEquals(v, lru[v])
        }
    }

    @Test
    fun cacheTest() {
        val useTimes = MutableList(101){ 0 }
        val lru = LRU<Int, Int>(100){
            ++useTimes[it]
            it
        }
        for (i in 0 until 1000000) {
            val v = if (i % 100 == 0) 0 else i % 101
            assertEquals(v, lru[v])
        }
        assertEquals(1, useTimes[0])
        for (i in 1 until 101) {
            assertTrue(useTimes[i] > 1)
        }
    }
}
