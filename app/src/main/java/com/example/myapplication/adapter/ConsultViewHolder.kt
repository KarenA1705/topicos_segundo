package com.example.myapplication.adapter

import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.Character
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class ConsultViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val context = view.context
    private val tvItemName = view.findViewById<TextView>(R.id.textViewName);
    private val tvItemDescription: TextView = view.findViewById(R.id.textViewDescription)
    private val chipItemTypeCharacter = view.findViewById<ChipGroup>(R.id.chipTypeCharacter)
    private val ivItemCharacterImage: ImageView = view.findViewById(R.id.imageViewCharacter)

    fun render(character: Character) {
        tvItemName.text = character.nameCharacter
        tvItemDescription.text = character.description

        if (character.mainCharacter == 1)
            getCharacterType("Principal")

        if (character.secondaryCharacter == 1)
            getCharacterType("Secundario")

        if (character.extraCharacter == 1)
            getCharacterType("Extra")

        ivItemCharacterImage.setImageURI(Uri.parse(character.image))
    }

    private fun getCharacterType(valueType: String) {
        val chip = Chip(chipItemTypeCharacter.context)
        chip.text = valueType
        chipItemTypeCharacter.addView(chip)
    }
}