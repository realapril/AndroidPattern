package com.example.androidpattern


import android.content.Context
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
    private lateinit var mController: Controller
    private lateinit var mModel: Model
    private lateinit var addressAdapter: AddressAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mController = Controller()
        mModel = Model(mController)
        mController hasView this
        mController hasModel mModel
        loadView()
        respondToClicks()
    }

    private fun loadView() {
        setContentView(R.layout.activity_main)
        addressAdapter = AddressAdapter()
        main_activity_recyclerView.adapter = addressAdapter
    }

    private fun respondToClicks() {
        //검색버튼
        main_activity_button.setOnClickListener { mController.findAddress(main_activity_editText.text.toString()) }
        //리사이클러뷰 아이템
        addressAdapter setItemClickMethod {
            mController doWhenClickIsMadeOn it
        }
        addressAdapter.setItemShowMethod { fetchItemText(it) }
    }

    fun fetchItemText(it: Model.ResultEntity): String {
        return "${it.year}: ${it.title}"
    }

    override fun onStop() {
        super.onStop()
        mController.onStop()
    }

    private fun updateMovieList(t: List<Model.ResultEntity>) {
        addressAdapter.updateList(t)
        addressAdapter.notifyDataSetChanged()
    }

    fun showResult() {
        updateMovieList(mModel.mList)
    }

    fun showError() {
        showToast(this, "Error")
    }

    fun showProgressBar() {
        main_activity_progress_bar.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        main_activity_progress_bar.visibility = View.GONE
    }

    private fun showToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    class AddressAdapter : RecyclerView.Adapter<AddressAdapter.Holder>() {
        var mList: List<Model.ResultEntity> = arrayListOf()
        private lateinit var mOnClick: (item: Model.ResultEntity) -> Unit
        private lateinit var mOnShowItem: (item: Model.ResultEntity) -> String

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

        infix fun setItemClickMethod(onClick: (item: Model.ResultEntity) -> Unit) {
            this.mOnClick = onClick
        }

        infix fun setItemShowMethod(onShowItem: (item: Model.ResultEntity) -> String) {
            this.mOnShowItem = onShowItem
        }

        fun updateList(list: List<Model.ResultEntity>) {
            mList = list
        }

        class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }
}