package com.example.myapplication

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.ConsultAdapter
import com.example.myapplication.database.DataBase
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var dataBase: DataBase
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        initComponet()
        initListener()
        initUi(savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initComponet() {
        drawerLayout = findViewById(R.id.main)
        navView = findViewById(R.id.navView)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        dataBase = DataBase(this)
    }

    private fun initListener() {
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_presentation -> supportFragmentManager.beginTransaction()
                    .replace(R.id.fgContainer, PresentationFragment())
                    .addToBackStack(null)
                    .commit()

                R.id.nav_form -> supportFragmentManager.beginTransaction()
                    .replace(R.id.fgContainer, FormFragment())
                    .addToBackStack(null)
                    .commit()

                R.id.nav_consult -> supportFragmentManager.beginTransaction()
                    .replace(R.id.fgContainer, ConsultFragment())
                    .addToBackStack(null)
                    .commit()

                R.id.nav_exit -> Toast.makeText(this, "Hola info", Toast.LENGTH_SHORT).show()
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

    }

    private fun initUi(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fgContainer, PresentationFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}