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
        val plan_btn = findViewById<Button>(R.id.button3)
        val dia_btn = findViewById<Button>(R.id.button4)
        val intent = Intent(this, MyService::class.java)
        startService(intent)
        ddl_btn.setOnClickListener {
            val intent = Intent(this, DDL::class.java)
            startActivity(intent)
        }
        cal_btn.setOnClickListener {
            val intent = Intent(this, Calendar::class.java)
            startActivity(intent)
        }
        plan_btn.setOnClickListener {
            val intent = Intent(this, Plan::class.java)
            startActivity(intent)
        }
        dia_btn.setOnClickListener {
            val intent = Intent(this, Diary::class.java)
            startActivity(intent)
        }
    }
}