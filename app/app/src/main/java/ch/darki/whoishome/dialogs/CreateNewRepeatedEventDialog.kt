package ch.darki.whoishome.dialogs

import android.content.Context
import ch.darki.whoishome.ServiceManager

class CreateNewRepeatedEventDialog(private val context : Context, private val service: ServiceManager) {

    val repeatedEventDetailDialog = RepeatedEventDetailDialog(context)
    val dinnerDetailDialog = DinnerDetailDialog(context)

    fun show() {

    }
}