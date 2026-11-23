package com.isetr.tp5app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.isetr.tp5app.R
import com.isetr.tp5app.data.entity.Etudiant
import com.isetr.tp5app.databinding.ItemEtudiantBinding

/**
 * Adapter RecyclerView avec trois callbacks :
 * - clic simple  -> détails
 * - clic long    -> suppression (confirmation)
 * - bouton Modifier -> mise à jour
 */
class EtudiantAdapter(
    private val onItemClick: (Etudiant) -> Unit,
    private val onItemLongClick: (Etudiant) -> Unit,
    private val onItemUpdate: (Etudiant) -> Unit
) : RecyclerView.Adapter<EtudiantAdapter.EtudiantViewHolder>() {

    private var etudiants: List<Etudiant> = emptyList()

    fun submitList(list: List<Etudiant>) {
        etudiants = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EtudiantViewHolder {
        val binding = ItemEtudiantBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EtudiantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EtudiantViewHolder, position: Int) {
        holder.bind(etudiants[position])
    }

    override fun getItemCount(): Int = etudiants.size

    inner class EtudiantViewHolder(private val binding: ItemEtudiantBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(etudiant: Etudiant) {
            binding.itemMail.text = etudiant.mail
            binding.itemClasse.text = binding.root.context.getString(
                R.string.classe_format,
                etudiant.classe
            )
            binding.root.setOnClickListener { onItemClick(etudiant) }
            binding.root.setOnLongClickListener {
                onItemLongClick(etudiant)
                true
            }
            binding.itemUpdate.setOnClickListener { onItemUpdate(etudiant) }
        }
    }
}
