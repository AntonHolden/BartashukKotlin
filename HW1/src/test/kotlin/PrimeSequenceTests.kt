import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

class PrimeSequenceTests {
    @Test
    @DisplayName("OnePrimeNumberTest")
    fun onePrimeNumberTest() {
        val s = (2L..2L).asSequence()

        assertEquals(1, Prime.sequence(s).count())
    }

    @Test
    @DisplayName("OneCompositeNumberTest")
    fun oneCompositeNumberTest() {
        val s = (4L..4L).asSequence()

        assertEquals(0, Prime.sequence(s).count())
    }

    @Test
    @DisplayName("1To100Test")
    fun _1To100Test() {
        val s = (1L..100L).asSequence()

        assertEquals(25, Prime.sequence(s).count())
        assertTrue(Prime.sequence(s).contains(17))
        assertFalse(Prime.sequence(s).contains(1))
        assertFalse(Prime.sequence(s).contains(6))
    }

    @Test
    @DisplayName("NegativeNumbersTest")
    fun negativeNumbersTest() {
        val s = (-100L..-1L).asSequence()

        assertEquals(0, Prime.sequence(s).count())
    }

    @Test
    @DisplayName("0Test")
    fun _0Test() {
        val s = (0L..0L).asSequence()

        assertEquals(0, Prime.sequence(s).count())
    }

    @Test
    @DisplayName("BigPrimeNumberTest")
    fun bigPrimeNumberTest() {
        val bigPrimeNumber = 1046527L
        val s = generateSequence(0L) { it + 1 }

        assertTrue(Prime.sequence(s).contains(bigPrimeNumber))
    }

    @Test
    @DisplayName("BigCompositeNumberTest")
    fun bigCompositeNumberTest() {
        val bigCompositeNumber = 2550409L
        val s = generateSequence(0L) { if (it > bigCompositeNumber) null else it + 1 }

        assertFalse(Prime.sequence(s).contains(bigCompositeNumber))
    }

    @Test
    @DisplayName("CacheTest")
    fun cacheTest() {
        val s = (0L..1000000L).asSequence()

        val startTime1 = System.currentTimeMillis()
        Prime.sequence(s).toList() // terminal operation
        val totalTime1 = System.currentTimeMillis() - startTime1

        val startTime2 = System.currentTimeMillis()
        Prime.sequence(s).toList() // terminal operation
        val totalTime2 = System.currentTimeMillis() - startTime2

        assertTrue(totalTime2 < totalTime1)
    }
}