package com.isetr.tp5app.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.isetr.tp5app.data.repository.EtudiantRepository

/**
 * Fabrique qui injecte le Repository dans le ViewModel partagé.
 */
class EtudiantViewModelFactory(private val repository: EtudiantRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(EtudiantViewModel::class.java)) {
            return EtudiantViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
