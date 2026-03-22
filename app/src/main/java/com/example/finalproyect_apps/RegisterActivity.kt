package com.example.finalproyect_apps

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    private lateinit var nameUser: EditText
    private lateinit var emailUser: EditText
    private lateinit var passwordUser: EditText
    private lateinit var password1User: EditText
    private lateinit var btnCreateAccount: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initVariables()
        btnCreateAccount.setOnClickListener {
            validateData()
//          val intent = Intent(this, MainActivity::class.java)
//          startActivity(intent)
        }
    }
    private fun initVariables(){
        nameUser = findViewById(R.id.input_name)
        emailUser = findViewById(R.id.input_email)
        passwordUser = findViewById(R.id.input_password)
        password1User = findViewById(R.id.input_password1)
        btnCreateAccount = findViewById(R.id.btn_account)
        auth = FirebaseAuth.getInstance()
    }

    private fun validateData(){
        val name = nameUser.text.toString().trim()
        val email = emailUser.text.toString().trim()
        val password =  passwordUser.text.toString().trim()
        val confirmPaswword = password1User.text.toString().trim()

        when {
            name.isEmpty() -> {
                nameUser.error = "Ingresa tu nombre"
                nameUser.requestFocus()
            }
            email.isEmpty() -> {
                emailUser.error = "Ingresa tu correo"
                emailUser.requestFocus()
            }
            password.isEmpty() -> {
                passwordUser.error = "Ingresa su contraseña"
                passwordUser.requestFocus()
            }

            confirmPaswword.isEmpty() -> {
                password1User.error = "Repita su contraseña"
                password1User.requestFocus()
            }

            password != confirmPaswword -> {
                password1User.error = "Contraseñas no coinciden"
                password1User.requestFocus()
            }
            else ->{
                createAccount(email, password)
            }
        }
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            val uid = auth.currentUser?.uid ?: return@addOnSuccessListener
            reference = FirebaseDatabase.getInstance()
                .getReference("Usuarios")
                .child(uid)

            val nameText = nameUser.text.toString().trim()
            val emailText = emailUser.text.toString().trim()
            val userMap = hashMapOf(
                "uid" to uid,
                "username" to nameText,
                "email" to emailText,
                "image" to "",
                "search" to nameText.lowercase()
            )

            reference.setValue(userMap)
                .addOnSuccessListener {
                    Toast.makeText(this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this, "Database error: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        } .addOnFailureListener {
            //Log.e("RegisterActivity", "Auth error: ${it.message}")
            Toast.makeText(this, "Auth error: ${it.message}", Toast.LENGTH_SHORT).show()
        }

    }
}