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
package soup.movie.datetime

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.DayOfWeek.FRIDAY
import java.time.DayOfWeek.MONDAY
import java.time.DayOfWeek.SATURDAY
import java.time.DayOfWeek.SUNDAY
import java.time.DayOfWeek.THURSDAY
import java.time.DayOfWeek.TUESDAY
import java.time.DayOfWeek.WEDNESDAY

class DataHelperTest {

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
