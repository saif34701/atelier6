package com.isetr.tp5app.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.isetr.tp5app.data.entity.Etudiant

/**
 * DAO : unique point d'accès aux requêtes SQL de la table etudiant_table.
 * Les opérations d'écriture/lecture ponctuelles sont suspendues (coroutines),
 * la liste observable est exposée avec LiveData.
 */
@Dao
interface EtudiantDao {

    @Insert
    suspend fun insert(etudiant: Etudiant)

    @Delete
    suspend fun delete(etudiant: Etudiant)

    @Update
    suspend fun update(etudiant: Etudiant)

    @Query("SELECT * FROM etudiant_table ORDER BY id ASC")
    fun getAllEtudiants(): LiveData<List<Etudiant>>

    @Query("SELECT * FROM etudiant_table ORDER BY id ASC")
    suspend fun getAllEtudiantsList(): List<Etudiant>

    @Query("DELETE FROM etudiant_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM etudiant_table WHERE classe LIKE '%' || :classe || '%' ORDER BY id ASC")
    suspend fun searchByClasse(classe: String): List<Etudiant>

    @Query("SELECT * FROM etudiant_table WHERE id = :id LIMIT 1")
    fun getEtudiantById(id: Int): LiveData<Etudiant?>
}
