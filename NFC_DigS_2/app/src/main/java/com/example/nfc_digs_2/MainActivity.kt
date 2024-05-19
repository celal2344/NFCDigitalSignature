package com.example.nfc_digs_2

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.nfc_digs_2.databinding.ActivityMainBinding
import com.example.nfc_digs_2.databinding.ActivityWordListBinding
import kotlinx.coroutines.runBlocking
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec

class MainActivity : AppCompatActivity() , NfcAdapter.ReaderCallback{
    private lateinit var binding: ActivityMainBinding
    private lateinit var nfcAdapter: NfcAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
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
    }
    public override fun onResume() {
        super.onResume()
        nfcAdapter?.enableReaderMode(this, this,
            NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK,
            null)
    }
    public override fun onPause() {
        super.onPause()
        nfcAdapter?.disableReaderMode(this)
    }
//    @OptIn(ExperimentalStdlibApi::class)
//    override fun onTagDiscovered(tag: Tag?) {
//        try{
//            val isoDep = IsoDep.get(tag)
//            isoDep.connect()
//            val response = isoDep.transceive(hexStringToByteArray(
//                "00A4040007A0000002471001"))
//            isoDep.close()
//            var responseStr = response.toString(Charsets.UTF_8)
//            println(responseStr)
//            var receivedWord = responseStr
//            runOnUiThread{
//                val i = Intent(this@MainActivity, WordListActivity::class.java)
//                i.putExtra("asd",receivedWord)
//                startActivity(i)
//            }
//        }catch(e:Exception){
//            println("error: ${ e.message }")
//            e.printStackTrace()
//        }
//    }
    @OptIn(ExperimentalStdlibApi::class)
    override fun onTagDiscovered(tag: Tag?) {
        try{
            val isoDep = IsoDep.get(tag)
            isoDep.connect()
            isoDep.timeout = 2000
            val responseApdu = isoDep.transceive(hexStringToByteArray("00A4040007A0000002471001"))
            isoDep.close()
            val buffer = responseApdu.copyOfRange(0, 4)
            val stringLength = ByteBuffer.wrap(buffer).getInt()  // Read the first 4 bytes as the string length
            val stringBytes = responseApdu.sliceArray(4 until (4 + stringLength))
            val stringList = String(stringBytes, StandardCharsets.UTF_8)
            val signatureBytes = responseApdu.sliceArray((4 +stringLength) until responseApdu.size)
            runOnUiThread{
                val i = Intent(this@MainActivity, WordListActivity::class.java).apply {
                    putExtra("stringList", stringList)
                    putExtra("signature", signatureBytes)
                }
                startActivity(i)
            }
        }catch(e:Exception){
            println("error: ${ e.message }")
            e.printStackTrace()
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
}