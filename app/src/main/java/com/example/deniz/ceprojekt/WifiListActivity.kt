package com.example.deniz.ceprojekt

import android.content.Intent
import android.graphics.Color
import android.net.wifi.ScanResult
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.widget.TextView
import java.util.*
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View


// Android extensions import statements for the two views to update

class WifiListActivity : AppCompatActivity() {



    lateinit var resultList: ArrayList<ScanResult>
    lateinit var wifiList: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        wifiList = findViewById<TextView>(R.id.wifiList)
        getList()

    }




    //get the List from the previous activity (e.g. pi or scanned smartphone List
    fun getList(){
        resultList= intent.getSerializableExtra("resultList") as ArrayList<ScanResult>
        displayList()
    }

    fun displayList(){


        val thirdPart = Intent(this, WifiConfigurationActivity::class.java)
        thirdPart.putExtra("resultList",resultList)

        for(result in resultList){
            /*Creating a  clickable String - on click - start the third activity*/
            val ss = SpannableString(result.SSID)
            val clickableSpan = object : ClickableSpan(){
                override fun onClick(widget: View?){
                    //create a new activity passing the name of the wifi
                    thirdPart.putExtra("wifiName",result.SSID)
                    startActivity(thirdPart)
                }
                override fun updateDrawState(ds: TextPaint?) {
                    super.updateDrawState(ds)
                    ds!!.isUnderlineText = false
                }
            }
            ss.setSpan(clickableSpan,0,result.SSID.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            wifiList.movementMethod = LinkMovementMethod.getInstance();
            wifiList.highlightColor = Color.TRANSPARENT;
            wifiList.append(ss)
            wifiList.append("\n")


            /*Creating a  clickable String end*/


       }


   }

}
