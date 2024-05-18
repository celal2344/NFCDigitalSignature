package com.example.nfc_dig_1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nfc_dig_1.databinding.ActivityNfcactivity1Binding

class NFCActivity1 : AppCompatActivity() {

    private lateinit var binding: ActivityNfcactivity1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNfcactivity1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val plainText = intent.getStringExtra("inputWord")
        val publicKey = MainActivity.KeyStore.keyPair?.public//send this through nfc

        println(publicKey.toString())

        binding.plainTextView.text = plainText.toString()
        binding.signatureTextView.text = publicKey.toString()
        val intent = Intent(this@CardActivity, HostCardEmulatorService::class.java)
        val inputData = "$plainText+$publicKey"
        intent.putExtra("input_data", inputData)
        startService(intent)

    }
}