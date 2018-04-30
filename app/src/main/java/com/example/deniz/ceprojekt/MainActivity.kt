package com.example.deniz.ceprojekt

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    //displays a pop up Message
    fun toastMe(view: View){
        //val myToast =Toast.makeText(this, message,duration);
        val myToast = Toast.makeText(this, "Hallo Andrea!",Toast.LENGTH_SHORT)
        myToast.show()

    }

    fun countMe (view: View){
        //get the textView
        val showCountTextView=findViewById<TextView>(R.id.textView)

        //get the value of the text view
        val countString= showCountTextView.text.toString()

        //Covert string to number and increment it
        var count=Integer.parseInt(countString)
        count++

        //display the new value in the text view
        showCountTextView.text=count.toString()
    }

    fun randomMe(view: View){

        val showCountTextView=findViewById<TextView>(R.id.textView)

        // Create an Intent to start the second activity
        val randomIntent = Intent(this, SecondActivity::class.java)


        //get the value of the text view
        val countString= showCountTextView.text.toString()

        //Covert string to number and increment it
        var count=Integer.parseInt(countString)


        randomIntent.putExtra(SecondActivity.TOTAL_COUNT,count)

        // Start the new activity.
        startActivity(randomIntent)
    }
}
