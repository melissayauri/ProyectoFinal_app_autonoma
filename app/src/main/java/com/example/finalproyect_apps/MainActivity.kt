package com.example.finalproyect_apps

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var txtNombre: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initVariables()
        showData()
    }

    // crear el menu superior
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

// crear la opcion cerrar sesion
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_logout -> {
                //deslogueo
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this@MainActivity, StartActivity::class.java)
                Toast.makeText(applicationContext, "Has cerrado sesión", Toast.LENGTH_SHORT).show()
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun initVariables(){
        txtNombre = findViewById(R.id.txtNombre)
    }

    private fun showData() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val reference = FirebaseDatabase.getInstance()
            .getReference("Usuarios")
            .child(uid)

        reference.get().addOnSuccessListener {
            val nombre = it.child("username").value.toString()

            // Mostrar datos
            txtNombre.text = "Bienvenido, $nombre"

            // Inicial (avatar)


        }.addOnFailureListener {
            txtNombre.text = "Error al cargar datos"
        }
    }
}