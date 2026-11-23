package com.isetr.tp5app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entité Room : une instance = une ligne de la table "etudiant_table".
 */
@Entity(tableName = "etudiant_table")
data class Etudiant(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val mail: String,
    val classe: String
)
