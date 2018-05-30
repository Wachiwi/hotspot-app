package com.example.deniz.ceprojekt

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.util.Log.d
import android.view.View
import java.util.*
import android.widget.TextView
import android.widget.Toast
import com.example.deniz.ceprojekt.R.id.displayedText
import kotlinx.android.synthetic.main.activity_main.view.*

const val PERMISSION_REQUEST_ACCESS_FINE_LOCATION=0
//const val PERMISSION_REQUEST_ACCESS_COARSE_LOCATION=0

lateinit var WIFI_NAME: String
lateinit var WIFI_PASSWORD: String

class MainActivity : AppCompatActivity() {


    //List to save the results
    var resultList = ArrayList<ScanResult>()


    //text views on the screen
    lateinit var displayedText: TextView

    lateinit var displayedSymbol: TextView

    //var for the wifiManager API
    lateinit var wifiManager: WifiManager

    lateinit var hotspotToConnectTo: ScanResult


    var actionInProgress=false
    var isConfigured =false
    var hotspotIsAvailable=false



    val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {

            //if the results are available, proceed
            if (intent!!.action == WifiManager.SCAN_RESULTS_AVAILABLE_ACTION) {
                //3. get the results and save them in a variable
                resultList = wifiManager.scanResults as ArrayList<ScanResult> //Problem, scan Results gets no results
                d("Test", resultList.toString())
             for(result in resultList)
                d("Test", result.SSID)

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*"Instances of this class [WifiManager]must be obtained using Context.getSystemService(Class) with the argument WifiManager.class
        or Context.getSystemService(String) with the argument Context.WIFI_SERVICE.*/


        //get the wifiManager API on Create
        wifiManager=this.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        //get the TextViews
        displayedText = findViewById<TextView>(R.id.displayedText)
        displayedSymbol= findViewById<TextView>(R.id.displayedSymbol)

    }

    //set variables to null/default value

    fun init(){

        hotspotIsAvailable=false
        displayedText.text=""
        displayedSymbol.text=""
        resultList.clear()

    }


    fun buttonOneClicked(view: View){
        WIFI_NAME ="Ax494"
        WIFI_PASSWORD="Iam1H0tSp0t!?"

        buttonClicked()
    }


    fun buttonTwoClicked(view: View){
        WIFI_NAME ="Rpi"
        WIFI_PASSWORD="123456789"

        buttonClicked()
    }
fun buttonClicked(){

    //set variables to null/default value
    init()

    //if the wifi is currently disabled, enable it
    if(!wifiManager.isWifiEnabled){
        wifiManager.isWifiEnabled = true
    }
    //if no action is in progress
    if (!actionInProgress) {
        // Check if the permission has been granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permission is already available, start scanning
            Toast.makeText(this, "Permission was granted", Toast.LENGTH_SHORT).show()
            actionInProgress=true
            startScanning()
        } else {
            requestPermission()
        }
    }
    //if a action is already in progress, please wait
    else{
        Toast.makeText(this, "Actionh already in progress. Please Wait.", Toast.LENGTH_SHORT).show()

    }


}



    private fun startScanning(){
        Toast.makeText(this, "Starting Scan!",Toast.LENGTH_SHORT).show()

        //1. Start Scanning
        wifiManager.startScan()

        //2. Waiting for the Scan_Result_is_Available
        registerReceiver(broadcastReceiver, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))

        //wait 10 seconds
        Handler().postDelayed({
            stopScanning()
        }, 10000)
    }

    private fun stopScanning(){

        unregisterReceiver(broadcastReceiver)
        /**ONLY FOR TESTING
        d("Test: Unregister", "0")

        val stringList = ArrayList<String>()

        //Add the result names to a List
        for(result in resultList){
            if(result.SSID==(WIFI_NAME)){
                stringList.add(result.SSID)
                stringList.add("\n")
                d("Test", result.SSID)
            }
        }
        *ONLY FOR TESTING end*/

        Toast.makeText(this, "Scan Completed!",Toast.LENGTH_SHORT).show()

        checkForHotspot()
    }

    private fun checkForHotspot(){

        // The wifi is saved in the variable hotspotToConnectTo
        for(result in resultList){
            if(result.SSID== WIFI_NAME){
                hotspotToConnectTo=result
                hotspotIsAvailable=true


            }
        }
        //if the variable is initialized above then trying to connect to the hotspot
        if(hotspotIsAvailable){
            Toast.makeText(this, "Hotspot Found - trying to Connect",Toast.LENGTH_SHORT).show()
            connectToHotspot()
        }

        //if the hotspot was not found...
        else{

            //check if the hotspot is already configured
                for(item in wifiManager.configuredNetworks) {
                    if(item.SSID.substring(1,item.SSID.length-1)==(WIFI_NAME)) {
                        isConfigured=true
                    }
                }
            //if it is not configured, configure it and scan again

            if(!isConfigured){
                val conf = WifiConfiguration()
                conf.SSID = WIFI_NAME
                conf.preSharedKey = WIFI_PASSWORD
                wifiManager.addNetwork(conf)
                Toast.makeText(this, "Hotspot not found - network will be configured", Toast.LENGTH_SHORT).show()
                isConfigured=true
                actionInProgress=false //able to start a new scan
                startScanning()

            }
            else {
                //if it is already configured display a check your device message
                displayedText.append("Hotspot can not be found - check your devices")
               // displayedSymbol.setTextColor(0xFF0000)
                displayedSymbol.append("X")

                actionInProgress=false


            }

    }
        }

    private fun connectToHotspot(){

        //disconnect the active wifi connection
        wifiManager.disconnect()
        val stringList = ArrayList<String>()
        var networkId =0

        //search all configured wifi connections
        for(item in wifiManager.configuredNetworks) {

            //if the netwpork is already configured, find the id and display the desired information
            if(item.SSID.substring(1,item.SSID.length-1)==(WIFI_NAME)) {
                networkId = item.networkId
                displayedText.append("    Name of the connected Wifi: ")
                displayedText.append(item.SSID)
                Toast.makeText(this, "network is already configured", Toast.LENGTH_SHORT).show()
            }
        }

        //connect to the wifi
        wifiManager.enableNetwork(networkId,true)
        wifiManager.reconnect()


        Toast.makeText(this, "Connected to iPhone hotspot",Toast.LENGTH_LONG).show()
        //displayedSymbol.setTextColor(0x00FF00)
        displayedSymbol.append("\u2713")
        actionInProgress=false

    }

    private fun requestPermission(){
            // Permission was not granted and must be requested
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                Toast.makeText(this, "Access Required!",Toast.LENGTH_SHORT).show()

                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_ACCESS_FINE_LOCATION)

            } else {
                // No explanation needed, we can request the permission.
                Toast.makeText(this, "Permission not available!",Toast.LENGTH_SHORT).show()

                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_ACCESS_FINE_LOCATION)

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_ACCESS_FINE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted
                    Toast.makeText(this, "Permission was granted",Toast.LENGTH_LONG).show()
                    startScanning()
                } else {
                    // permission denied
                    Toast.makeText(this, "Permission denied",Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }




}
