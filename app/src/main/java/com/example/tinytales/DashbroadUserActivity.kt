package com.example.tinytales

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.tinytales.databinding.ActivityDashbroadUserBinding
import com.google.firebase.auth.FirebaseAuth

class DashbroadUserActivity : AppCompatActivity() {

    lateinit var binding: ActivityDashbroadUserBinding
    private lateinit var auth: FirebaseAuth
    private var backPressedTime: Long = 0
    private lateinit var backToast: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashbroadUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // Kiểm tra nếu có extra được truyền từ BookDetailActivity
        val openCart = intent.getBooleanExtra("open_cart", false)

        // Nếu open_cart là true, mở CartFragment
        if (openCart) {
            if (isUserLoggedIn()) {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frame_dashbroad, CartFragment())
                    commit()
                }
            } else {
                showLoginDialog()
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frame_dashbroad, HomeFragment())
                    commit()
                }
            }
        } else {
            // Mặc định mở HomeFragment
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frame_dashbroad, HomeFragment())
                commit()
            }
        }

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.Home -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.frame_dashbroad, HomeFragment())
                        commit()
                    }
                }

                R.id.Category -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.frame_dashbroad, CategoryFragment())
                        commit()
                    }
                }

                R.id.Cart -> {
                    if (isUserLoggedIn()) {
                        supportFragmentManager.beginTransaction().apply {
                            replace(R.id.frame_dashbroad, CartFragment())
                            commit()
                        }
                    } else {
                        showLoginDialog()
                    }
                }

                R.id.Account -> {
                    if (isUserLoggedIn()) {
                        supportFragmentManager.beginTransaction().apply {
                            replace(R.id.frame_dashbroad, AccountFragment())
                            commit()
                        }
                    } else {
                        showLoginDialog()
                    }
                }
            }
            true
        }
    }

    // Xử lý nút back
    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.frame_dashbroad)

        when (currentFragment) {
            is HomeFragment -> {
                // Nếu đang ở HomeFragment, hiển thị thông báo thoát app
                handleBackPressForHome()
            }
            else -> {
                // Nếu đang ở fragment khác, quay về HomeFragment
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frame_dashbroad, HomeFragment())
                    commit()
                }
                // Cập nhật bottom navigation
                binding.bottomNavigation.selectedItemId = R.id.Home
            }
        }
    }

    // Xử lý back press khi đang ở HomeFragment
    private fun handleBackPressForHome() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            // Nếu ấn back lần thứ 2 trong vòng 2 giây, thoát app
            backToast.cancel()
            super.onBackPressed()
            finishAffinity() // Thoát hoàn toàn app
        } else {
            // Lần đầu ấn back, hiển thị thông báo
            backToast = Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT)
            backToast.show()
        }
        backPressedTime = System.currentTimeMillis()
    }

    // Kiểm tra người dùng đã đăng nhập chưa
    private fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    // Hiển thị dialog yêu cầu đăng nhập
    private fun showLoginDialog() {
        AlertDialog.Builder(this)
            .setTitle("Login required")
            .setMessage("You need to login to add products to cart. Do you want to login?")
            .setPositiveButton("Login") { _, _ ->
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
            .setNegativeButton("Sign up") { _, _ ->
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }
            .setNeutralButton("Cancel", null)
            .setCancelable(true)
            .show()
    }
}
