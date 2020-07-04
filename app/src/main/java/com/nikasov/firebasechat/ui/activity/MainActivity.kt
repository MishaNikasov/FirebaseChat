package com.nikasov.firebasechat.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nikasov.firebasechat.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }
}