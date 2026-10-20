package com.example.todo.ui.utils

import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.LocalDate

class DateUiTest {
    private val today = LocalDate.of(2026, 10, 20)

    private fun dateDaysAgo(days: Long) = today.minusDays(days)

    @Test
    fun `returns Today for current date`() {
        assertEquals("Today", today.toDisplayText(today))
    }

    @Test
    fun `returns Tomorrow for next day`() {
        assertEquals("Tomorrow", today.plusDays(1).toDisplayText(today))
    }

    @Test
    fun `returns Yesterday for previous day`() {
        assertEquals("Yesterday", today.minusDays(1).toDisplayText(today))
    }

    @Test
    fun `returns days ago for 2 to 6 days`() {
        assertEquals("2 days ago", dateDaysAgo(2).toDisplayText(today))
        assertEquals("3 days ago", dateDaysAgo(3).toDisplayText(today))
        assertEquals("6 days ago", dateDaysAgo(6).toDisplayText(today))
    }

    @Test
    fun `returns singular week for 7 to 13 days`() {
        assertEquals("1 week ago", dateDaysAgo(7).toDisplayText(today))
        assertEquals("1 week ago", dateDaysAgo(13).toDisplayText(today))
    }

    @Test
    fun `returns plural weeks for 14 to 29 days`() {
        assertEquals("2 weeks ago", dateDaysAgo(14).toDisplayText(today))
        assertEquals("4 weeks ago", dateDaysAgo(29).toDisplayText(today))
    }

    @Test
    fun `returns months for dates in the month range`() {
        assertEquals("1 month ago", dateDaysAgo(31).toDisplayText(today))
        assertEquals("2 months ago", dateDaysAgo(60).toDisplayText(today))
    }

    @Test
    fun `returns years for dates at least 365 days ago`() {
        assertEquals("1 year ago", dateDaysAgo(365).toDisplayText(today))
        assertEquals("2 years ago", dateDaysAgo(730).toDisplayText(today))
    }

    @Test
    fun `formats dates more than one year in the future`() {
        val futureDate = today.plusDays(400)

        assertEquals(
            futureDate.toFormattedString(),
            futureDate.toDisplayText(today)
        )
    }
}