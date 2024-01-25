package ch.darki.whoishome.dialogs

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import org.joda.time.DateTime
import java.util.Calendar

class DatePicker(private val context : Context, private val callback : (dateTime: DateTime) -> Unit) : DatePickerDialog.OnDateSetListener {

    private var defaultDay = 0
    private var defaultMonth = 0
    private var defaultYear = 0

    fun show(){
        getDateTimeCalender()
        DatePickerDialog(context, this, defaultYear, defaultMonth, defaultDay).show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        callback.invoke(DateTime(year, month + 1, dayOfMonth, 0, 0))
    }

    private fun getDateTimeCalender() {
        val cal = Calendar.getInstance()
        defaultDay = cal.get(Calendar.DAY_OF_MONTH)
        defaultMonth = cal.get(Calendar.MONTH)
        defaultYear = cal.get(Calendar.YEAR)
    }
}