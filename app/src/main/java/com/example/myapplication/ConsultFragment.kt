package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.ConsultAdapter
import com.example.myapplication.database.DataBase
import com.example.myapplication.model.Character

class ConsultFragment : Fragment() {

    private var characterList = mutableListOf<Character>()
    private lateinit var recyclerConsult: RecyclerView
    private lateinit var adapter: ConsultAdapter

    private var dataBase: DataBase? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_consult, container, false)
        initComponent(view)
        initListener()

        return view
    }

    private fun initComponent(view: View) {
            recyclerConsult = view.findViewById(R.id.recyclerConsult)

            dataBase = DataBase(requireContext())
            adapter = ConsultAdapter(characterList)
            recyclerConsult.layoutManager = LinearLayoutManager(requireContext())
            recyclerConsult.adapter = adapter
    }

    private fun initListener() {
        dataBase?.let { db ->
            val listCharacter = db.selectAllCharacter()
            Log.d("ConsultFragment", "Número de personajes: ${listCharacter.size}")
            characterList.clear()
            characterList.addAll(listCharacter)
            adapter.notifyDataSetChanged()
        }
    }
}