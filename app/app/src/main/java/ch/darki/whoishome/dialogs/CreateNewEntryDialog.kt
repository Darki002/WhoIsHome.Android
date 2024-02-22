package ch.darki.whoishome.dialogs

import android.content.Context
import android.widget.Toast
import ch.darki.whoishome.ServiceManager
import org.joda.time.DateTime

class CreateNewEntryDialog(private val context : Context, private val service: ServiceManager) {

    private val eventDetailDialog = EventDetailDialog(context)
    private val dinnerDetailDialog = DinnerDetailDialog(context)

    fun show() {
        eventDetailDialog.show {
            if(it){
                dinnerDetailDialog.show { finish ->
                    if(finish){
                        createNewEvent(
                            eventDetailDialog.name!!,
                            eventDetailDialog.date!!,
                            eventDetailDialog.startTime!!,
                            eventDetailDialog.endTime!!,
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

    private fun createNewEvent(name: String, date: DateTime, startTime: DateTime, endTime: DateTime,
                               relevantForDinner : Boolean, dinnerAt : DateTime?, notAtHomeForDinner : Boolean,
                               callback: (Boolean) -> Unit) {
        if (service.currentPerson != null) {

            if(notAtHomeForDinner){
                service.presenceService.eventService.createEvent(
                    service.currentPerson!!,
                    name,
                    date,
                    startTime,
                    endTime,
                    true,
                    null
                ){
                    callback.invoke(it)
                }
                return
            }

            service.presenceService.eventService.createEvent(
                service.currentPerson!!,
                name,
                date,
                startTime,
                endTime,
                relevantForDinner,
                dinnerAt
            ){
                callback.invoke(it)
            }
        }
    }
}