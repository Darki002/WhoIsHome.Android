package ch.darki.whoishome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.fragment.NavHostFragment
import ch.darki.whoishome.core.LogInService
import ch.darki.whoishome.core.PresenceService
import org.joda.time.DateTime

class Home : Fragment() {

    private var personPresences : List<PresenceService.PersonPresence>? = null
    private var layout : LinearLayout? = null

    private val presentColor = android.R.drawable.presence_online
    private val absentColor = android.R.drawable.presence_busy

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if(LogInService.instance?.isLoggedIn() == false){
            val action = HomeDirections.actionHomeToLoginView()
            NavHostFragment.findNavController(this).navigate(action)
        }

        val fragment = inflater.inflate(R.layout.fragment_home, container, false)

        layout = fragment.findViewById(R.id.home_presence_container)

        personPresences = PresenceService.instance?.getPresenceListFrom(DateTime.now())
        personPresences!!.forEach { p ->
            showPerson(p)
        }

        return fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun showPerson(personPresence: PresenceService.PersonPresence){
        val view = layoutInflater.inflate(R.layout.person_presence, null)

        view.setOnClickListener {
            val action = HomeDirections.actionHomeToPersonView(personPresence.person.email)
            NavHostFragment.findNavController(this).navigate(action)
        }

        view.findViewById<TextView>(R.id.personName).text = personPresence.person.displayName

        val imageView = view.findViewById<ImageView>(R.id.isPresent)
        val drawable = if(personPresence.isPresent){ presentColor } else{ absentColor }
        imageView.setImageResource(drawable)

        layout?.addView(view)
    }
}