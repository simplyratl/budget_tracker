package com.example.budget_tracker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.budget_tracker.databinding.ActivityMainBinding
import com.example.budget_tracker.ui.home.AddTransactionActivity
import com.example.budget_tracker.ui.home.TransactionsViewModel
import com.example.budget_tracker.ui.scanner.ScannerActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var transactionViewModel:TransactionsViewModel

    private lateinit var binding: ActivityMainBinding

    private lateinit var startBtn:FloatingActionButton
    private lateinit var cameraBtn:FloatingActionButton
    private lateinit var addBtn:FloatingActionButton
    private val rotateOpen by lazy { AnimationUtils.loadAnimation(this, R.anim.floating_rotate_open_anim) }
    private val rotateClose by lazy { AnimationUtils.loadAnimation(this, R.anim.floating_rotate_close_anim) }
    private val fromBottom by lazy { AnimationUtils.loadAnimation(this, R.anim.floating_from_bottom_anim) }
    private val toBottom by lazy { AnimationUtils.loadAnimation(this, R.anim.floating_to_bottom_anim) }

    private var floatingOpened = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        transactionViewModel = ViewModelProvider(this)[TransactionsViewModel::class.java]

        val navView: BottomNavigationView = binding.navView
        startBtn = findViewById(R.id.floating_start_btn)
        addBtn = findViewById(R.id.floating_add_btn)
        cameraBtn = findViewById(R.id.floating_camera_btn)

        startBtn.setOnClickListener{
            onAddButtonClicked(floatingOpened)
        }

        addBtn.setOnClickListener{
            val intent = Intent(this@MainActivity, AddTransactionActivity::class.java)
            startActivity(intent)
        }

        cameraBtn.setOnClickListener{
            val intent = Intent(this@MainActivity, ScannerActivity::class.java)
            startActivity(intent)
        }

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun onAddButtonClicked(clicked:Boolean){
        setVisibility(clicked)
        setAnimation(clicked)

        floatingOpened = !clicked
    }

    private fun setVisibility(clicked: Boolean){
        if(!clicked){
            cameraBtn.visibility = View.VISIBLE
            addBtn.visibility = View.VISIBLE
        } else{
            cameraBtn.visibility = View.INVISIBLE
            addBtn.visibility = View.INVISIBLE
        }
    }

    private fun setAnimation(clicked:Boolean){
        if(!clicked){
            cameraBtn.startAnimation(fromBottom)
            addBtn.startAnimation(fromBottom)
            startBtn.startAnimation(rotateOpen)
        } else{
            cameraBtn.startAnimation(toBottom)
            addBtn.startAnimation(toBottom)
            startBtn.startAnimation(rotateClose)
        }
    }
}
