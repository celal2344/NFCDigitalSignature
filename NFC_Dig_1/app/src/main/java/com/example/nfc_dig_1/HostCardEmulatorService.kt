package com.example.nfc_dig_1

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
    private var publicKeyBytes : ByteArray = byteArrayOf()
    private var buttonClicked = false
    private lateinit var receiver: BroadcastReceiver

    @OptIn(ExperimentalStdlibApi::class)
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        try {
            //receive extras
            if (intent.extras != null) {
                    stringList = intent.getStringExtra("stringList").toString()
                    signature = intent.getByteArrayExtra("signature")!!
                    publicKeyBytes = intent.getByteArrayExtra("publicKeyBytes")!!
            }
        }catch (e:Exception){
            println("error: ${e.message}")
        }
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == "com.example.UPDATE_BOOLEAN") {
                    buttonClicked = intent.getBooleanExtra("boolean_value", false)
                    // Perform any other actions needed when the boolean is updated
                }
            }
        }
        val filter = IntentFilter("com.example.UPDATE_BOOLEAN")
        registerReceiver(receiver, filter, Context.RECEIVER_EXPORTED) // Set the receiver as exported

        return Service.START_STICKY_COMPATIBILITY
    }
    override fun onDeactivated(reason: Int) {
    }

    override fun onDestroy() {
        super.onDestroy()

    }
    @OptIn(ExperimentalStdlibApi::class)
    override fun processCommandApdu(commandApdu: ByteArray?, extras: Bundle?): ByteArray {
        var response: ByteArray = byteArrayOf()
        //send hce response to the other device according to the received extras
        if(commandApdu?.toHexString() == "00a4040007a0000002471011"){
            val stringBytes = stringList.toByteArray()
            response = ByteBuffer.allocate(4 + stringBytes.size + signature.size)
                .putInt(stringBytes.size)
                .put(stringBytes)
                .put(signature)
                .array()
            stringList = ""
            signature = byteArrayOf()
        }

        if(commandApdu?.toHexString() == "00a4040007a0000002471001" && buttonClicked){

            response = publicKeyBytes
        }
        return response
    }
}