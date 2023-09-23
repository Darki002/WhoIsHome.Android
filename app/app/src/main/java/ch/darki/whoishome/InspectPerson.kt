package ch.darki.whoishome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import ch.darki.whoishome.core.Person
import ch.darki.whoishome.core.PresenceService

class InspectPerson : Fragment() {

    var person : Person? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.inspect_person, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: InspectPersonArgs = InspectPersonArgs.fromBundle(requireArguments())
        person = PresenceService.instance?.personService?.getPersonByEmail(args.email)

        setTitle(view, person?.displayName)
    }

    private fun setTitle(view : View, displayName : String?){
        view.findViewById<TextView>(R.id.person_inspect_name).text = displayName
    }

}