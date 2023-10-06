package com.example.sampleproject

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun main() {
    val dateString = "August 2022"
    val lmsDate = LMsDate()
    println(lmsDate.getLastMonth())
    println(lmsDate.getLastMonth())
    println(lmsDate.getLastMonth())
    println(lmsDate.getLastMonth())
    println(lmsDate.getLastMonth())
    println(lmsDate.getLastMonth())
    println(lmsDate.getLastMonth())
    println(lmsDate.getLastMonth())
    println(lmsDate.getLastMonth())
    println(lmsDate.getLastMonth())
    println(lmsDate.getLastMonth())
    println(lmsDate.getLastMonth())
    println(lmsDate.getLastMonth())
    println(lmsDate.getLastMonth())
    println(lmsDate.getLastMonth())
    println(lmsDate.getLastMonth())
    println(lmsDate.getNextMonth())
    println(lmsDate.getNextMonth())
    println(lmsDate.getNextMonth())
    println(lmsDate.getNextMonth())
    println(lmsDate.getNextMonth())
    println(lmsDate.getNextMonth())
    println(lmsDate.getNextMonth())
    println(lmsDate.getNextMonth())
    println(lmsDate.getNextMonth())
    println(lmsDate.getNextMonth())
    println(lmsDate.getNextMonth())
    println(lmsDate.getNextMonth())
    println(lmsDate.getNextMonth())
    println(lmsDate.getNextMonth())
    println(lmsDate.getNextMonth())

}

class LMsDate {
    private var DATE_FORMAT_PATTERN = "yyyy-MM-dd"

    private val weekData = arrayListOf(Date())
    private var yCalendar = Calendar.getInstance()
    private var currentMonthOffset = 0 // Initialize offset to 0
    fun getParseToLastMonthOrWeek(strDate: String): Date {
        try {
            return SimpleDateFormat("MMMM yyyy", Locale.US).parse(strDate) ?: Date()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Date()
    }

    fun getParseToDate(strDate: String): Date {
        try {
            return SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(strDate) ?: Date()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Date()
    }

    private fun getDateDisplayDateFormat(
        mDateMonday: Date,
        mDateSunday: Date
    ): Triple<String, String, String> {

        val monday: String = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(mDateMonday)
        val sunday: String = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(mDateSunday)

        val dd: String = SimpleDateFormat("dd", Locale.US).format(mDateMonday)
        val ddMmmYy: String = SimpleDateFormat("dd MMM yy", Locale.US).format(mDateSunday)

        return Triple(monday, sunday, "$dd - $ddMmmYy")
    }

    private fun getDateFromStringDateObject(date: Date): String {
        val calFirst = Calendar.getInstance()
        calFirst.time = date
        val year = calFirst[Calendar.YEAR]
        val month =
            if ((calFirst[Calendar.MONTH] + 1).toString().length == 1) "0" + (calFirst[Calendar.MONTH] + 1).toString()
            else (calFirst[Calendar.MONTH] + 1).toString()

        val lcDate =
            if ((calFirst[Calendar.DATE]).toString().length == 1) "0" + (calFirst[Calendar.DATE]).toString()
            else (calFirst[Calendar.DATE]).toString()

        return "$year-$month-$lcDate"

    }

    private fun getDates(dateString1: String, dateString2: String): ArrayList<Date> {
        val dates: ArrayList<Date> = ArrayList()
        val df1: DateFormat = SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.US)

        var dateOne: Date? = null
        var dateTwo: Date? = null

        try {
            dateOne = df1.parse(dateString1)
            dateTwo = df1.parse(dateString2)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val cal1: Calendar = Calendar.getInstance()
        if (dateOne != null) {
            cal1.time = dateOne
        }
        val cal2: Calendar = Calendar.getInstance()
        if (dateTwo != null) {
            cal2.time = dateTwo
        }
        while (!cal1.after(cal2)) {
            dates.add(cal1.time)
            cal1.add(Calendar.DATE, 1)
        }
        getDateFromStringDateObject(dates.first())
        getDateFromStringDateObject(dates.last())

        return dates
    }

    fun getLastWeek(mCalendar: Calendar = yCalendar): Triple<String, String, String> {
        // Monday
        mCalendar.add(Calendar.DAY_OF_YEAR, -13)
        val mDateMonday: Date = mCalendar.time

        // Sunday
        mCalendar.add(Calendar.DAY_OF_YEAR, 6)
        val mDateSunday: Date = mCalendar.time


        val triple = getDateDisplayDateFormat(mDateMonday, mDateSunday)

        //return MONDAY + " - " + SUNDAY;

        yCalendar = mCalendar

        weekData.clear()
        weekData.addAll(getDates(triple.first, triple.second))

        return triple
    }

    //MONDAY - SUNDAY
    fun getCurrentWeek(mCalendar: Calendar = yCalendar): Triple<String, String, String> {
        val date = Date()
        mCalendar.time = date

        // 1 = Sunday, 2 = Monday, etc.
        val dayOfWeek: Int = mCalendar.get(Calendar.DAY_OF_WEEK)

        val mondayOffset: Int = if (dayOfWeek == 1) -6 else 2 - dayOfWeek // need to minus back

        mCalendar.add(Calendar.DAY_OF_YEAR, mondayOffset)
        val mDateMonday: Date = mCalendar.time

        // return 6 the next days of current day (object cal save current day)
        mCalendar.add(Calendar.DAY_OF_YEAR, 6)
        yCalendar = mCalendar

        val mDateSunday: Date = mCalendar.time

        val triple = getDateDisplayDateFormat(mDateMonday, mDateSunday)

        //return MONDAY + " - " + SUNDAY;
        weekData.clear()
        weekData.addAll(getDates(triple.first, triple.second))

        return triple
    }

    private fun getCurrentWeekTwo(mCalendar: Calendar = yCalendar): ArrayList<Date> {
        //SUNDAY - SATURDAY
        // Set the calendar to sunday of the current week
        mCalendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        val sdf = SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.US)
        val startDate: String = sdf.format(mCalendar.time)
        mCalendar.add(Calendar.DATE, 6)
        val endDate: String = sdf.format(mCalendar.time)
        println("Start Date = $startDate")
        println("End Date = $endDate")
        return getDates(startDate, endDate)
    }

    fun getNextWeek(mCalendar: Calendar = yCalendar): Triple<String, String, String> {
        // Monday
        mCalendar.add(Calendar.DAY_OF_YEAR, 1)
        val mDateMonday: Date = mCalendar.time

        // Sunday
        mCalendar.add(Calendar.DAY_OF_YEAR, 6)
        val weekSundayDate: Date = mCalendar.time

        yCalendar = mCalendar

        val triple = getDateDisplayDateFormat(mDateMonday, weekSundayDate)

        //return MONDAY + " - " + SUNDAY;
        weekData.clear()
        weekData.addAll(getDates(triple.first, triple.second))

        return triple
    }

    private fun getTodayDate(mCalendar: Calendar = yCalendar): Date {
        // Monday
        mCalendar.add(Calendar.DAY_OF_YEAR, 0)
        val mDateMonday: Date = mCalendar.time
        // Date format
        val simpleDateFormat = SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.US)
        val monday: String = simpleDateFormat.format(mDateMonday)

        val dateFormat: DateFormat = SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.US)
        var lcDateOne: Date? = null
        try {
            lcDateOne = dateFormat.parse(monday)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val calendar: Calendar = Calendar.getInstance()
        if (lcDateOne != null) {
            calendar.time = lcDateOne
        }
        return calendar.time
    }


    fun getCurrentMonth(calendar: Calendar = yCalendar): Triple<String, String, String> {
        // Set calendar to the first day of the current month
        val mCalendar = Calendar.getInstance()
        mCalendar.time = calendar.time

        currentMonthOffset = 0

        mCalendar.set(Calendar.DAY_OF_MONTH, 1)
        val mDateFirstDayOfMonth: Date = mCalendar.time


        // Set calendar to the last day of the current month
        mCalendar.set(Calendar.DAY_OF_MONTH, mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        val mDateLastDayOfMonth: Date = mCalendar.time

        val triple = getDateDisplayDateFormat(mDateFirstDayOfMonth, mDateLastDayOfMonth)

        weekData.clear()
        weekData.addAll(getDates(triple.first, triple.second))

        return triple
    }


    fun getLastMonth(calendar: Calendar = yCalendar): Triple<String, String, String> {
        // Create a copy of the calendar object
        val mCalendar = Calendar.getInstance()
        mCalendar.time = calendar.time

        currentMonthOffset--

        // Subtract one month from the copied calendar
        mCalendar.add(Calendar.MONTH, currentMonthOffset)

        // Set the copied calendar to the first day of the previous month
        mCalendar.set(Calendar.DAY_OF_MONTH, 1)
        val mDateFirstDayOfMonth: Date = mCalendar.time

        // Move the copied calendar to the last day of the next month
        mCalendar.set(Calendar.DAY_OF_MONTH, mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH))

        val mDateLastDayOfMonth: Date = mCalendar.time

        return getDateDisplayDateFormat(mDateFirstDayOfMonth, mDateLastDayOfMonth)
    }


    fun getNextMonth(calendar: Calendar = yCalendar): Triple<String, String, String> {
        // Create a copy of the calendar object
        val mCalendar = Calendar.getInstance()
        mCalendar.time = calendar.time

        currentMonthOffset++


        // Add one month to the copied calendar
        mCalendar.add(Calendar.MONTH, currentMonthOffset)

        // Set the copied calendar to the first day of the current month
        mCalendar.set(Calendar.DAY_OF_MONTH, mCalendar.getActualMinimum(Calendar.DAY_OF_MONTH))
        val mDateFirstDayOfMonth: Date = mCalendar.time

        // Move the copied calendar to the last day of the next month
        mCalendar.set(Calendar.DAY_OF_MONTH, mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        val mDateLastDayOfMonth: Date = mCalendar.time

        return getDateDisplayDateFormat(mDateFirstDayOfMonth, mDateLastDayOfMonth)
    }

}