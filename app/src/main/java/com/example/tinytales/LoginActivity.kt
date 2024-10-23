package com.example.tinytales

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tinytales.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    // viewbinding
    private lateinit var binding: ActivityLoginBinding

    // firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    // progress dialog
    private lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        // firestore
        val db = FirebaseFirestore.getInstance()
        val usersCollection = db.collection("users")

        // init progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.registerTV.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // handle click, begin login
        binding.loginButton.setOnClickListener{
            /* Step
            1. input data
            2. validate data
            3. login - firebase auth
            4. check user type
                if User - go to UserDashBoardActivity
                if Admin - go to AdminDashBoardActivity
            */
            validateData()
        }

    }

    private var email = ""
    private var password = ""

    private fun validateData() {
        // 1. input data
        email = binding.emailET.text.toString().trim()
        password = binding.passwordET.text.toString().trim()

        // 2. validate data

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this,"Invalid email format", Toast.LENGTH_SHORT).show()
        }
        else if (password.isEmpty()){
            Toast.makeText(this,"Please type password", Toast.LENGTH_SHORT).show()
        } else
            loginUser()
    }

    private fun loginUser() {
        // Hiển thị trạng thái
        progressDialog.setMessage("Logging in....")
        progressDialog.show()

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                // Kiểm tra nếu người dùng chọn "Remember Me"
                if (binding.checkBox.isChecked) {
                    // Lưu UID vào SharedPreferences
                    val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                    val editor = sharedPref.edit()
                    editor.putString("USER_ID", firebaseAuth.currentUser?.uid)
                    editor.putBoolean("REMEMBER_ME", true)
                    editor.apply()
                }
                checkUser()
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this, "Login failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


    private fun checkUser() {
        // Hiển thị trạng thái
        progressDialog.setMessage("Checking User")

        val firebaseUser = firebaseAuth.currentUser!!
        val userId = firebaseUser.uid

        // Truy vấn Firestore với UID của người dùng
        val db = FirebaseFirestore.getInstance()
        db.collection("User").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // Lấy thông tin người dùng từ Firestore
                    val user = document.toObject(User::class.java)
                    progressDialog.dismiss()

                    // Chuyển hướng tới DashbroadUserActivity
                    startActivity(Intent(this@LoginActivity, DashbroadUserActivity::class.java))
                    finish()
                } else {
                    progressDialog.dismiss()
                    Toast.makeText(this, "User not found in Firestore", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this, "Error checking user: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

}


