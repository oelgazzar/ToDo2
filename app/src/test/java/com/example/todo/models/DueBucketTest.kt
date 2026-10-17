package com.example.todo.models

import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.LocalDate

class DueBucketTest {
    private val today = LocalDate.of(2026, 9, 27) // Sunday

    @Test
    fun `null date returns no due date`() {
        assertEquals(
            DueBucket.NO_DUE_DATE,
            null.toDueBucket(today)
        )
    }

    @Test
    fun `past date returns overdue`() {
        assertEquals(
            DueBucket.OVERDUE,
            today.minusDays(1).toDueBucket(today)
        )
    }

    @Test
    fun `today returns today`() {
        assertEquals(
            DueBucket.TODAY,
            today.toDueBucket(today)
        )
    }

    @Test
    fun `tomorrow returns tomorrow`() {
        assertEquals(
            DueBucket.TOMORROW,
            today.plusDays(1).toDueBucket(today)
        )
    }

    @Test
    fun `dates before next Saturday return this week`() {
        assertEquals(
            DueBucket.THIS_WEEK,
            LocalDate.of(2026, 10, 2).toDueBucket(today) // Friday
        )
    }

    @Test
    fun `next Saturday returns next week`() {
        assertEquals(
            DueBucket.NEXT_WEEK,
            LocalDate.of(2026, 10, 3).toDueBucket(today)
        )
    }

    @Test
    fun `date inside next week returns next week`() {
        assertEquals(
            DueBucket.NEXT_WEEK,
            LocalDate.of(2026, 10, 8).toDueBucket(today) // Thursday
        )
    }

    @Test
    fun `date in same month after the following week returns this month`() {
        val today = LocalDate.of(2026, 10, 1)
        assertEquals(
            DueBucket.THIS_MONTH,
            LocalDate.of(2026, 10, 16).toDueBucket(today)
        )
    }

    @Test
    fun `date in next month returns later`() {
        assertEquals(
            DueBucket.LATER,
            LocalDate.of(2026, 10, 15).toDueBucket(today)
        )
    }
}