package com.hirauchi.countdown.activity

import android.os.Bundle
import com.hirauchi.countdown.R
import com.hirauchi.countdown.fragment.AppInfoFragment

class AppInfoActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_info)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.app_info_title)

        if (savedInstanceState == null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, AppInfoFragment()).commit()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}