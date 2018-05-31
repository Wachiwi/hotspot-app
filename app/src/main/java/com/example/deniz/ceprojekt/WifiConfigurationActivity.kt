package com.example.deniz.ceprojekt

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.net.wifi.ScanResult
import android.widget.EditText

import android.widget.TextView

class WifiConfigurationActivity : AppCompatActivity() {

    lateinit var wifiName: TextView
    lateinit var resultList: ArrayList<ScanResult>
    lateinit var password: EditText



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)
        wifiName = findViewById<TextView>(R.id.wifiName)

        getWifiName()

    }

    fun getWifiName(){


        wifiName.append(intent.getSerializableExtra("wifiName") as String)


    }

    fun buttonCancelClicked(view: View){

        val firstPart = Intent(this, MainActivity::class.java)
        startActivity(firstPart)

    }


    fun buttonBackClicked(view: View){

        val secondPart = Intent(this, WifiListActivity::class.java)
        resultList= intent.getSerializableExtra("resultList") as java.util.ArrayList<ScanResult>
        secondPart.putExtra("resultList",resultList)
        startActivity(secondPart)

    }


    fun buttonSubmitClicked(view: View){
        password = findViewById<EditText>(R.id.password)


        /*TODO Send Password to Raspberry Pi*/
        wifiName.append(password.text)



    }

}