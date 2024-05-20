package com.example.nfc_dig_1

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nfc_dig_1.databinding.ActivityNfcactivity2Binding

class NFCActivity2 : AppCompatActivity() {
    private lateinit var binding : ActivityNfcactivity2Binding
    @OptIn(ExperimentalStdlibApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNfcactivity2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.publicKeyText.text = intent.getStringExtra("publicKey")
        startService(Intent(this@NFCActivity2, HostCardEmulatorService::class.java))
    }
}