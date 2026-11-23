package com.isetr.tp5app.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isetr.tp5app.data.entity.Etudiant
import com.isetr.tp5app.data.repository.EtudiantRepository
import kotlinx.coroutines.launch

/**
 * ViewModel partagé entre les fragments : il expose les données observées et
 * exécute les opérations Room dans viewModelScope.
 */
class EtudiantViewModel(private val repository: EtudiantRepository) : ViewModel() {

    /** Tous les étudiants, observés directement depuis la base (LiveData Room). */
    val etudiants: LiveData<List<Etudiant>> = repository.allEtudiants

    /** Résultats de la recherche par classe. */
    private val _etudiantsSearchResults = MutableLiveData<List<Etudiant>>(emptyList())
    val etudiantsSearchResults: LiveData<List<Etudiant>> = _etudiantsSearchResults

    /** Requête courante, partagée avec la SearchView. */
    private val _searchQuery = MutableLiveData("")
    val searchQuery: LiveData<String> = _searchQuery

    /** Étudiant sélectionné pour l'écran de détails. */
    private val _selectedEtudiant = MutableLiveData<Etudiant?>(null)
    val selectedEtudiant: LiveData<Etudiant?> = _selectedEtudiant

    /** Conservé pour l'écran d'accueil du TP5. */
    private val _text = MutableLiveData("This is home Fragment")
    val text: LiveData<String> = _text

    fun addEtudiant(mail: String, classe: String) {
        viewModelScope.launch {
            repository.insert(Etudiant(mail = mail.trim(), classe = classe.trim()))
            refreshSearchResults()
        }
    }

    fun updateEtudiant(etudiant: Etudiant) {
        viewModelScope.launch {
            repository.update(etudiant)
            if (_selectedEtudiant.value?.id == etudiant.id) {
                _selectedEtudiant.value = etudiant
            }
            refreshSearchResults()
        }
    }

    fun deleteEtudiant(etudiant: Etudiant) {
        viewModelScope.launch {
            repository.delete(etudiant)
            if (_selectedEtudiant.value?.id == etudiant.id) {
                _selectedEtudiant.value = null
            }
            refreshSearchResults()
        }
    }

    fun deleteAllEtudiants() {
        viewModelScope.launch {
            repository.deleteAll()
            _selectedEtudiant.value = null
            refreshSearchResults()
        }
    }

    fun selectEtudiant(etudiant: Etudiant) {
        _selectedEtudiant.value = etudiant
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        viewModelScope.launch {
            if (query.isBlank()) {
                _etudiantsSearchResults.postValue(emptyList())
            } else {
                _etudiantsSearchResults.postValue(repository.searchEtudiantsByClasse(query.trim()))
            }
        }
    }

    fun getEtudiantById(id: Int): LiveData<Etudiant?> = repository.getEtudiantById(id)

    /** Garde les résultats filtrés synchronisés après une écriture. */
    private suspend fun refreshSearchResults() {
        val query = _searchQuery.value.orEmpty()
        if (query.isNotBlank()) {
            _etudiantsSearchResults.postValue(repository.searchEtudiantsByClasse(query.trim()))
        }
    }
}
