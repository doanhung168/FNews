package com.poly_team.fnews.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.poly_team.fnews.R
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            for (fragment in supportFragmentManager.fragments) {
                val subFragments = fragment.childFragmentManager.fragments
                for(subFragment in subFragments) {
                    subFragment.onActivityResult(requestCode, resultCode, data)
                }
            }
        } catch (e: Exception) {
            Log.d("ERROR", e.toString())
        }
    }
}