package com.example.nfc_dig_1

import android.content.Intent
import android.nfc.NfcAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import com.example.nfc_dig_1.databinding.ActivityNfcactivity1Binding
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec

class NFCActivity1 : AppCompatActivity() {

    private lateinit var binding: ActivityNfcactivity1Binding
    private var stringList: String? = null
    private var signature: ByteArray? = byteArrayOf()
    private var publicKeyBytes: ByteArray? = byteArrayOf()

    @OptIn(ExperimentalStdlibApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNfcactivity1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        //check nfc
        val nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC is not available on this device", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        if (!nfcAdapter!!.isEnabled) {
            Toast.makeText(this, "Please enable NFC", Toast.LENGTH_SHORT).show()
        }
        //receive plaintext string list and signature
        stringList = intent.getStringExtra("stringList")
        signature = intent.getByteArrayExtra("signature")
        publicKeyBytes = intent.getByteArrayExtra("publicKeyBytes")

        //display words to be sent
        var wordListText = "Signed word: ${stringList?.split(" ".toRegex())?.get(0)}\nRandom words:"
        binding.signatureText.text = "Signature: ${signature?.toHexString()}"
        stringList?.split(" ".toRegex())?.slice(1 until 10)?.forEach { element ->
            wordListText += "\n -$element"
        }
        binding.plainTextView.text = wordListText

        //send the string list and the signature to hce service
        val intent = Intent(this@NFCActivity1, HostCardEmulatorService::class.java).apply {
            putExtra("stringList", stringList)
            putExtra("signature", signature)
            putExtra("publicKeyBytes", publicKeyBytes)
        }
        startService(intent)

        //continue when the button is pressed
        binding.button.setOnClickListener {
            val i = Intent(this@NFCActivity1, NFCActivity2::class.java)
            i.putExtra("publicKey",publicKeyBytes?.toHexString())
            startActivity(i)
            val intent = Intent("com.example.UPDATE_BOOLEAN")
            intent.putExtra("boolean_value", true) // or false, depending on your need
            sendBroadcast(intent)
        }

    }
}