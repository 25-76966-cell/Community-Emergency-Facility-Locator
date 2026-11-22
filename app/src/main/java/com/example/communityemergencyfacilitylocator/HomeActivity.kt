package com.example.communityemergencyfacilitylocator

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)

        val hospitalBtn =
            findViewById<Button>(R.id.hospitalBtn)

        val policeBtn =
            findViewById<Button>(R.id.policeBtn)

        val fireBtn =
            findViewById<Button>(R.id.fireBtn)

        val reportBtn =
            findViewById<Button>(R.id.reportBtn)

        val emergencyBtn =
            findViewById<Button>(R.id.emergencyBtn)

        val trackReportBtn =
            findViewById<Button>(R.id.trackReportBtn)

        val logoutBtn =
            findViewById<Button>(R.id.logoutBtn)

        hospitalBtn.setOnClickListener {

            val intent = Intent(this, MapActivity::class.java)
            intent.putExtra("TYPE", "Hospital")
            startActivity(intent)

        }

        policeBtn.setOnClickListener {

            val intent = Intent(this, MapActivity::class.java)
            intent.putExtra("TYPE", "Police")
            startActivity(intent)

        }

        fireBtn.setOnClickListener {

            val intent = Intent(this, MapActivity::class.java)
            intent.putExtra("TYPE", "Fire")
            startActivity(intent)

        }

        reportBtn.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    ReportActivity::class.java
                )
            )
        }

        emergencyBtn.setOnClickListener {

            val intent =
                Intent(Intent.ACTION_DIAL)

            intent.data =
                Uri.parse("tel:911")

            startActivity(intent)
        }

        trackReportBtn.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    TrackReportActivity::class.java
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