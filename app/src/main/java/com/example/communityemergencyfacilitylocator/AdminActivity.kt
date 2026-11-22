package com.example.communityemergencyfacilitylocator

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_admin)

        val viewReportsBtn =
            findViewById<Button>(R.id.viewReportsBtn)

        val logoutBtn =
            findViewById<Button>(R.id.logoutBtn)


        viewReportsBtn.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    ReportListActivity::class.java
                )
            )

        }

        logoutBtn.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    LoginActivity::class.java
                )
            )

            finish()

        }
    }
}