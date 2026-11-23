package com.isetr.tp5app.ui.liste

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.isetr.tp5app.EtudiantApplication
import com.isetr.tp5app.R
import com.isetr.tp5app.data.entity.Etudiant
import com.isetr.tp5app.databinding.FragmentListeBinding
import com.isetr.tp5app.ui.adapter.EtudiantAdapter
import com.isetr.tp5app.viewModel.EtudiantViewModel
import com.isetr.tp5app.viewModel.EtudiantViewModelFactory

class ListeFragment : Fragment() {

    private var _binding: FragmentListeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EtudiantViewModel by activityViewModels {
        EtudiantViewModelFactory((requireActivity().application as EtudiantApplication).repository)
    }

    private lateinit var adapter: EtudiantAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = EtudiantAdapter(
            onItemClick = { etudiant ->
                viewModel.selectEtudiant(etudiant)
                findNavController().navigate(R.id.action_nav_liste_to_detailEtudiant)
            },
            onItemLongClick = { etudiant -> showDeleteConfirmation(etudiant) },
            onItemUpdate = { etudiant -> showEditDialog(etudiant) }
        )
        binding.recyclerView.adapter = adapter

        // Liste complète (LiveData Room) : affichée quand aucune recherche n'est active.
        viewModel.etudiants.observe(viewLifecycleOwner) { liste ->
            if (viewModel.searchQuery.value.isNullOrBlank()) renderList(liste)
        }

        // Retour à la liste complète quand la recherche est vidée.
        viewModel.searchQuery.observe(viewLifecycleOwner) { query ->
            if (query.isBlank()) viewModel.etudiants.value?.let { renderList(it) }
        }

        // Résultats de la recherche par classe (exercice 4).
        viewModel.etudiantsSearchResults.observe(viewLifecycleOwner) { liste ->
            if (!viewModel.searchQuery.value.isNullOrBlank()) renderList(liste)
        }

        setupSearchMenu()
    }

    private fun renderList(liste: List<Etudiant>) {
        adapter.submitList(liste)
        binding.textEmpty.visibility = if (liste.isEmpty()) View.VISIBLE else View.GONE
    }

    /** MenuProvider moderne : la SearchView vit dans l'AppBar via le cycle de vie de la vue. */
    private fun setupSearchMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main, menu)
                val searchView = menu.findItem(R.id.action_search).actionView as? SearchView
                searchView?.apply {
                    queryHint = getString(R.string.search_hint)
                    isSubmitButtonEnabled = false
                    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            viewModel.setSearchQuery(query.orEmpty())
                            return true
                        }

                        override fun onQueryTextChange(newText: String?): Boolean {
                            viewModel.setSearchQuery(newText.orEmpty())
                            return true
                        }
                    })
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean = false
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    /** Exercice 2 : clic long -> confirmation de suppression. */
    private fun showDeleteConfirmation(etudiant: Etudiant) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.delete_title)
            .setMessage(getString(R.string.delete_message, etudiant.mail))
            .setNegativeButton(R.string.cancel, null)
            .setPositiveButton(R.string.delete) { _, _ ->
                viewModel.deleteEtudiant(etudiant)
                Toast.makeText(
                    requireContext(),
                    getString(R.string.deleted_toast, etudiant.mail),
                    Toast.LENGTH_SHORT
                ).show()
            }
            .show()
    }

    /** Exercice 3 : bouton Modifier -> boîte de dialogue avec validation. */
    private fun showEditDialog(etudiant: Etudiant) {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_edit_details, null)
        val mailField = dialogView.findViewById<EditText>(R.id.edit_mail)
        val classeField = dialogView.findViewById<EditText>(R.id.edit_classe)
        mailField.setText(etudiant.mail)
        classeField.setText(etudiant.classe)

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.edit_title)
            .setView(dialogView)
            .setNegativeButton(R.string.cancel, null)
            .setPositiveButton(R.string.update, null)
            .create()

        // Le bouton positif reste ouvert tant que les champs sont invalides.
        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val mail = mailField.text.toString().trim()
                val classe = classeField.text.toString().trim()

                when {
                    mail.isEmpty() -> mailField.error = getString(R.string.error_mail)
                    classe.isEmpty() -> classeField.error = getString(R.string.error_classe)
                    else -> {
                        viewModel.updateEtudiant(etudiant.copy(mail = mail, classe = classe))
                        Toast.makeText(
                            requireContext(),
                            R.string.updated_toast,
                            Toast.LENGTH_SHORT
                        ).show()
                        dialog.dismiss()
                    }
                }
            }
        }
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerView.adapter = null
        _binding = null
    }
}
