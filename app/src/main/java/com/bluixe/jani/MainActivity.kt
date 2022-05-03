package com.bluixe.jani

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val ddl_btn = findViewById<Button>(R.id.button1)
        val cal_btn = findViewById<Button>(R.id.button2)
        ddl_btn.setOnClickListener {
            val intent = Intent(this, DDL::class.java)
            startActivity(intent)
        }
        cal_btn.setOnClickListener {
            val intent = Intent(this, Calender::class.java)
            startActivity(intent)
        }
    }
}