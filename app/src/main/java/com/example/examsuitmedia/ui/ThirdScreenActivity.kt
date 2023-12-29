package com.example.examsuitmedia.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.examsuitmedia.R
import com.example.examsuitmedia.adapter.ListAdapter
import com.example.examsuitmedia.databinding.ActivityThirdScreenBinding
import com.example.examsuitmedia.retrofit.ApiConfig
import com.example.examsuitmedia.retrofit.ApiService
import com.example.examsuitmedia.retrofit.DataItem
import com.example.examsuitmedia.retrofit.Response
import retrofit2.Call
import javax.security.auth.callback.Callback

class ThirdScreenActivity : AppCompatActivity(), (DataItem) -> Unit {

    private lateinit var binding: ActivityThirdScreenBinding
    private lateinit var apiService: ApiService
    private lateinit var listAdapter: ListAdapter
    private var currentPage = 1
    private var isLastPage = false
    private var isLoading = false
    private val userList = mutableListOf<DataItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityThirdScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiService = ApiConfig.getApiService()

        listAdapter = ListAdapter(userList, this@ThirdScreenActivity)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ThirdScreenActivity)
            adapter = listAdapter
        }

        fetchData()
        setRecyclerViewScrollListener()
    }

    override fun onResume() {
        super.onResume()
        fetchData() // Panggil kembali fetchData() di sini untuk memperbarui data ketika kembali dari SecondScreenActivity
    }

    override fun invoke(user: DataItem) {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("SELECTED_USER_NAME", "${user.firstName} ${user.lastName}")
        editor.apply()
        setResult(Activity.RESULT_OK)
        finish()
    }


    private fun setRecyclerViewScrollListener() {
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= 10 // assumed page size
                    ) {
                        isLoading = true
                        currentPage++
                        fetchData()
                    }
                }
            }
        })
    }

    private fun fetchData() {
        binding.progressBar.visibility = View.VISIBLE

        val apiService = ApiConfig.getApiService()
        val call: Call<Response> = apiService.getUsers(currentPage, 10)

        call.enqueue(object : retrofit2.Callback<Response> {
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.data?.let { users ->
                        userList.addAll(users.filterNotNull())
                        listAdapter.notifyDataSetChanged()
                        if (currentPage >= responseBody.totalPages ?: 0) {
                            isLastPage = true
                        }
                    }
                }
                binding.progressBar.visibility = View.GONE
            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                // Handle failure case
                binding.progressBar.visibility = View.GONE
            }
        })
    }

    companion object {
        const val USER_NAME = "SHOW_NAME"
    }

}

