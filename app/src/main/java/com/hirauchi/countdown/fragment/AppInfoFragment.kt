package com.hirauchi.countdown.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.hirauchi.countdown.R

class AppInfoFragment : Fragment() {

    private lateinit var mContext: Context

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_app_info, container, false)
    }

    override fun onAttach(context: Context){
        mContext = context
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listView = view.findViewById<ListView>(R.id.list_view)
        val itemList = listOf(getString(R.string.app_info_privacy), getString(R.string.app_info_oss), getString(R.string.app_info_version))
        val adapter = ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, itemList)
        listView.adapter = adapter

        val version = mContext.packageManager?.getPackageInfo(mContext.packageName, 0)?.versionName
        listView.setOnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://hirauchi-genta.com/privacy")))
                1 -> startActivity(Intent(mContext, OssLicensesMenuActivity::class.java))
                2 -> Toast.makeText(mContext, version, Toast.LENGTH_SHORT).show()
            }
        }
    }
}