package com.example.nhltracker

// dynamic textview

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.URL
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties


class MainActivity : AppCompatActivity() {

    private val gson = Gson()
    lateinit var scheduleJSON : String
    lateinit var schedule : Schedule
    lateinit var appContext : Context
    lateinit var linearLayout: LinearLayout
    lateinit var listSchedule: ListView
    lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Display the toolbar
        setSupportActionBar(findViewById(R.id.toolbar))

        appContext = applicationContext

        // Get data from StartupActivity
        scheduleJSON = intent.getStringExtra("schedule")
        schedule = gson.fromJson(scheduleJSON, Schedule::class.java)

        // for creating TextViews programmatically, not in use
        linearLayout = findViewById<LinearLayout>(R.id.linear_main)
        listSchedule = findViewById(R.id.scheduleListView) as ListView

        textView = findViewById(R.id.rowx)
        textView.visibility = View.GONE
        textView.typeface = ResourcesCompat.getFont(appContext, R.font.aldrich)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    // Menu actions
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.action_refresh -> {
                onResume()
                true
            }
            R.id.action_settings -> {

                // settings or statistics, whatever is available in the api
                runOnUiThread() {
                    Toast.makeText(
                        applicationContext, "no implementation", Toast.LENGTH_SHORT
                    ).show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Refresh screen
    override fun onResume() {
        super.onResume()
        var resultJSON = ""

        GlobalScope.launch { // launch a new coroutine in background and continue

            try {
                resultJSON =
                    URL("https://statsapi.web.nhl.com/api/v1/schedule").readText()

            } catch (e: Exception) {
            }
            runOnUiThread(Runnable {
                if (resultJSON == "") {
                    Toast.makeText(
                        applicationContext, "data connection failed", Toast.LENGTH_SHORT
                    ).show()
                }
                else {
                    Toast.makeText(
                        applicationContext, "data retrieved", Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }

        schedule = gson.fromJson(scheduleJSON, Schedule::class.java)
        displayJsonOnScreen(appContext, linearLayout, schedule, listSchedule, textView)
    }
}

data class Schedule(
    val copyright : String,
    val totalItems : Int,
    val totalEvents : Int,
    val totalGames : Int,
    val totalMatches : Int,
    val wait : Int,
    val dates : List<String>
)

fun displayJsonOnScreen(appContext: Context, linearLayout : LinearLayout, schedule: Schedule, listSchedule : ListView, textView: TextView) {

    var list = ArrayList<String>()

    var adapter
            = ArrayAdapter(appContext, R.layout.row, list)
    listSchedule.adapter = adapter

    var i = 0
    for ( prop in Schedule::class.memberProperties) {

        list.add("${prop.name}: ${prop.get(schedule)}")

        listSchedule.alpha = 0.70F
        adapter.notifyDataSetChanged()

/*
        //var currentBGColor = R.color.dataTextBGColor2
        //var tvBackgroundLight = false
        //i++
        // TO BE CONTINUED..
        //adapter.add(textView.text.toString())
        //adapter.getView(i, textView, listSchedule)

        var tv = listSchedule?.getChildAt(i)
        if (tv is TextView) {
            tv.typeface = ResourcesCompat.getFont(appContext, R.font.aldrich)
        }

        //textView.setTextColor(appContext.getResources().getColor(R.color.dataTextColor))
        //textView.typeface = ResourcesCompat.getFont(appContext, R.font.aldrich)

        // Dynamically control size of textview
        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(textView,
            20, 26, 1,
            TypedValue.COMPLEX_UNIT_SP)

        //textView.id = i
        //println(i)
        //textView.gravity = Gravity.CENTER_VERTICAL
        //adapter.getItem(i).gravity = Gravity.CENTER_VERTICAL
        //
        //adapter.getItem(i).setTextColor(appContext.getResources().getColor(R.color.dataTextColor))
        //
        //adapter.getItem(i).text = "${prop.name}: ${prop.get(schedule)}"
        //textView.layoutParams.height = 140
        //textView.typeface = ResourcesCompat.getFont(appContext, R.font.aldrich)
        //adapter.getItem(i).typeface = ResourcesCompat.getFont(appContext, R.font.aldrich)

        // Alternate BG color, no effect in this version
        if ( tvBackgroundLight ) {
            currentBGColor = R.color.dataTextBGColor2
            tvBackgroundLight = false
        }
        else {
            currentBGColor = R.color.dataTextBGColor1
            tvBackgroundLight = true
        }

        textView.setBackgroundResource(currentBGColor)
*/
    }
    linearLayout.invalidate()
}
