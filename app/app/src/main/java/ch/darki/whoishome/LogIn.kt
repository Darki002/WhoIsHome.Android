package ch.darki.whoishome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import ch.darki.whoishome.core.LogInService
import ch.darki.whoishome.core.ServiceManager

class LogIn : Fragment() {


    private lateinit var service: ServiceManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.log_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        service = activity?.applicationContext as ServiceManager

        view.findViewById<Button>(R.id.logInButton).setOnClickListener {

            val email = view.findViewById<TextView>(R.id.emailLogIn).text.toString()
            val displayName = view.findViewById<TextView>(R.id.displayNameLogIn).text.toString()
            service.logInService.register(email, displayName)
            goToHome()
            Toast.makeText(this.context, "Logged In", Toast.LENGTH_SHORT).show()
        }
    }

    private fun goToHome(){
        val action = LogInDirections.actionLoginViewToHome()
        NavHostFragment.findNavController(this).navigate(action)
    }
}