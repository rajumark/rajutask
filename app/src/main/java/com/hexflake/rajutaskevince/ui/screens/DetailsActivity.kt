package com.hexflake.rajutaskevince.ui.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.hexflake.rajutaskevince.database.UserModel
import com.hexflake.rajutaskevince.databinding.ActivityDetailsBinding
import com.hexflake.rajutaskevince.service.startDownloadService
import com.hexflake.rajutaskevince.utils.click
import com.hexflake.rajutaskevince.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityDetailsBinding.inflate(layoutInflater)
    }

    val mainViewModel by viewModels<MainViewModel>()

    /**
     * Recive data from previews screen
     * */
    val modelUser by lazy {
        intent.takeIf { it.hasExtra("model") }?.getSerializableExtra("model") as? UserModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupUI()
    }

    private fun setupUI() {
        with(binding) {


            //set lifecycler for binding object to safly data update by binding adapter
            lifecycleOwner = this@DetailsActivity

            //setup data to ui from model
            modelUser?.let {
                //setup data to ui
                model = it

                //for download image
                it.avatar?.let { image ->
                    download click {
                        Toast.makeText(
                            this@DetailsActivity,
                            "Look in Notification",
                            Toast.LENGTH_SHORT
                        ).show()
                        startDownloadService(image)
                    }
                }

                //delete user actin
                deleteuser click {
                    mainViewModel.deleteUserFromServer(it)
                    mainViewModel.deleteUserFromRoomDB(it){
                        Toast.makeText(
                            this@DetailsActivity,
                            "${it.first_name} Deleted",
                            Toast.LENGTH_SHORT
                        ).show()
                        //after user deleted details screen is no more so closing
                        finish()
                    }
                }
            }
        }
    }
}