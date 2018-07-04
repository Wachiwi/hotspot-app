package com.example.deniz.ceprojekt

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_fourth.*
import kotlinx.android.synthetic.main.activity_third.*



class SuccessActivity : AppCompatActivity() {

    lateinit var successMessage: TextView
    lateinit var displayedSymbol: TextView
    lateinit var wifi_name: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fourth)

        wifi_name=intent.getSerializableExtra("wifi_name") as String
        successMessage = findViewById<TextView>(R.id.successMessage)
        displayedSymbol= findViewById<TextView>(R.id.displayedSymbol)
        displayedSymbol.append("\u2713")
        successMessage.append("Successfully Connected the pi to")
        successMessage.append(wifi_name)



    }
}