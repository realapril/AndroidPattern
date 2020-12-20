package com.example.androidpattern.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidpattern.MainModel
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.subjects.PublishSubject
import retrofit2.HttpException

class MainViewModel () : ViewModel() {
    val resultListObservable = MutableLiveData<List<String>>()
    val resultListErrorObservable = MutableLiveData<HttpException>()
    private val itemObservable = MutableLiveData<MainModel.ResultEntity>()
    fun getResultListObservable(): LiveData<List<String>> = resultListObservable
    fun getResultListErrorObservable(): LiveData<HttpException> = resultListErrorObservable
    fun getItemObservable(): LiveData<MainModel.ResultEntity> = itemObservable
    lateinit var entityList: List<MainModel.ResultEntity>
    lateinit var mainModel: MainModel
    private val schedulersWrapper = SchedulersWrapper()

    constructor(mMainModel: MainModel) : this() {
        mainModel = mMainModel
    }

    fun findAddress(address: String) {
        mainModel.fetchAddress(address)!!.subscribeOn(schedulersWrapper.io()).observeOn(schedulersWrapper.main()).subscribeWith(object : DisposableSingleObserver<List<MainModel.ResultEntity>?>() {
            override fun onSuccess(t: List<MainModel.ResultEntity>) {
                entityList = t
                resultListObservable.postValue(fetchItemTextFrom(t))
            }

            override fun onError(e: Throwable) {
                resultListErrorObservable.postValue(e as HttpException)
            }
        })
    }

    private fun fetchItemTextFrom(it: List<MainModel.ResultEntity>): ArrayList<String> {
        val li = arrayListOf<String>()
        for (resultEntity in it) {
            li.add("${resultEntity.year}: ${resultEntity.title}")
        }
        return li
    }

    fun doOnItemClick(position: Int) {
        itemObservable.value = entityList[position]
    }
}