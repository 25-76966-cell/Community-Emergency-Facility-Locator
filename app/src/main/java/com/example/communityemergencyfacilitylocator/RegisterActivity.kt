package com.example.communityemergencyfacilitylocator

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        val registerBtn = findViewById<Button>(R.id.registerBtn)

        registerBtn.setOnClickListener {

            val userEmail = email.text.toString().trim()
            val userPassword = password.text.toString().trim()

            if (userEmail.isEmpty() || userPassword.isEmpty()) {

                Toast.makeText(
                    this,
                    "Please fill all fields",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(
                userEmail,
                userPassword
            ).addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    Toast.makeText(
                        this,
                        "Registration Successful!",
                        Toast.LENGTH_LONG
                    ).show()

                    finish()

                } else {

                    Toast.makeText(
                        this,
                        task.exception?.message,
                        Toast.LENGTH_LONG
                    ).show()
                }

            }
        }
    }
}