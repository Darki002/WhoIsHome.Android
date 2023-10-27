package ch.darki.whoishome.dialogs

import android.app.TimePickerDialog
import android.content.Context
import android.widget.TimePicker
import org.joda.time.DateTime

class TimePicker(private val context : Context) : TimePickerDialog.OnTimeSetListener {

    private lateinit var time : DateTime

    fun show() : DateTime {
        TimePickerDialog(context, this, 18, 0, true).show()
        return time
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        time = DateTime(0, 0, 0,hourOfDay, minute)
    }
}