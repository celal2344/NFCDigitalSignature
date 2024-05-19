package com.example.nfc_dig_1

import android.app.Service
import android.content.Intent
import android.nfc.cardemulation.HostApduService
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import java.nio.ByteBuffer
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec

class HostCardEmulatorService: HostApduService() {

    private var stringList: String = ""
    private var signature: ByteArray = byteArrayOf()

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        try {
            if (intent.extras != null) {
                stringList = intent.getStringExtra("stringList").toString()
                signature = intent.getByteArrayExtra("signature")!!
            }
        }catch (e:Exception){
            println("error: ${e.message}")
        }
        return Service.START_NOT_STICKY
    }
    override fun onDeactivated(reason: Int) {
    }
    override fun processCommandApdu(commandApdu: ByteArray?, extras: Bundle?): ByteArray {
        val stringBytes = stringList.toByteArray()
        val response = ByteBuffer.allocate(4 + stringBytes.size + signature.size)
            .putInt(stringBytes.size)
            .put(stringBytes)
            .put(signature)
            .array()
        val i = Intent(this@HostCardEmulatorService, NFCActivity2::class.java)
        return response
    }
}