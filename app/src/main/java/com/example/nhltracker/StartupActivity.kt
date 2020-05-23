package com.example.nhltracker

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import kotlinx.coroutines.*
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.net.URL
import android.widget.Toast

class StartupActivity : AppCompatActivity() {

    private var logo: ImageView? = null
    private var text: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup)

        logo = findViewById<ImageView>(R.id.nhl_logo)
        text = findViewById<TextView>(R.id.textAppName)

        var resultJSON = ""

        // Retrieve data from API
        GlobalScope.launch { // launch a new coroutine in background and continue
            try {
                resultJSON =
                    URL("https://statsapi.web.nhl.com/api/v1/schedule").readText()
                //println(resultJSON) // print after delay
            }
            catch (e : Exception){
            }
            delay(5500L) // non-blocking delay for 1 second (default time unit is ms)

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

            // Switch activity to display data
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.putExtra("schedule", resultJSON)
            startActivity(intent)
        }
        // Store data to a collection or DB
        fadeAnimation()
    }

    private fun fadeAnimation() {
        val fadeInLogo = ObjectAnimator.ofFloat(logo, View.ALPHA, 0.0f, 1.0f)
        val fadeInText = ObjectAnimator.ofFloat(text, View.ALPHA, 0.0f, 1.0f)
        val fadeOutLogo = ObjectAnimator.ofFloat(logo, View.ALPHA, 1.0f, 0.0f)
        val fadeOutText = ObjectAnimator.ofFloat(text, View.ALPHA, 1.0f, 0.0f)

        fadeInLogo.duration = 2000
        fadeOutLogo.duration = 2000
        fadeInText.duration = 2000
        fadeOutText.duration = 2000

        //fadeOutText.repeatCount = 2

        var anim = AnimatorSet()
        var anim1 = AnimatorSet()
        var anim2 = AnimatorSet()
        //anim1.playTogether(fadeInLogo, fadeInText)
        //anim2.playTogether(fadeOutLogo, fadeOutText)
        anim1.play(fadeInLogo).with(fadeInText)
        anim2.play(fadeOutLogo).with(fadeOutText)

        //var animations = listOf<Animator>(anim, anim)
        //anim.playSequentially(anim, anim)

        anim.play(anim1).before(anim2)
        anim.startDelay = 250
        anim.start()

        anim.addListener(object: Animator.AnimatorListener{
            override fun onAnimationRepeat(p0: Animator?) {
            }

            override fun onAnimationCancel(p0: Animator?) {
            }

            override fun onAnimationEnd(p0: Animator?) {
                anim.start()
                anim.removeAllListeners()
            }

            override fun onAnimationStart(p0: Animator?) {
            }
        })
    }
}
