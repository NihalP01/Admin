package com.example.admin

import Logged
import UserPrefManager
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.admin.Network.ApiAdapter
import com.example.admin.Storage.TokenDB
import kotlinx.android.synthetic.main.login_ui.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserLogin : AppCompatActivity() {

    private lateinit var userPrefManager: UserPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_ui)

        userPrefManager = UserPrefManager(applicationContext)

        btnLogin.setOnClickListener {
            val email = userEmail.text.toString().trim()
            val password = userPassword.text.toString().trim()

            if (email.isEmpty()) {
                userEmail.error = "Please enter your email"
                userEmail.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                userPassword.error = "Please enter your password"
                userPassword.requestFocus()
                return@setOnClickListener
            }

            GlobalScope.launch(Dispatchers.Main) {
                try {
                    val response = ApiAdapter.apiClient.login(email, password)
                    if (response.isSuccessful && response.body() != null) {
                        lifecycleScope.launch(Dispatchers.Default) {
                            userPrefManager.setLogged(Logged.IN)
                            observerUser()
                            /* var db = Room.databaseBuilder(
                                applicationContext,
                                TokenDB::class.java,
                                "Token DB"
                            ).build()
                            val token = TokenEntity()
                            token.token = response.body()!!.token
                            db.tokenDao().saveToken(token)
                            val userToken = db.tokenDao().readToken()
                            userToken.forEach {
                                Log.d("myTag", it.token)
                            }*/
                        }
                        startActivity(Intent(this@UserLogin, MainActivity::class.java))
                    } else {
                        Toast.makeText(this@UserLogin, response.message(), Toast.LENGTH_SHORT)
                            .show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@UserLogin, e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun observerUser() {
        userPrefManager.loggedInflow.asLiveData().observe(this, Observer { logged ->
            when (logged) {
                Logged.IN -> doAfterLoggedIn()
                Logged.OUT -> doAfterLoggedOut()
            }
        })
    }

    private fun doAfterLoggedIn() {
        //startActivity(null)
        GlobalScope.launch {
            val i =
                Room.databaseBuilder(applicationContext, TokenDB::class.java, "Token DB").build()
                    .tokenDao().readToken()
            runOnUiThread {
                Toast.makeText(this@UserLogin, "Login success total ${i.size}", Toast.LENGTH_LONG)
                    .show()
                startActivity(Intent(this@UserLogin, MainActivity::class.java)).also {
                    this@UserLogin.finish()
                }
            }
        }

    }

    private fun doAfterLoggedOut() {

    }
}

