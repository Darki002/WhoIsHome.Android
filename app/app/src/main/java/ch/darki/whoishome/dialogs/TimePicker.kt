package ch.darki.whoishome.dialogs

import android.app.TimePickerDialog
import android.content.Context
import android.widget.TimePicker
import org.joda.time.DateTime

class TimePicker(private val context : Context, private val callback : (dateTime: DateTime) -> Unit) : TimePickerDialog.OnTimeSetListener {
    fun show() {
        TimePickerDialog(context, this, 18, 0, true).show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val time = DateTime(0, 0, 0,hourOfDay, minute)
        callback.invoke(time)
    }
}