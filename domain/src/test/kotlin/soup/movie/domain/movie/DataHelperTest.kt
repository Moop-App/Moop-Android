/*
 * Copyright 2021 SOUP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package soup.movie.domain.movie

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.DayOfWeek.FRIDAY
import java.time.DayOfWeek.MONDAY
import java.time.DayOfWeek.SATURDAY
import java.time.DayOfWeek.SUNDAY
import java.time.DayOfWeek.THURSDAY
import java.time.DayOfWeek.TUESDAY
import java.time.DayOfWeek.WEDNESDAY
import java.time.LocalDate

class DataHelperTest {

    @Test
    fun isInWeekOfCultureDay_isCorrect() {
        // 5월
        assertEquals(false, date(2019, 5, 25).isInWeekOfCultureDay())
        assertEquals(true, date(2019, 5, 26).isInWeekOfCultureDay())
        assertEquals(true, date(2019, 5, 27).isInWeekOfCultureDay())
        assertEquals(true, date(2019, 5, 28).isInWeekOfCultureDay())
        assertEquals(true, date(2019, 5, 29).isInWeekOfCultureDay())
        assertEquals(false, date(2019, 5, 30).isInWeekOfCultureDay())

        // 6월
        assertEquals(false, date(2019, 6, 22).isInWeekOfCultureDay())
        assertEquals(true, date(2019, 6, 23).isInWeekOfCultureDay())
        assertEquals(true, date(2019, 6, 24).isInWeekOfCultureDay())
        assertEquals(true, date(2019, 6, 25).isInWeekOfCultureDay())
        assertEquals(true, date(2019, 6, 26).isInWeekOfCultureDay())
        assertEquals(false, date(2019, 6, 27).isInWeekOfCultureDay())

        // 7월
        assertEquals(false, date(2019, 7, 27).isInWeekOfCultureDay())
        assertEquals(true, date(2019, 7, 28).isInWeekOfCultureDay())
        assertEquals(true, date(2019, 7, 29).isInWeekOfCultureDay())
        assertEquals(true, date(2019, 7, 30).isInWeekOfCultureDay())
        assertEquals(true, date(2019, 7, 31).isInWeekOfCultureDay())
        assertEquals(false, date(2019, 8, 1).isInWeekOfCultureDay())

        // 8월
        assertEquals(false, date(2019, 8, 24).isInWeekOfCultureDay())
        assertEquals(true, date(2019, 8, 25).isInWeekOfCultureDay())
        assertEquals(true, date(2019, 8, 26).isInWeekOfCultureDay())
        assertEquals(true, date(2019, 8, 27).isInWeekOfCultureDay())
        assertEquals(true, date(2019, 8, 28).isInWeekOfCultureDay())
        assertEquals(false, date(2019, 8, 29).isInWeekOfCultureDay())

        // 9월
        assertEquals(false, date(2019, 9, 21).isInWeekOfCultureDay())
        assertEquals(true, date(2019, 9, 22).isInWeekOfCultureDay())
        assertEquals(true, date(2019, 9, 23).isInWeekOfCultureDay())
        assertEquals(true, date(2019, 9, 24).isInWeekOfCultureDay())
        assertEquals(true, date(2019, 9, 25).isInWeekOfCultureDay())
        assertEquals(false, date(2019, 9, 26).isInWeekOfCultureDay())

        // 10월
        assertEquals(false, date(2019, 10, 26).isInWeekOfCultureDay())
        assertEquals(true, date(2019, 10, 27).isInWeekOfCultureDay())
        assertEquals(true, date(2019, 10, 28).isInWeekOfCultureDay())
        assertEquals(true, date(2019, 10, 29).isInWeekOfCultureDay())
        assertEquals(true, date(2019, 10, 30).isInWeekOfCultureDay())
        assertEquals(false, date(2019, 10, 31).isInWeekOfCultureDay())
    }

    private fun date(year: Int, month: Int, dayOfMonth: Int): LocalDate {
        return LocalDate.of(year, month, dayOfMonth)
    }

    @Test
    fun calculateMinusDaysTo_isCorrect() {
        assertEquals(5, MONDAY.calculateMinusDaysTo(WEDNESDAY))
        assertEquals(6, TUESDAY.calculateMinusDaysTo(WEDNESDAY))
        assertEquals(0, WEDNESDAY.calculateMinusDaysTo(WEDNESDAY))
        assertEquals(1, THURSDAY.calculateMinusDaysTo(WEDNESDAY))
        assertEquals(2, FRIDAY.calculateMinusDaysTo(WEDNESDAY))
        assertEquals(3, SATURDAY.calculateMinusDaysTo(WEDNESDAY))
        assertEquals(4, SUNDAY.calculateMinusDaysTo(WEDNESDAY))
    }

    @Test
    fun calculatePlusDaysTo_success() {
        assertEquals(4, MONDAY.calculatePlusDaysTo(FRIDAY))
        assertEquals(3, TUESDAY.calculatePlusDaysTo(FRIDAY))
        assertEquals(2, WEDNESDAY.calculatePlusDaysTo(FRIDAY))
        assertEquals(1, THURSDAY.calculatePlusDaysTo(FRIDAY))
        assertEquals(0, FRIDAY.calculatePlusDaysTo(FRIDAY))
        assertEquals(6, SATURDAY.calculatePlusDaysTo(FRIDAY))
        assertEquals(5, SUNDAY.calculatePlusDaysTo(FRIDAY))
    }
}
