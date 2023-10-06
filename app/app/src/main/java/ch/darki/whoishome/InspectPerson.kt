package ch.darki.whoishome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import ch.darki.whoishome.core.Event
import ch.darki.whoishome.core.Person


class InspectPerson : Fragment() {

    private lateinit var service: ServiceManager

    private lateinit var person : Person
    private var todayEventsLayout : LinearLayout? = null
    private var thisWeekEventsLayout : LinearLayout? = null
    private var otherEventsLayout : LinearLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val fragment = inflater.inflate(R.layout.inspect_person, container, false)
        service = activity?.applicationContext as ServiceManager

        todayEventsLayout = fragment.findViewById(R.id.todayEvents)
        thisWeekEventsLayout = fragment.findViewById(R.id.thisWeekEvents)
        otherEventsLayout = fragment.findViewById(R.id.otherEvents)

        return fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: InspectPersonArgs = InspectPersonArgs.fromBundle(requireArguments())

        person = service.presenceService.personService.getPersonByEmail(args.email)!!
        setTitle(view, person.displayName)
    }

    override fun onResume() {
        super.onResume()
        showAllEvents()
    }

    private fun setTitle(view : View, displayName : String?){
        view.findViewById<TextView>(R.id.person_inspect_name).text = displayName
    }

    private fun showAllEvents(){
        val eventsForPerson = service.presenceService.eventService.getEventsForPersonByEmail(person.email)
        val todayEvents = eventsForPerson.today
        val thisWeekEvents = eventsForPerson.thisWeek
        val otherEvents = eventsForPerson.otherEvents

        showToday(todayEvents)
        showEventsAt(thisWeekEvents, thisWeekEventsLayout)
        showEventsAt(otherEvents, otherEventsLayout)
    }

    private fun showToday(todayEvents : List<Event>){

        todayEventsLayout?.removeAllViews()
        todayEvents.forEach (
            fun (e){
                val view = layoutInflater.inflate(R.layout.event_view, null)
                view.findViewById<TextView>(R.id.eventName).text = e.eventName
                view.findViewById<TextView>(R.id.date).text = "Heute"

                view.findViewById<Button>(R.id.deleteEvent).setOnClickListener {

                    if(person.email != service.logInService.currentPerson?.email){
                        Toast.makeText(context, "Unauthorized", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        service.presenceService.eventService.deleteEvent(e.id)
                        (view.parent as ViewManager).removeView(view)
                    }
                }

                todayEventsLayout?.addView(view)
            }
        )
    }

    private fun showEventsAt(events : List<Event>, layout : LinearLayout?){

        layout?.removeAllViews()
        events.forEach (
            fun (e) {
                val view = layoutInflater.inflate(R.layout.event_view, null)
                view.id = e.id
                view.findViewById<TextView>(R.id.eventName).text = e.eventName
                view.findViewById<TextView>(R.id.date).text = e.startDate.toString("dd.MM.yyyy HH:mm")

                view.findViewById<Button>(R.id.deleteEvent).setOnClickListener {


                    if(person.email != service.logInService.currentPerson?.email){
                        Toast.makeText(context, "Unauthorized", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        service.presenceService.eventService.deleteEvent(e.id)
                        (view.parent as ViewManager).removeView(view)
                    }
                }

                layout?.addView(view)
            })
    }

}