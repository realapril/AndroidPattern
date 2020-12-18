package com.example.androidpattern

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item.view.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class MainActivity : AppCompatActivity() {
    private lateinit var mMainPresenter: MainPresenter
    private lateinit var addressAdapter: AddressAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMainPresenter = MainPresenter(MainModel())
        mMainPresenter hasView this
        loadView()
        respondToClicks()
    }

    private fun loadView() {
        setContentView(R.layout.activity_main)
        addressAdapter = AddressAdapter()
        main_activity_recyclerView.adapter = addressAdapter
    }

    private fun respondToClicks() {
        main_activity_button.setOnClickListener { mMainPresenter.findAddress(main_activity_editText.text.toString()) }
        addressAdapter setItemClickMethod {
            mMainPresenter doWhenItemIsClicked it
        }
        addressAdapter.setItemShowMethod { mMainPresenter fetchItemTextFrom it }
    }

    fun showProgressBar() {
        main_activity_progress_bar.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        main_activity_progress_bar.visibility = View.GONE
    }

    fun showErrorMessage(errorMsg: String) {
        Toast.makeText(this, "Error retrieving data: $errorMsg", Toast.LENGTH_SHORT).show()
    }

    fun updateMovieList(t: List<MainModel.ResultEntity>) {
        addressAdapter.updateList(t)
        addressAdapter.notifyDataSetChanged()
    }

    override fun onStop() {
        super.onStop()
        mMainPresenter.onStop()
    }

    fun goToDetailActivity(item: MainModel.ResultEntity) {
        var bundle = Bundle()
        bundle.putString(DetailActivity.Constants.RATING, item.rating)
        bundle.putString(DetailActivity.Constants.TITLE, item.title)
        bundle.putString(DetailActivity.Constants.YEAR, item.year)
        bundle.putString(DetailActivity.Constants.DATE, item.date)
        var intent = Intent(this, DetailActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    class AddressAdapter : RecyclerView.Adapter<AddressAdapter.Holder>() {
        var mList: List<MainModel.ResultEntity> = arrayListOf()
        private lateinit var mOnClick: (item: MainModel.ResultEntity) -> Unit
        private lateinit var mOnShowItem: (item: MainModel.ResultEntity) -> String

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item, parent, false)
            return Holder(view)
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            holder.itemView.item_textView.text = mOnShowItem(mList[position])
            holder.itemView.setOnClickListener { mOnClick(mList[position]) }
        }

        override fun getItemCount(): Int {
            return mList.size
        }

        infix fun setItemClickMethod(onClick: (item: MainModel.ResultEntity) -> Unit) {
            this.mOnClick = onClick
        }

        infix fun setItemShowMethod(onShowItem: (item: MainModel.ResultEntity) -> String) {
            this.mOnShowItem = onShowItem
        }

        fun updateList(list: List<MainModel.ResultEntity>) {
            mList = list
        }

        class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

}