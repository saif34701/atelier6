package com.isetr.tp5app.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.isetr.tp5app.data.dao.EtudiantDao
import com.isetr.tp5app.data.entity.Etudiant

/**
 * Base Room locale. Une seule instance est partagée par toute l'application.
 */
@Database(entities = [Etudiant::class], version = 1, exportSchema = false)
abstract class EtudiantDatabase : RoomDatabase() {

    abstract fun etudiantDao(): EtudiantDao

    companion object {
        @Volatile
        private var INSTANCE: EtudiantDatabase? = null

        fun getDatabase(context: Context): EtudiantDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    EtudiantDatabase::class.java,
                    "etudiant_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
