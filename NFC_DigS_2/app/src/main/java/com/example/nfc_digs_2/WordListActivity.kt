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
import androidx.core.view.forEachIndexed
import com.example.nfc_digs_2.databinding.ActivityWordListBinding
import java.security.KeyFactory
import java.security.MessageDigest
import java.security.PublicKey
import java.security.Signature
import java.security.spec.X509EncodedKeySpec

class WordListActivity : AppCompatActivity() , NfcAdapter.ReaderCallback{

    private lateinit var binding: ActivityWordListBinding
    private lateinit var nfcAdapter: NfcAdapter
    private lateinit var adapter : ArrayAdapter<String>
    private lateinit var randomWords :List<String>
    private lateinit var stringList: String
    private lateinit var signatureBytes: ByteArray
    private lateinit var publicKey: PublicKey

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWordListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //check nfc
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        enableNFC()
        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC is not available on this device", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        if (!nfcAdapter!!.isEnabled) {
            Toast.makeText(this, "Please enable NFC", Toast.LENGTH_SHORT).show()
        }
        //disable nfc until button press
//        nfcAdapter.disableReaderMode(this)
        //receive data
        stringList = intent.getStringExtra("stringList").toString()
        signatureBytes = intent.getByteArrayExtra("signature")!!

        //split the received words string to an array and display on screen
        randomWords = stringList?.split(" ")?.shuffled()!!
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, randomWords)
        binding.wordList.adapter = adapter
        //nfc activation button
//        binding.button.setOnClickListener {
//            enableNFC()
//            Toast.makeText(this, "NFC Enabled",Toast.LENGTH_LONG).show()
//        }
    }

    private fun hashString(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
    private fun verifySignature(data: ByteArray, signatureBytes: ByteArray, publicKey: PublicKey): Boolean {
        val signature = Signature.getInstance("SHA256withRSA")
        signature.initVerify(publicKey)
        signature.update(data)
        return signature.verify(signatureBytes)
    }
    private fun highlightCorrectWord(){
        if(::publicKey.isInitialized){
            binding.wordList.forEachIndexed { index, _ ->
                var chosenWord = binding.wordList.adapter.getItem(index).toString()
                println(chosenWord)
                if (verifySignature(hashString(chosenWord).toByteArray(), signatureBytes, publicKey!!)) {
                    runOnUiThread {
                        binding.wordList.getChildAt(index).setBackgroundColor(Color.Green.hashCode())
                        Toast.makeText(this, "Correct word is found.", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
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
            isoDep.timeout = 3000
            val response = isoDep.transceive(hexStringToByteArray("00A4040007A0000002471001"))
            isoDep.close()
            println(response)
//            public byte array to public key object
            val keySpec = X509EncodedKeySpec(response)
            val keyFactory = KeyFactory.getInstance("RSA")
            publicKey = keyFactory.generatePublic(keySpec)
            highlightCorrectWord()
            println(publicKey.toString())
        }catch(e:Exception){
            println("wlistact error: ${ e.message }")
        }
    }
    private fun hexStringToByteArray(data: String) : ByteArray {
        val HEX_CHARS = "0123456789ABCDEF"
        val result = ByteArray(data.length / 2)

        for (i in 0 until data.length step 2) {
            val firstIndex = HEX_CHARS.indexOf(data[i]);
            val secondIndex = HEX_CHARS.indexOf(data[i + 1]);

            val octet = firstIndex.shl(4).or(secondIndex)
            result.set(i.shr(1), octet.toByte())
        }

        return result
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