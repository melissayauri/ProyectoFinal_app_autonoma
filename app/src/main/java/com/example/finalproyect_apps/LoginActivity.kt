package com.example.finalproyect_apps

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var lgEmail: EditText
    private lateinit var lgPassword: EditText
    private lateinit var btnLg: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        supportActionBar?.title = "Login"
        initVariables()
        btnLg.setOnClickListener {
            validateData()
        }
    }

    private fun initVariables(){
        lgEmail = findViewById(R.id.email_login)
        lgPassword = findViewById(R.id.password_login)
        btnLg = findViewById(R.id.btn_sigIn)
        auth = FirebaseAuth.getInstance()
    }

    private fun validateData() {
        val email = lgEmail.text.toString().trim()
        val password = lgPassword.text.toString().trim()

        when {
            email.isEmpty() -> {
                lgEmail.error = "Ingresa tu correo"
                lgEmail.requestFocus()
            }

            password.isEmpty() -> {
                lgPassword.error = "Ingresa tu contraseña"
                lgPassword.requestFocus()
            }

            else -> {
                loginUser(email, password)
            }
        }
    }

    private fun loginUser(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    val errorMessage = task.exception?.message ?: "Error desconocido"
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
    }
}