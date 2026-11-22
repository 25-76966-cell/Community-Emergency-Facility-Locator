package com.example.communityemergencyfacilitylocator

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class FacilityActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val txt = TextView(this)
        txt.text = "VIEW FACILITIES IS WORKING!"

        setContentView(txt)
    }
}
