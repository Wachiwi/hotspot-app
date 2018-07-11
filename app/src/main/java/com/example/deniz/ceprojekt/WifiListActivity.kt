package com.example.deniz.ceprojekt

import android.content.Intent
import android.graphics.Color
import android.net.wifi.ScanResult
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import java.util.*
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log.d
import android.view.View
import android.widget.*
import com.example.deniz.ceprojekt.triggerPiScan
import kotlinx.android.synthetic.main.activity_second.view.*
import org.json.JSONArray
import org.json.JSONObject


// Android extensions import statements for the two views to update

class WifiListActivity : AppCompatActivity() {



    lateinit var resultList: ArrayList<ScanResult>
    lateinit var wifiList: TextView
    lateinit var piList: String
    lateinit var piListJSON: JSONArray
    lateinit var piList2: String
    lateinit var piList2JSON: JSONArray
    lateinit var displayedList:ListView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        //wifiList = findViewById<TextView>(R.id.wifiList)
        displayedList=findViewById<ListView>(R.id.list)
        getList()

    }




    //get the List from the previous activity (e.g. pi or scanned smartphone List
    fun getList(){
        piList=intent.getSerializableExtra("piList") as String
        piListJSON= JSONArray(piList)
        displayPiList3()

    }

    fun displayPhoneList(){


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

/*use piListJSON.getJSONObject(i).getString("ssid") instead of result.SSID*/
    fun displayPiList() {
    val thirdPart = Intent(this, WifiConfigurationActivity::class.java)
    thirdPart.putExtra("piList", piList)


    for (i in 1 until piListJSON.length()) {


        /*Creating a  clickable String - on click - start the third activity*/
        val ss = SpannableString(piListJSON.getJSONObject(i).getString("ssid"))
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View?) {
                //create a new activity passing the name of the wifi
                thirdPart.putExtra("wifiName", piListJSON.getJSONObject(i).getString("ssid"))
                startActivity(thirdPart)
            }

            override fun updateDrawState(ds: TextPaint?) {
                super.updateDrawState(ds)
                ds!!.isUnderlineText = false
            }
        }

        ss.setSpan(clickableSpan, 0, piListJSON.getJSONObject(i).getString("ssid").length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        wifiList.movementMethod = LinkMovementMethod.getInstance();
        wifiList.highlightColor = Color.TRANSPARENT;
        wifiList.append(ss)
        wifiList.append("\n")

    }
}

    fun displayPiList2(){

        val thirdPart = Intent(this, WifiConfigurationActivity::class.java)
        thirdPart.putExtra("piList2", piList2)


        for (i in 1 until piList2JSON.length()) {


            /*Creating a  clickable String - on click - start the third activity*/
            val ss = SpannableString(piList2JSON.getJSONObject(i).getString("SSID"))
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(widget: View?) {
                    //create a new activity passing the name of the wifi
                    thirdPart.putExtra("wifiName", piList2JSON.getJSONObject(i).getString("SSID"))
                    startActivity(thirdPart)
                }

                override fun updateDrawState(ds: TextPaint?) {
                    super.updateDrawState(ds)
                    ds!!.isUnderlineText = false
                }
            }

            ss.setSpan(clickableSpan, 0, piList2JSON.getJSONObject(i).getString("ssid").length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            wifiList.movementMethod = LinkMovementMethod.getInstance();
            wifiList.highlightColor = Color.TRANSPARENT;
            wifiList.append(ss)
            wifiList.append("\n")

        }


    }
    fun displayPiList3(){
       var listItems= arrayListOf<String>()
        for(i in 0 until piListJSON.length()){
           listItems.add( piListJSON.getJSONObject(i).getString("ssid"))

        }
        d("TEST",listItems.toString())
        val arrayAdapter = ArrayAdapter<String>(this, R.layout.listview, listItems)



        displayedList.adapter = arrayAdapter

        displayedList.setOnItemClickListener{
            parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
            val thirdPart = Intent(this, WifiConfigurationActivity::class.java)
            thirdPart.putExtra("piList", piList)
            thirdPart.putExtra("wifiName", piListJSON.getJSONObject(id.toInt()).getString("ssid"))
            startActivity(thirdPart)

        }

    }




    fun refreshButtonClicked(view: View){
     Toast.makeText(this, "List refreshed!",Toast.LENGTH_SHORT).show()
        piList=triggerPiScan()
        piListJSON=JSONArray(piList)
        getList()

    }



}
