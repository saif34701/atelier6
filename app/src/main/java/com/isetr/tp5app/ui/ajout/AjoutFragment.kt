package com.isetr.tp5app.ui.ajout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.isetr.tp5app.EtudiantApplication
import com.isetr.tp5app.R
import com.isetr.tp5app.databinding.FragmentAjoutBinding
import com.isetr.tp5app.viewModel.EtudiantViewModel
import com.isetr.tp5app.viewModel.EtudiantViewModelFactory

class AjoutFragment : Fragment() {

    private var _binding: FragmentAjoutBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EtudiantViewModel by activityViewModels {
        EtudiantViewModelFactory((requireActivity().application as EtudiantApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAjoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonAjouter.setOnClickListener {
            val mail = binding.editMailAjout.text.toString().trim()
            val classe = binding.editClasseAjout.text.toString().trim()

            when {
                mail.isEmpty() -> binding.editMailAjout.error = getString(R.string.error_mail)
                classe.isEmpty() -> binding.editClasseAjout.error = getString(R.string.error_classe)
                else -> {
                    viewModel.addEtudiant(mail, classe)
                    Toast.makeText(
                        requireContext(),
                        R.string.added_toast,
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.editMailAjout.text?.clear()
                    binding.editClasseAjout.text?.clear()

                    // popUpTo + popUpToInclusive définis dans mobile_navigation.xml
                    findNavController().navigate(
                        R.id.action_nav_ajout_etudiant_to_nav_liste_etudiants
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
