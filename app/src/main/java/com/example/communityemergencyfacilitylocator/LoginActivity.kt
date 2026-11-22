package com.example.communityemergencyfacilitylocator

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        val loginBtn = findViewById<Button>(R.id.loginBtn)
        val registerScreenBtn =
            findViewById<Button>(R.id.registerScreenBtn)

        loginBtn.setOnClickListener {

            val userEmail = email.text.toString().trim()
            val userPassword = password.text.toString().trim()

            auth.signInWithEmailAndPassword(
                userEmail,
                userPassword
            ).addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    val uid = auth.currentUser?.uid

                    FirebaseDatabase.getInstance(
                        "https://emergency-facility-locator-default-rtdb.asia-southeast1.firebasedatabase.app"
                    )
                        .getReference("users")
                        .child(uid!!)
                        .child("role")
                        .get()
                        .addOnSuccessListener { snapshot ->

                            val role = snapshot.value.toString()

                            when (role) {

                                "Admin" -> {
                                    startActivity(
                                        Intent(
                                            this,
                                            AdminActivity::class.java
                                        )
                                    )
                                }

                                "Worker" -> {
                                    startActivity(
                                        Intent(
                                            this,
                                            WorkerActivity::class.java
                                        )
                                    )
                                }

                                else -> {
                                    startActivity(
                                        Intent(
                                            this,
                                            HomeActivity::class.java
                                        )
                                    )
                                }
                            }

                            finish()
                        }

                } else {

                    Toast.makeText(
                        this,
                        task.exception?.message,
                        Toast.LENGTH_LONG
                    ).show()

                }

            }

        }



        registerScreenBtn.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    RegisterActivity::class.java
                )
            )

        }
    }
}
