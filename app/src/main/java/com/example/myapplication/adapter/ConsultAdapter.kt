package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.Character

class ConsultAdapter(private val charactersList: List<Character>) :
    RecyclerView.Adapter<ConsultViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsultViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ConsultViewHolder(layoutInflater.inflate(R.layout.layout_data, parent, false))
    }

    override fun getItemCount(): Int = charactersList.size

    override fun onBindViewHolder(holder: ConsultViewHolder, position: Int) {
        holder.render(charactersList[position])
    }
}