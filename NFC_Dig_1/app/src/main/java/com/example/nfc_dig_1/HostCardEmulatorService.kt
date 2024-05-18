package com.example.nfc_dig_1

import android.content.Intent
import android.nfc.cardemulation.HostApduService
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.KITKAT)
class HostCardEmulatorService: HostApduService() {
    override fun onDeactivated(reason: Int) {
    }


    @OptIn(ExperimentalStdlibApi::class)
    override fun processCommandApdu(commandApdu: ByteArray?, extras: Bundle?): ByteArray {
    }
}