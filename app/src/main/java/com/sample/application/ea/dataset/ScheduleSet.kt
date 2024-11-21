package com.sample.application.ea.dataset

import com.sample.application.ea.dataset.readonly.DateTimeSet
import com.sample.application.ea.extension.toCompactDate
import com.sample.application.ea.extension.toCompactDateTime
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Calendar
import java.util.Date

data class ScheduleSet(
    override var year: Int = 0,
    override var month: Int = 0,
    override var day: Int = 0,
    override var hour: Int = 0,
    override var minute: Int = 0,
    var title: String = "",
    var content: String = "",
) : DateTimeSet {

    companion object {

        @JvmStatic
        fun fromCalendar(calendar: Calendar): DateTimeSet {
            val dt = LocalDateTime.ofInstant(
                calendar.toInstant(),
                ZoneId.systemDefault()
            )
            return ScheduleSet(
                dt.year,
                dt.month.value,
                dt.dayOfMonth,
                dt.hour,
                dt.minute,
                "",
                ""
            )
        }

        @JvmStatic
        fun toCalendar(
            year: Int,
            month: Int,
            day: Int,
            hour: Int,
            minute: Int
        ): Calendar {
            val calendar = Calendar.getInstance()
            calendar.time = Date.from(
                LocalDateTime.of(
                    year,
                    month,
                    day,
                    hour,
                    minute
                )
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
            )
            return calendar
        }
    }

//    constructor() : this(0, 0, 0, 0, 0, "", "")

    fun calender() = toCalendar(year, month, day, hour, minute)

    fun key() = calender().toCompactDate()

    fun item() = calender().toCompactDateTime()

    override fun toString(): String {
        return String.format("Key: %s\nTitle: %s", key(), title)
    }

}
