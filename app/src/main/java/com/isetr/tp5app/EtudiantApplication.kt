package com.isetr.tp5app

import android.app.Application
import com.isetr.tp5app.data.database.EtudiantDatabase
import com.isetr.tp5app.data.repository.EtudiantRepository

/**
 * Point d'entrée de l'application et conteneur de dépendances singleton :
 * la base Room et le Repository ne sont créés qu'une seule fois.
 */
class EtudiantApplication : Application() {

    val database: EtudiantDatabase by lazy { EtudiantDatabase.getDatabase(this) }

    val repository: EtudiantRepository by lazy { EtudiantRepository(database.etudiantDao()) }
}
