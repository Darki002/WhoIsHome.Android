package ch.darki.whoishome.core

class PersonService {
    var persons : List<Person>? = null
        private set

    fun loadAllPersons(){
        persons = listOf(
            Person("Llyn", "Baumann", "llyn.baumann@gmx.ch"),
            Person("Ryanne", "Baumann", "ryanne@gmx.ch"),
            Person("Jennifer", "Baumann", "jennifer.baumann@bluewin.ch"),
            Person("Ruth", "Baumann", "ruth.baum@bluewin.ch")
        )
    }
}