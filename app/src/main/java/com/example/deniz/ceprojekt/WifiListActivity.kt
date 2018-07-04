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
import android.util.Log.d
import android.view.View
import android.widget.ListView
import com.example.deniz.ceprojekt.triggerPiScan
import kotlinx.android.synthetic.main.activity_second.view.*
import org.json.JSONArray
import org.json.JSONObject
import android.widget.ArrayAdapter




// Android extensions import statements for the two views to update

class WifiListActivity : AppCompatActivity() {



    lateinit var resultList: ArrayList<ScanResult>
    lateinit var piList: String
    lateinit var wifiList: TextView

    lateinit var piListJSON: JSONArray
    lateinit var piList2: String
    lateinit var piList2JSON: JSONArray
    lateinit var displayedList:ListView
    lateinit var listItems: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        wifiList = findViewById<TextView>(R.id.wifiList)
        //displayedList=findViewById<ListView>(R.id.list)  //TODO Liste schöner machen
        getList()

    }




    //get the List from the previous activity (e.g. pi or scanned smartphone List
    fun getList(){
        piList=intent.getSerializableExtra("piList") as String
        piListJSON= JSONArray(piList)
        displayPiList()
        //displayPiList2()
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

        for(i in 1 until piListJSON.length()){
           listItems[i]= piList2JSON.getJSONObject(i).getString("SSID")

        }
        d("TEST",listItems.toString())
        val arrayAdapter = ArrayAdapter<String>(this, R.layout.listview, listItems)

        //displayedList.setOnItemClickListener()

        displayedList.adapter = arrayAdapter
        //TODO - Liste schöner machen


    }

    //TODO - copy in activity_second - Liste schöner machen
    /* <ListView
        android:id="@+id/list"
        android:layout_width="262dp"
        android:layout_height="65dp"
        android:layout_marginBottom="8dp"
        android:layout_marginEnd="8dp"
        android:layout_marginStart="8dp"
        android:layout_marginTop="8dp"
        android:divider="@color/colorPrimaryDark"
        android:dividerHeight="1dp"
        app:layout_constraintBottom_toTopOf="@+id/button4"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/wifiList" />
*/


    fun refreshButtonClicked(view: View){
        wifiList.text="Refreshed: \n"
        piList=triggerPiScan()
        piListJSON=JSONArray(piList)
        getList()

    }

}
