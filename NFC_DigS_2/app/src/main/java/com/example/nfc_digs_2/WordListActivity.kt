package com.example.nfc_digs_2

import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Color
import androidx.core.view.children
import com.example.nfc_digs_2.databinding.ActivityWordListBinding
import java.security.KeyFactory
import java.security.PublicKey
import java.security.Signature
import java.security.spec.X509EncodedKeySpec

class WordListActivity : AppCompatActivity() , NfcAdapter.ReaderCallback{

    private lateinit var binding: ActivityWordListBinding
    private lateinit var nfcAdapter: NfcAdapter
    private lateinit var adapter : ArrayAdapter<String>
    private lateinit var receivedWord :String
    private lateinit var randomWords :List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWordListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC is not available on this device", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        if (!nfcAdapter!!.isEnabled) {
            Toast.makeText(this, "Please enable NFC", Toast.LENGTH_SHORT).show()
        }
        binding.button.setOnClickListener{
            enableNFC()
        }
        val receivedWord = intent.getStringExtra("stringList")
        val signatureBytes = intent.getByteArrayExtra("signature")
        println("Received signature $signatureBytes")
        randomWords = receivedWord?.split(" ")?.shuffled()!!
//        var randomWords = (getRandomWords(9) + receivedWord).shuffled()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, randomWords)
        binding.wordList.adapter = adapter
        binding.button.setOnClickListener {
            enableNFC()

        }
    }
    private fun verifySignature(data: ByteArray, signatureBytes: ByteArray, publicKey: PublicKey): Boolean {
        val signature = Signature.getInstance("SHA256withRSA")
        signature.initVerify(publicKey)
        signature.update(data)
        return signature.verify(signatureBytes)
    }
    private fun highlightCorrectWord(receivedWord:String, randomWords: List<String>){
        var index = randomWords.indexOf(receivedWord)
        println(binding.wordList.children.toList().toString())
        binding.wordList.getChildAt(index).setBackgroundColor(Color.Green.hashCode())
    }
    private fun enableNFC() {
        if (nfcAdapter != null) {
            // Enable foreground dispatch to receive NFC events
            nfcAdapter?.enableReaderMode(this, this,
                NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK,
                null)
        }
    }
    public override fun onResume() {
        super.onResume()
    }
    public override fun onPause() {
        super.onPause()
        nfcAdapter?.disableReaderMode(this)
    }
    override fun onTagDiscovered(tag: Tag?) {
        try{
            val isoDep = IsoDep.get(tag)
            isoDep.connect()
            isoDep.timeout = 2000
            val response = isoDep.transceive("00A4040007A0000002471001".toByteArray())
            var responseStr = response.toString(Charsets.UTF_8)
            println(responseStr)
            runOnUiThread{
                binding.wordList.post {//HIGHLIGHT AFTER VERIFYING
                    highlightCorrectWord(receivedWord, randomWords)
                }
            }
            isoDep.close()
        }catch(e:Exception){
            println("wlistact error: ${ e.message }")
        }
    }
//    fun bytesToPublicKey(bytes: ByteArray): PublicKey {
//        val keySpec = X509EncodedKeySpec(bytes)
//        val keyFactory = KeyFactory.getInstance("RSA")
//        return keyFactory.generatePublic(keySpec)
//    }
//
//    // Function to convert a byte array back to a PrivateKey
//    fun bytesToPrivateKey(bytes: ByteArray): PrivateKey {
//        val keySpec = PKCS8EncodedKeySpec(bytes)
//        val keyFactory = KeyFactory.getInstance("RSA")
//        return keyFactory.generatePrivate(keySpec)
//    }
}