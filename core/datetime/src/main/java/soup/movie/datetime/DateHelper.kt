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

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

private val ZONE_SEOUL = ZoneId.of("Asia/Seoul")
fun currentTime(): LocalDateTime = LocalDateTime.now(ZONE_SEOUL)
fun today(): LocalDate = LocalDate.now(ZONE_SEOUL)
fun yesterday(): LocalDate = today().minusDays(1)

fun LocalDate.plusDaysTo(nextDayOfWeek: DayOfWeek): LocalDate {
    return plusDays(dayOfWeek.calculatePlusDaysTo(nextDayOfWeek))
}

fun DayOfWeek.calculateMinusDaysTo(dayOfWeekToPrevious: DayOfWeek): Long {
    var result = value - dayOfWeekToPrevious.value
    if (result < 0) {
        result += DayOfWeek.SUNDAY.value
    }
    return result.toLong()
}

fun DayOfWeek.calculatePlusDaysTo(dayOfWeekToNext: DayOfWeek): Long {
    var result = dayOfWeekToNext.value - value
    if (result < 0) {
        result += DayOfWeek.SUNDAY.value
    }
    return result.toLong()
}

fun LocalDate.MM_DD(): String {
    return format(DateTimeFormatter.ofPattern("MM.dd"))
}
fun LocalDate.YYYY_MM_DD(): String {
    return format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
}

fun calculateDDay(openDate: LocalDate?, today: LocalDate): Long? {
    return openDate?.let {
        ChronoUnit.DAYS.between(today, it)
    }
}

/**
 * Valid format: YYYY.MM.DD
 */
fun String.toLocalDate(): LocalDate? = split(".").let {
    return if (it.size == 3) {
        LocalDate.of(it[0].toInt(), it[1].toInt(), it[2].toInt())
    } else {
        null
    }
}
