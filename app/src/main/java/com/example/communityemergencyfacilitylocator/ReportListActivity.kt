package com.example.communityemergencyfacilitylocator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import android.widget.Toast

class ReportListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_report_list)

        val txt =
            findViewById<TextView>(R.id.reportsText)

        val comingBtn =
            findViewById<Button>(R.id.comingBtn)

        val doneBtn =
            findViewById<Button>(R.id.doneBtn)

        val reportId = "-P6kKL2kVLtbr9D7PBvI"

        FirebaseDatabase.getInstance(
            "https://emergency-facility-locator-default-rtdb.asia-southeast1.firebasedatabase.app"
        )
            .getReference("reports")
            .child(reportId)
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    val type =
                        snapshot.child("type")
                            .getValue(String::class.java)

                    val description =
                        snapshot.child("description")
                            .getValue(String::class.java)

                    val status =
                        snapshot.child("status")
                            .getValue(String::class.java)

                    txt.text =
                        "Type: $type\n\n" +
                                "Description: $description\n\n" +
                                "Current Status: $status"
                }

                override fun onCancelled(error: DatabaseError) {
                    txt.text = error.message
                }
            })

        doneBtn.setOnClickListener {

            FirebaseDatabase.getInstance(
                "https://emergency-facility-locator-default-rtdb.asia-southeast1.firebasedatabase.app"
            )
                .getReference("reports")
                .child(reportId)
                .child("status")
                .setValue("Done")
                .addOnSuccessListener {

                    Toast.makeText(
                        this,
                        "Incident marked as completed",
                        Toast.LENGTH_SHORT
                    ).show()

                }
        }

        comingBtn.setOnClickListener {

            FirebaseDatabase.getInstance(
                "https://emergency-facility-locator-default-rtdb.asia-southeast1.firebasedatabase.app"
            )
                .getReference("reports")
                .child(reportId)
                .child("status")
                .setValue("Coming")
                .addOnSuccessListener {

                    Toast.makeText(
                        this,
                        "Worker is on the way",
                        Toast.LENGTH_SHORT
                    ).show()

                }
        }

        doneBtn.setOnClickListener {

            FirebaseDatabase.getInstance(
                "https://emergency-facility-locator-default-rtdb.asia-southeast1.firebasedatabase.app"
            )
                .getReference("reports")
                .child(reportId)
                .child("status")
                .setValue("Done")
        }
    }
}