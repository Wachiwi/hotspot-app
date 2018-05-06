package com.example.deniz.ceprojekt

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log.d
import android.view.View
import java.util.*
import android.widget.TextView
import android.widget.Toast

const val PERMISSION_REQUEST_ACCESS_FINE_LOCATION=0
const val PERMISSION_REQUEST_ACCESS_COARSE_LOCATION=0

class MainActivity : AppCompatActivity() {


    //List to save the results
    var resultList = ArrayList<ScanResult>()


    //text views on the screen
    lateinit var wifiList: TextView

    //var for the wifiManager API
    lateinit var wifiManager: WifiManager



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
        wifiList = findViewById<TextView>(R.id.wifiList)

    }
/**
 *
 *
 *
 * MAYBE IF THIS DOES NOT WORK ADD SECOND PERMISSION ACCESS_COARSE_LOCATION
 *
 *
 *
 *
 * */
    fun startScanClicked(view: View){

        // Check if the permission has been granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permission is already available, start scanning
            Toast.makeText(this, "Permission was granted",Toast.LENGTH_LONG).show()
            startScanning()
        }
        else {
            requestPermission()
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

        d("Test: Unregister", "0")

        val stringList = ArrayList<String>()

        //Add the result names to a List
        for(result in resultList){
            stringList.add(result.SSID)
            stringList.add("\n")
            d("Test", result.SSID)
        }
        Toast.makeText(this, "Scan Completed!",Toast.LENGTH_SHORT).show()
        wifiList.text=stringList.toString()


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
