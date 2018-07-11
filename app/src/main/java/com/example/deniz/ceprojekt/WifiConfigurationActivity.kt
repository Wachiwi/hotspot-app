package com.example.deniz.ceprojekt

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.net.wifi.ScanResult
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.util.Log.d
import android.widget.EditText

import android.widget.TextView
import android.widget.Toast
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import kotlinx.android.synthetic.main.activity_third.view.*
import org.json.JSONArray
import java.io.PrintWriter
import java.net.HttpURLConnection
import java.net.InetAddress
import java.net.Socket
import java.net.URL
import java.nio.charset.Charset
import kotlin.text.Charsets.UTF_8

class WifiConfigurationActivity : AppCompatActivity() {

    lateinit var wifiManager: WifiManager

    lateinit var wifiName: TextView
    lateinit var passwordField: EditText
    lateinit var errorMessage: TextView
    lateinit var piList: String
    lateinit var piListJSON: JSONArray




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)
        wifiName = findViewById<TextView>(R.id.wifiName)
        errorMessage = findViewById<TextView>(R.id.errorMessage)
        passwordField=findViewById<EditText>(R.id.password)
        getWifiName()
        piList=intent.getSerializableExtra("piList") as String
        piListJSON= JSONArray(piList)

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
        secondPart.putExtra("piList",piList)
        startActivity(secondPart)
    }



    fun buttonSubmitClicked(view: View){

      sendToPi(wifiName.text,passwordField.text)

        var reachable =false

        connectToWifi()

        //ping to raspberry
        var serverAddr = InetAddress.getByName("raspberrypi")
        reachable=serverAddr.isReachable(10000)


        if(reachable)
        displaySuccessfullMessage()


        if(!reachable) {
            val firstPart = Intent(this, MainActivity::class.java)

            startActivity(firstPart)

        }

    }

   fun sendToPi(name: CharSequence, password: CharSequence) {
       Fuel.post("http://"+getPiIp()+"/connect?ssid="+ name.toString() + "&passphrase=" + password.toString()).response { request, response, result ->
       }

   }

    fun displayErrorMessage(){

        errorMessage.text="Password is incorrect"
    }

    fun displaySuccessfullMessage(){

        val fourthPart = Intent(this, SuccessActivity::class.java)
        fourthPart.putExtra("wifi_name",wifiName.text.toString())

        startActivity(fourthPart)


    }

    fun connectToWifi(){
        var isConfigured=false

        //Connection to the same wifi as pi
        WIFI_NAME=wifiName.text.toString()
        WIFI_PASSWORD=passwordField.text.toString()

        wifiManager=this.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        d("DEBUG",WIFI_NAME)
        d("DEBUG", WIFI_PASSWORD)

        //check if the wifi is already configured
        for(item in wifiManager.configuredNetworks) {
            if(item.SSID.substring(1,item.SSID.length-1)==(WIFI_NAME)) {
                isConfigured=true
            }
        }

        //if it is not configured, configure it
        if(!isConfigured){
            val conf = WifiConfiguration()
            conf.SSID = WIFI_NAME
            conf.preSharedKey = WIFI_PASSWORD
            wifiManager.addNetwork(conf)
        }



        var networkId =0

        //search all configured wifi connections
        for(item in wifiManager.configuredNetworks) {

            //if the netwpork is already configured, find the id and display the desired information
            if(item.SSID.substring(1,item.SSID.length-1)==(WIFI_NAME)) {
                networkId = item.networkId
            }
        }

        //connect to the wifi
        wifiManager.disconnect()
        wifiManager.enableNetwork(networkId,true)
        wifiManager.reconnect()

    }



}