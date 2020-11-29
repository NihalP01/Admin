package com.example.admin.Storage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.admin.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        GlobalScope.launch(Dispatchers.Default) {
            var db =
                Room.databaseBuilder(applicationContext, TokenDB::class.java, "Token DB").build()
            val token = TokenEntity()
            token.token = "Another one"
            db.tokenDao().saveToken(token)
            db.tokenDao().readToken().forEach {

            }
        }
    }
}