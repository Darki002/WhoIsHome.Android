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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import ch.darki.whoishome.core.Event
import ch.darki.whoishome.core.Person


class InspectPerson : Fragment() {

    private lateinit var service: ServiceManager

    private lateinit var viewModel: InspectPersonViewModel

    private var todayEventsLayout : LinearLayout? = null
    private var thisWeekEventsLayout : LinearLayout? = null
    private var otherEventsLayout : LinearLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val fragment = inflater.inflate(R.layout.fragment_inspect_person, container, false)
        service = activity?.applicationContext as ServiceManager

        todayEventsLayout = fragment.findViewById(R.id.todayEvents)
        thisWeekEventsLayout = fragment.findViewById(R.id.thisWeekEvents)
        otherEventsLayout = fragment.findViewById(R.id.otherEvents)

        viewModel = activityViewModels<InspectPersonViewModel>().value

        return fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: InspectPersonArgs = InspectPersonArgs.fromBundle(requireArguments())

        if(viewModel.person == null || viewModel.person?.email != args.email){
            service.presenceService.personService.getPersonByEmail(args.email){
                viewModel.person = it
                setTitle(view, viewModel.person?.displayName)
                showAllEvents()
            }
        }
        else{
            setTitle(view, viewModel.person?.displayName)
            showAllEvents()
        }
    }

    override fun onResume() {
        super.onResume()
        showAllEvents()
    }

    private fun setTitle(view : View, displayName : String?){
        view.findViewById<TextView>(R.id.person_inspect_name).text = displayName
    }

    private fun showAllEvents(){
        if(viewModel.person == null){
            return
        }

        service.presenceService.eventService.getEventsForPersonByEmail(viewLifecycleOwner.lifecycleScope, viewModel.person!!.email){ eventsForPerson ->
            val todayEvents = eventsForPerson.today
            val thisWeekEvents = eventsForPerson.thisWeek
            val otherEvents = eventsForPerson.otherEvents

            showToday(todayEvents)
            showEventsAt(thisWeekEvents, thisWeekEventsLayout)
            showEventsAt(otherEvents, otherEventsLayout)
        }
    }

    private fun showToday(todayEvents : List<Event>){

        if(viewModel.person == null){
            return
        }

        todayEventsLayout?.removeAllViews()
        todayEvents.forEach (
            fun (e){
                val view = layoutInflater.inflate(R.layout.view_event, null)
                view.findViewById<TextView>(R.id.eventName).text = e.eventName
                view.findViewById<TextView>(R.id.date).text = "Heute"

                view.findViewById<Button>(R.id.edit_event).setOnClickListener {
                    if(viewModel.person?.email != service.currentPerson?.email){
                        Toast.makeText(context, "Unauthorized", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    val action = InspectPersonDirections.actionPersonViewToEdiEvent(e.id, viewModel.person!!.email)
                    NavHostFragment.findNavController(this).navigate(action)
                }

                view.findViewById<Button>(R.id.deleteEvent).setOnClickListener {

                    if(viewModel.person?.email != service.currentPerson?.email){
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

        if(viewModel.person == null){
            return
        }

        layout?.removeAllViews()
        events.forEach (
            fun (e) {
                val view = layoutInflater.inflate(R.layout.view_event, null)
                view.findViewById<TextView>(R.id.eventName).text = e.eventName
                view.findViewById<TextView>(R.id.date).text = e.toDateTimeString()

                view.findViewById<Button>(R.id.edit_event).setOnClickListener {
                    if(viewModel.person?.email != service.currentPerson?.email){
                        Toast.makeText(context, "Unauthorized", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    val action = InspectPersonDirections.actionPersonViewToEdiEvent(e.id, viewModel.person!!.email)
                    NavHostFragment.findNavController(this).navigate(action)
                }

                view.findViewById<Button>(R.id.deleteEvent).setOnClickListener {

                    if(viewModel.person?.email != service.currentPerson?.email){
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

class InspectPersonViewModel : ViewModel() {
    var person: Person? = null
}