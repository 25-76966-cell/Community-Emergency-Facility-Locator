package com.example.communityemergencyfacilitylocator

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class ReportActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_report)

        val typeInput =
            findViewById<EditText>(R.id.typeInput)

        val descriptionInput =
            findViewById<EditText>(R.id.descriptionInput)



        // save report

        val submitBtn =
            findViewById<Button>(R.id.submitBtn)

        submitBtn.setOnClickListener {

            val type = typeInput.text.toString().trim()
            val description = descriptionInput.text.toString().trim()

            if (type.isEmpty() || description.isEmpty()) {
                Toast.makeText(
                    this,
                    "Please fill all fields",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val database = FirebaseDatabase.getInstance(
                "https://emergency-facility-locator-default-rtdb.asia-southeast1.firebasedatabase.app"
            )

            val reportId = database.reference
                .child("reports")
                .push()
                .key!!

            val report = mapOf(
                "reportId" to reportId,
                "type" to type,
                "description" to description,
                "status" to "Pending"
            )

            database.reference
                .child("reports")
                .child(reportId)
                .setValue(report)
                .addOnSuccessListener {

                    Toast.makeText(
                        this,
                        "Report Submitted!",
                        Toast.LENGTH_LONG
                    ).show()

                    typeInput.setText("")
                    descriptionInput.setText("")
                }
                .addOnFailureListener { e ->

                    Toast.makeText(
                        this,
                        "Failed: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
    }
}