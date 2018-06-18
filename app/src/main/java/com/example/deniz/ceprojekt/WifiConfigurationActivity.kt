package com.example.deniz.ceprojekt

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.net.wifi.ScanResult
import android.widget.EditText

import android.widget.TextView
import kotlinx.android.synthetic.main.activity_third.view.*
import java.io.PrintWriter
import java.net.HttpURLConnection
import java.net.InetAddress
import java.net.Socket
import java.net.URL

class WifiConfigurationActivity : AppCompatActivity() {

    lateinit var wifiName: TextView
    lateinit var resultList: ArrayList<ScanResult>
    lateinit var password: EditText
    lateinit var errorMessage: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)
        wifiName = findViewById<TextView>(R.id.wifiName)
        errorMessage = findViewById<TextView>(R.id.errorMessage)
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

      sendToPi(wifiName.text,password.text)



       // sendToPi(wifiName.text,password.text)
        /*TODO Check status
               while(true){


                   if(/*BUSY*/);

                   else if(/*TIMEOUT*/){
                       sendToPi(wifiName.text,password.text)
                   }
                   else if(/*False*/)
                        displayErrorMessage and input screen

                   else if(/*OK*/)
                        displaySuccessfullMessage


               }
               TODO END*/

    }

   fun sendToPi(name: CharSequence, password: CharSequence) {

       /*
        val url="http://"+getPiIp()+"/connect"
        val connection= URL(url).openConnection() as HttpURLConnection
        connection.requestMethod="POST"
    */

       //TCP Connection - if not working then see https://discuss.kotlinlang.org/t/kotlin-client-tcp/6652

       val serverIP: String = getPiIp()
       val serverPort: Int = 15000 /*TODO*/
       var out: PrintWriter? = null

       //CONNECT

       var serverAddr = InetAddress.getByName(serverIP)
       var socket = Socket(serverAddr, serverPort)

       //send to Pi

       if (out != null && !out!!.checkError()) {

           out!!.println(name)

           out!!.println(password)

           out!!.flush()
       }
   }

    fun displayErrorMessage(){

        errorMessage.text="Password is incorrect"
    }

    fun displaySuccessfullMessage(){

        val fourthPart = Intent(this, SuccessActivity::class.java)
        fourthPart.putExtra("wifi_name",wifiName.text)

        startActivity(fourthPart)


    }

}