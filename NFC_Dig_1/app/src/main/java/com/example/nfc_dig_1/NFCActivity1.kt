package com.example.nfc_dig_1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import com.example.nfc_dig_1.databinding.ActivityNfcactivity1Binding
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec

class NFCActivity1 : AppCompatActivity() {

    private lateinit var binding: ActivityNfcactivity1Binding
    private var stringList: String? = null
    private var signature: ByteArray? = byteArrayOf()
//    private var publicKey: PublicKey? = null
    @OptIn(ExperimentalStdlibApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNfcactivity1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        stringList = intent.getStringExtra("stringList")
        signature = intent.getByteArrayExtra("signature")
//        val publicKeyBytes = intent.getByteArrayExtra("public_key")
//        publicKeyBytes?.let { bytes ->
//            val keySpec = X509EncodedKeySpec(bytes)
//            val keyFactory = KeyFactory.getInstance("RSA")
//            publicKey = keyFactory.generatePublic(keySpec)
//        }
        var wordListText = "Signed word: ${stringList?.split(" ".toRegex())?.get(0)}\nRandom words:"
        stringList?.split(" ".toRegex())?.slice(1 until 10)?.forEach { element ->
            wordListText += "\n -$element"
        }
        binding.plainTextView.text = wordListText
//        binding.signatureTextView.text = "Public key: ${Base64.encodeToString(publicKey?.encoded, 2);}"
        val intent = Intent(this@NFCActivity1, HostCardEmulatorService::class.java).apply {
            putExtra("stringList", stringList)
            putExtra("signature", signature)
        }
        startService(intent)

    }
}