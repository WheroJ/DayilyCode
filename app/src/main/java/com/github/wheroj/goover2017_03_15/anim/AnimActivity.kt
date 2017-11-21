package com.github.wheroj.goover2017_03_15.anim

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.github.wheroj.goover2017_03_15.R

class AnimActivity : AppCompatActivity() {

    var targetView: View = null!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anim)

//        ObjectAnimator.ofFloat(targetView, "translationX", 0.0, 100.0)
    }
}
