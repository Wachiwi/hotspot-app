package com.example.deniz.ceprojekt

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.net.wifi.ScanResult
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.widget.EditText

import android.widget.TextView
import android.widget.Toast
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import kotlinx.android.synthetic.main.activity_third.view.*
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
    lateinit var resultList: ArrayList<ScanResult>
    lateinit var passwordField: EditText
    lateinit var errorMessage: TextView
    lateinit var piList: String
    lateinit var piList2: String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)
        wifiName = findViewById<TextView>(R.id.wifiName)
        errorMessage = findViewById<TextView>(R.id.errorMessage)
        passwordField=findViewById<EditText>(R.id.password)
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

        //resultList= intent.getSerializableExtra("resultList") as java.util.ArrayList<ScanResult>
        //piList=intent.getSerializableExtra("piList") as String
        // piListJSON= JSONArray(piList)
        piList2=intent.getSerializableExtra("piList2") as String
        secondPart.putExtra("piList2",piList2)
        startActivity(secondPart)

    }



    fun buttonSubmitClicked(view: View){

      sendToPi(wifiName.text,passwordField.text)

        var reachable =false

        connectToWifi()

        //ping to raspberry
        var serverAddr = InetAddress.getByName(getPiIp())
        reachable=serverAddr.isReachable(10000)


        if(reachable)
        displaySuccessfullMessage()


        if(!reachable) {
            val firstPart = Intent(this, MainActivity::class.java)

            startActivity(firstPart)
        }

    }

   fun sendToPi(name: CharSequence, password: CharSequence) {


        /*val url="http://"+getPiIp()+"/connect?ssid="+ name.toString() + "&passphrase=" + password.toString()
        val connection= URL(url).openConnection() as HttpURLConnection
        connection.requestMethod="POST"
        //connection.outputStream.use{it.bufferedWriter().use { bufferedWriter-> bufferedWriter.write(name.toString()+ " " +password.toString()) }}
        //connection.outputStream.flush()
        connection.connect()
        connection.disconnect()
*/

  /*     val url="http://"+getPiIp()+"/connect"
       println(1)
       val (request, response, result) = url.httpPost().body(name.toString()+" " +password.toString(), ) // result is Result<String, FuelError>
*/

       Fuel.post("http://"+getPiIp()+"/connect?ssid="+ name.toString() + "&passphrase=" + password.toString()).response { request, response, result ->
       }
       /*TODO Dokumentation mit fuel get*/

       //TCP Connection - if not working then see https://discuss.kotlinlang.org/t/kotlin-client-tcp/6652
/*
       val serverIP: String = getPiIp()
       val serverPort: Int = 5454
       var out: PrintWriter? = null

       //CONNECT

       var serverAddr = InetAddress.getByName(serverIP)
       var socket = Socket(serverAddr, serverPort)

       //send to Pi

       socket.getOutputStream().use { it.bufferedWriter().use { bufferedWriter-> bufferedWriter.write("{"+"\"SSID\":"+wifiName.text.toString()+","+ "\"PW\":"+passwordField.text.toString()+"}") } }*/
   }

    fun displayErrorMessage(){

        errorMessage.text="Password is incorrect"
    }

    fun displaySuccessfullMessage(){

        val fourthPart = Intent(this, SuccessActivity::class.java)
        fourthPart.putExtra("wifi_name",wifiName.text)

        startActivity(fourthPart)


    }

    fun connectToWifi(){
        var isConfigured=false

        //Connection to the same wifi as pi
        WIFI_NAME=wifiName.text.toString()
        WIFI_PASSWORD=passwordField.text.toString()

        wifiManager=this.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager


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

        wifiManager.disconnect()

        var networkId =0

        //search all configured wifi connections
        for(item in wifiManager.configuredNetworks) {

            //if the netwpork is already configured, find the id and display the desired information
            if(item.SSID.substring(1,item.SSID.length-1)==(WIFI_NAME)) {
                networkId = item.networkId
            }
        }

        //connect to the wifi
        wifiManager.enableNetwork(networkId,true)
        wifiManager.reconnect()

    }

}