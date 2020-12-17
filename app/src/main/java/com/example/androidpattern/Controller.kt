package com.example.androidpattern

import android.content.Intent
import android.os.Bundle

class Controller {
    private lateinit var mView: MainActivity
    private lateinit var mModel: Model

    infix fun hasView(mainActivity: MainActivity) {
        mView = mainActivity
    }

    fun findAddress(address: String) {
        mView.showProgressBar()
        mModel.findAddress(address)
    }

    infix fun doWhenClickIsMadeOn(item: Model.ResultEntity) {
        var bundle = Bundle()
        bundle.putString(DetailActivity.Constants.RATING, item.rating)
        bundle.putString(DetailActivity.Constants.TITLE, item.title)
        bundle.putString(DetailActivity.Constants.YEAR, item.year)
        bundle.putString(DetailActivity.Constants.DATE, item.date)
        var intent = Intent(mView, DetailActivity::class.java)
        intent.putExtras(bundle)
        mView.startActivity(intent)
    }

    fun onStop() {
        mModel.stopLoadingTheList()
    }

    fun doWhenResultIsReady() {
        mView.hideProgressBar()
        mView.showResult()
    }

    fun doWhenThereIsErrorFetchingTheResult() {
        mView.hideProgressBar()
        mView.showError()
    }

    infix fun hasModel(mModel: Model) {
        this.mModel = mModel
    }

}