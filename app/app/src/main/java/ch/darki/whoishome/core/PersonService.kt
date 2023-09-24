package ch.darki.whoishome.core

import java.util.ArrayList
import kotlin.jvm.optionals.getOrNull

class PersonService {
    var persons : ArrayList<Person>? = null
        private set

    fun loadAllPersons(){
        persons = arrayListOf(
            Person("Llyn", "llyn.baumann@gmx.ch"),
            Person("Ryanne", "ryanne@gmx.ch"),
            Person("Jennifer", "jennifer.baumann@bluewin.ch"),
            Person("Ruth", "ruth.baum@bluewin.ch")
        )
    }

    fun getPersonByEmail(email: String): Person? {
        return persons?.stream()?.filter {
            p -> p.email == email
        }?.findFirst()?.getOrNull()
    }

    fun createPersonIfNotExists(person: Person){
        val foundPerson = persons?.any {
            p -> p.email == person.email
        }

        if(!foundPerson!!){
            persons?.add(person)
        }
    }
}