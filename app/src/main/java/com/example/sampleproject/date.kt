package com.example.sampleproject

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.abs


private fun getWeekStartDate(): Long {
    val calendar = Calendar.getInstance()
    while (calendar[Calendar.DAY_OF_WEEK] !== Calendar.MONDAY) {
        calendar.add(Calendar.DATE, -1)
    }
    return calendar.timeInMillis
}
private fun convertLongToDate(time: Long?): String {
    //Tuesday, 18 November 2022
    val date = time?.let { Date(it) }
    val format = SimpleDateFormat(
        "dd MMM", Locale.getDefault()
    )
    format.timeZone = TimeZone.getTimeZone("UTC")
    return format.format(date)
}
fun weeklyDate() {
    val cal = Calendar.getInstance()
    cal.add(Calendar.DATE, -7)
    cal.timeZone = TimeZone.getTimeZone("IST")
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    val date: Date = cal.time
    cal.time = date // compute start of the day for the timestamp


    val endCal = Calendar.getInstance()
    endCal.add(Calendar.DATE, 0)
    endCal.timeZone = TimeZone.getTimeZone("UTC")
    endCal.set(Calendar.HOUR_OF_DAY, 23);
    endCal.set(Calendar.MINUTE, 59);
    endCal.set(Calendar.SECOND, 59);
    endCal.set(Calendar.MILLISECOND, 0);
    val endDate: Date = endCal.time
    endCal.time = endDate

    val mStartDate = abs(cal.timeInMillis / 1000)
    val mEndDate = abs(endCal.timeInMillis / 1000)

    val mDate = convertLongToDate(getWeekStartDate()) + "-" + convertLongToDate(getWeekEndDate())

    println(mDate)
    println(mStartDate)
    println(mEndDate)
}

private fun getCurrentMonth(time: Long?): String {
//Tuesday, 18 November 2022
    val date = time?.let { Date(it) }
    val format = SimpleDateFormat(
        "MMMM", Locale.getDefault()
    )
    format.timeZone = TimeZone.getTimeZone("UTC")
    return format.format(date)
}
fun getWeekEndDate(): Long? {
    val calendar = Calendar.getInstance()
    if (calendar[Calendar.DAY_OF_WEEK] == Calendar.MONDAY) {
        calendar.add(Calendar.DATE, +6)
    } else if (calendar[Calendar.DAY_OF_WEEK] == Calendar.TUESDAY) {
        calendar.add(Calendar.DATE, +5)
    } else if (calendar[Calendar.DAY_OF_WEEK] == Calendar.WEDNESDAY) {
        calendar.add(Calendar.DATE, +4)
    } else if (calendar[Calendar.DAY_OF_WEEK] == Calendar.THURSDAY) {
        calendar.add(Calendar.DATE, +3)
    } else if (calendar[Calendar.DAY_OF_WEEK] == Calendar.FRIDAY) {
        calendar.add(Calendar.DATE, +2)
    } else if (calendar[Calendar.DAY_OF_WEEK] == Calendar.SATURDAY) {
        calendar.add(Calendar.DATE, +1)
    } else if (calendar[Calendar.DAY_OF_WEEK] == Calendar.SUNDAY) {
        calendar.add(Calendar.DATE, +7)
    }
    return calendar.timeInMillis
}


fun main(){
    println(weeklyDate())
}