package ch.darki.whoishome.dialogs

import android.content.Context
import android.widget.Toast
import ch.darki.whoishome.ServiceManager
import org.joda.time.DateTime

class CreateNewRepeatedEventDialog(private val context : Context, private val service: ServiceManager) {

    private val repeatedEventDetailDialog = RepeatedEventDetailDialog(context)
    private val dinnerDetailDialog = DinnerDetailDialog(context)

    fun show() {
        repeatedEventDetailDialog.show {
            if(it){
                dinnerDetailDialog.show { finish ->
                    if(finish){
                        createNewRepeatedEvent(
                            repeatedEventDetailDialog.name!!,
                            repeatedEventDetailDialog.startTime!!,
                            repeatedEventDetailDialog.endTime!!,
                            repeatedEventDetailDialog.firstDay!!,
                            repeatedEventDetailDialog.lastDay!!,
                            dinnerDetailDialog.relevantForDinner,
                            dinnerDetailDialog.dinnerAt,
                            dinnerDetailDialog.notAtHomeForDinner
                        ){ success ->
                            if(success){
                                Toast.makeText(context, "Event erstellt", Toast.LENGTH_SHORT).show()
                            }
                            else{
                                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    else{
                        show()
                    }
                }
            }
        }
    }

    private fun createNewRepeatedEvent(name: String,
                                       startTime: DateTime,
                                       endTime: DateTime,
                                       firstDay: DateTime,
                                       lastDay: DateTime,
                                       relevantForDinner: Boolean,
                                       dinnerAt: DateTime?,
                                       notAtHomeForDinner : Boolean,
                                       callback: (Boolean) -> Unit) {
        if (service.currentPerson != null) {

            if(notAtHomeForDinner){
                service.presenceService.repeatEventService.createRepeatedEvent(
                    service.currentPerson!!,
                    name,
                    startTime,
                    endTime,
                    firstDay,
                    lastDay,
                    true,
                    null
                ){
                    callback.invoke(it)
                }
                return
            }

            service.presenceService.repeatEventService.createRepeatedEvent(
                service.currentPerson!!,
                name,
                startTime,
                endTime,
                firstDay,
                lastDay,
                relevantForDinner,
                dinnerAt
            ){
                callback.invoke(it)
            }
        }
    }
}