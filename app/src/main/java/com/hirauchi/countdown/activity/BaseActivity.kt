package com.hirauchi.countdown.activity

import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import android.widget.RelativeLayout
import androidx.annotation.LayoutRes
import com.hirauchi.countdown.R

abstract class BaseActivity : AppCompatActivity() {

    lateinit var mAdView : AdView

    override fun setContentView(@LayoutRes layoutResID: Int) {
        val container = layoutInflater.inflate(R.layout.activity_base, null) as RelativeLayout
        val activityContent = container.findViewById(R.id.layout_container) as FrameLayout
        layoutInflater.inflate(layoutResID, activityContent, true)
        super.setContentView(container)

        MobileAds.initialize(this) {}
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }
}