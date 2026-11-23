package com.isetr.tp5app.data.repository

import androidx.lifecycle.LiveData
import com.isetr.tp5app.data.dao.EtudiantDao
import com.isetr.tp5app.data.entity.Etudiant

/**
 * Repository : pont entre le ViewModel et le DAO. Centralise toutes les
 * opérations CRUD et la recherche par classe.
 */
class EtudiantRepository(private val etudiantDao: EtudiantDao) {

    val allEtudiants: LiveData<List<Etudiant>> = etudiantDao.getAllEtudiants()

    suspend fun insert(etudiant: Etudiant) = etudiantDao.insert(etudiant)

    suspend fun delete(etudiant: Etudiant) = etudiantDao.delete(etudiant)

    suspend fun update(etudiant: Etudiant) = etudiantDao.update(etudiant)

    suspend fun deleteAll() = etudiantDao.deleteAll()

    suspend fun searchEtudiantsByClasse(classe: String): List<Etudiant> =
        etudiantDao.searchByClasse(classe)

    suspend fun getAllEtudiantsList(): List<Etudiant> = etudiantDao.getAllEtudiantsList()

    fun getEtudiantById(id: Int): LiveData<Etudiant?> = etudiantDao.getEtudiantById(id)
}
