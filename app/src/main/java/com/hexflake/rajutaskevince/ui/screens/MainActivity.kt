package com.hexflake.rajutaskevince.ui.screens

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hexflake.rajutaskevince.databinding.ActivityMainBinding
import com.hexflake.rajutaskevince.ui.adapters.PagingUsersListAdapter
import com.hexflake.rajutaskevince.utils.exceptionCoroutine
import com.hexflake.rajutaskevince.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }


    val adapterUserList by lazy {
        PagingUsersListAdapter { model ->
            startActivity((Intent(this, DetailsActivity::class.java)).apply {
                putExtra("model", model)
            })
        }
    }

    override fun onResume() {
        super.onResume()
        adapterUserList.refresh()
    }
    val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupUI()
        setupList()

mainViewModel.syncWithServer()

        //instructionn for tester
        Toast.makeText(
            this@MainActivity,
            "Scroll for loading next page",
            Toast.LENGTH_LONG
        ).show()



    }


    private fun setupUI() {
        with(binding) {
            lifecycleOwner = this@MainActivity
            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            recyclerView.adapter = adapterUserList

            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (!recyclerView.canScrollVertically(1)) loadNextPage()
                }
            })
        }
    }

    private fun loadNextPage() {
        val lastpage = adapterUserList.snapshot().items.last().page
        if (lastpage != null) mainViewModel.getNextPageFromServer(lastpage + 1)
    }

    private fun setupList() {
        lifecycleScope.launch(exceptionCoroutine) {
            mainViewModel.pagerUserList.collectLatest {
                adapterUserList.submitData(it)
            }
        }
    }
}