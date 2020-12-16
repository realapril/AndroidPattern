package com.example.androidpattern

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = intent.extras
        setContentView(R.layout.activity_detail)
        bundle?.let {
            detail_rating.text = "${MainActivity.Constants.RATING.capitalize()}: ${it.getString(MainActivity.Constants.RATING)}"
            detail_title.text = "${MainActivity.Constants.TITLE.capitalize()}: ${it.getString(MainActivity.Constants.TITLE)}"
            detail_year.text = "${MainActivity.Constants.YEAR.capitalize()}: ${it.getString(MainActivity.Constants.YEAR)}"
            detail_date.text = "${MainActivity.Constants.DATE.capitalize()}: ${it.getString(MainActivity.Constants.DATE)}"
        }

    }
}