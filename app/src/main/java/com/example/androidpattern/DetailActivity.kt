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
            detail_rating.text = "${Constants.RATING.capitalize()}: ${bundle.getString(Constants.RATING)}"
            detail_title.text = "${Constants.TITLE.capitalize()}: ${bundle.getString(Constants.TITLE)}"
            detail_year.text = "${Constants.YEAR.capitalize()}: ${bundle.getString(Constants.YEAR)}"
            detail_date.text = "${Constants.DATE.capitalize()}: ${bundle.getString(Constants.DATE)}"
        }

    }

    object Constants {
        const val RATING = "rating"
        const val TITLE = "title"
        const val YEAR = "year"
        const val DATE = "date"
    }
}