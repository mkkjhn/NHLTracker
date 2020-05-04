package com.example.nhltracker

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kotlin.reflect.KCallable
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlinx.android.synthetic.main.activity_main.*
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT

// dynamic textview
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginTop
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import org.json.JSONArray

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get data from StartupActivity
        val scheduleJSON = intent.getStringExtra("schedule")

        // Display data
        var gson = Gson()
        var schedule = gson.fromJson(scheduleJSON, Schedule::class.java)


        // denzil
        //val scheduleJSON = intent.getStringExtra("schedule")
        //val objects = listOf<Schedule>()

        // creating TextView programmatically
        val linearLayout = findViewById<LinearLayout>(R.id.linear_main)

        var currentBGColor = R.color.dataTextBGColor2
        var tvBackgroundLight = false

        for ( prop in Schedule::class.memberProperties) {
            val itemJSON = prop
            //println("${prop.name} = ${prop.get(schedule)}")

            val textView = TextView(this)
            //textView.gravity = Gravity.CENTER
            textView.setTextColor(resources.getColor(R.color.dataTextColor))

            //val cd = textView.background as ColorDrawable

            // Alternate BG color
            if ( tvBackgroundLight ) {
                currentBGColor = R.color.dataTextBGColor2
                tvBackgroundLight = false
            }
            else {
                currentBGColor = R.color.dataTextBGColor1
                tvBackgroundLight = true
            }

            textView.setBackgroundResource(currentBGColor)

            textView.text = "${prop.name}: ${prop.get(schedule)}"

            // Add TextView to LinearLayout
            linearLayout?.addView(textView)

        }
        /*
        val sList = listOf(schedule)

        for (i in sList) {
            val (name, value) = i
            println("ITEROINTI ALKAA")
            println(name)
            println(value)
        }
        val (
            copyright,
            totalItems,
            totalEvents,
            totalGames,
            totalMatches,
            wait,
            dates
        ) = schedule
        */



        // Menee väärin, yksi kerrallaan alekkain, nyt ottaa ilmeisesti kaikki kerralla
        /*
        for ((a) in listOf(schedule)) {
            val textView = TextView(this)
            textView.layoutParams = ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            textView.gravity = Gravity.CENTER
            textView.setTextColor(resources.getColor(R.color.dataTextColor))
            textView.setBackgroundColor(resources.getColor(R.color.dataTextBGColor))
            textView.text = "$a"
            // Add TextView to LinearLayout
            linearLayout?.addView(textView)
        }
        */

        //textView.setOnClickListener { Toast.makeText(this@MainActivity, R.string.clicked_on_me, Toast.LENGTH_LONG).show() }

    }
}

data class Schedule(
    val copyright : String,
    val totalItems : Int,
    val totalEvents : Int,
    val totalGames : Int,
    val totalMatches : Int,
    val wait : Int,
    val dates : Array<String>
)

@Suppress("UNCHECKED_CAST")
fun <R> readInstanceProperty(instance: Any, propertyName: String): R {
    val property = instance::class.memberProperties
        // don't cast here to <Any, R>, it would succeed silently
        .first { it.name == propertyName } as KProperty1<Any, *>
    // force a invalid cast exception if incorrect type here
    return property.get(instance) as R
}