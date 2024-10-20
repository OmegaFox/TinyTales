package com.example.tinytales

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.tinytales.databinding.ActivityDashbroadUserBinding

class DashbroadUserActivity : AppCompatActivity() {

    lateinit var binding: ActivityDashbroadUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashbroadUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Kiểm tra nếu có extra được truyền từ BookDetailActivity
        val openCart = intent.getBooleanExtra("open_cart", false)



        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_dashbroad, HomeFragment())
            commit()
        }

        // Nếu open_cart là true, mở CartFragment
        if (openCart) {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frame_dashbroad, CartFragment())
                commit()
            }
        } else {
            // Mặc định mở HomeFragment nếu không có yêu cầu mở CartFragment
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frame_dashbroad, HomeFragment())
                commit()
            }
        }

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.Home -> supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frame_dashbroad, HomeFragment())
                    commit()
                }

                R.id.Category -> supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frame_dashbroad, CategoryFragment())
                    commit()
                }

                R.id.Cart -> supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frame_dashbroad, CartFragment())
                    commit()
                }

                R.id.Account -> supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frame_dashbroad, AccountFragment())
                    commit()
                }
            };true
        }
    }
}