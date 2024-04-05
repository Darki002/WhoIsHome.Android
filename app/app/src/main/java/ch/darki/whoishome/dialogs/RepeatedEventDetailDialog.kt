package ch.darki.whoishome.dialogs

import android.content.Context
import org.joda.time.DateTime

class RepeatedEventDetailDialog(private val context : Context) {
    var name: String? = null
        private set

    var startTime: DateTime? = null
        private set

    var endTime: DateTime? = null
        private set

    fun show(callback: (Boolean) -> Unit){

    }
}