package com.example.communityemergencyfacilitylocator

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class TrackReportActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_track_report)

        val statusText =
            findViewById<TextView>(R.id.statusText)

        FirebaseDatabase.getInstance(
            "https://emergency-facility-locator-default-rtdb.asia-southeast1.firebasedatabase.app"
        )
            .getReference("reports")
            .child("-P6kKL2kVLtbr9D7PBvI")
            .child("status")
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    statusText.text =
                        "Current Report Status\n\n${snapshot.value}"

                }

                override fun onCancelled(error: DatabaseError) {

                    statusText.text =
                        error.message

                }
            })
    }
}