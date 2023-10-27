package ch.darki.whoishome.dialogs

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.DatePicker
import android.widget.TimePicker
import org.joda.time.DateTime
import java.util.Calendar

class DateTimePicker(private val context : Context, private val callback : (dateTime: DateTime) -> Unit) : DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private var day = 0
    private var month = 0
    private var year = 0

    private var defaultDay = 0
    private var defaultMonth = 0
    private var defaultYear = 0
    private val defaultHour = 12
    private val defaultMinute = 0

    fun show(){
        getDateTimeCalender()
        DatePickerDialog(context, this, defaultYear, defaultMonth, defaultDay).show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        this.day = dayOfMonth
        this.month = month + 1
        this.year = year
        TimePickerDialog(context, this, defaultHour, defaultMinute, true).show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val dateTime = DateTime(year, month, day, hourOfDay, minute)
        callback.invoke(dateTime)
    }

    private fun getDateTimeCalender() {
        val cal = Calendar.getInstance()
        defaultDay = cal.get(Calendar.DAY_OF_MONTH)
        defaultMonth = cal.get(Calendar.MONTH)
        defaultYear = cal.get(Calendar.YEAR)
    }
}