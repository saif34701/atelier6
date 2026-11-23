package com.isetr.tp5app.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.isetr.tp5app.EtudiantApplication
import com.isetr.tp5app.R
import com.isetr.tp5app.databinding.FragmentDetailEtudiantBinding
import com.isetr.tp5app.viewModel.EtudiantViewModel
import com.isetr.tp5app.viewModel.EtudiantViewModelFactory

/**
 * Exercice 1 : écran de détails de l'étudiant sélectionné dans la liste.
 * L'état est partagé via le même ViewModel d'activité.
 */
class DetailEtudiantFragment : Fragment() {

    private var _binding: FragmentDetailEtudiantBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EtudiantViewModel by activityViewModels {
        EtudiantViewModelFactory((requireActivity().application as EtudiantApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailEtudiantBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.selectedEtudiant.observe(viewLifecycleOwner) { etudiant ->
            if (etudiant == null) {
                binding.textDetailId.text = getString(R.string.id_format, "-")
                binding.textDetailMail.text = getString(R.string.mail_format, "-")
                binding.textDetailClasse.text = getString(R.string.classe_format, "-")
            } else {
                binding.textDetailId.text = getString(R.string.id_format, etudiant.id.toString())
                binding.textDetailMail.text = getString(R.string.mail_format, etudiant.mail)
                binding.textDetailClasse.text = getString(R.string.classe_format, etudiant.classe)
            }
        }

        binding.buttonDetailRetour.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
