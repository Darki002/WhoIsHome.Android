package ch.darki.whoishome.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ch.darki.whoishome.R

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings_container, Settings())
            .commit()
    }
}