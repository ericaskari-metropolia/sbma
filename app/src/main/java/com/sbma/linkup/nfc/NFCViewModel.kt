package com.sbma.linkup.nfc

import android.content.ContentValues
import android.nfc.NfcAdapter
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.experimental.and

class NFCViewModel(val appNfcManager: AppNfcManager) : ViewModel() {
    companion object {
        private val TAG = NFCViewModel::class.java.getSimpleName()
        private const val prefix = "android.nfc.tech."
    }

    private val liveNFC: MutableStateFlow<NFCStatus?> = MutableStateFlow(null)
    private val liveToast: MutableSharedFlow<String?> = MutableSharedFlow()


    private suspend fun postToast(message: String) {
        Log.d(TAG, "postToast(${message})")
        liveToast.emit(message)
    }

    public fun observeToast(): SharedFlow<String?> {
        return liveToast.asSharedFlow()
    }

    //endregion
    public fun getNFCFlags(): Int {
        return NfcAdapter.FLAG_READER_NFC_A or
                NfcAdapter.FLAG_READER_NFC_B or
                NfcAdapter.FLAG_READER_NFC_F or
                NfcAdapter.FLAG_READER_NFC_V or
                NfcAdapter.FLAG_READER_NFC_BARCODE //or NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK
    }

    public fun getExtras(): Bundle {
        val options: Bundle = Bundle();
        options.putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 30000);
        return options
    }

    //region NFC Methods
    public fun onCheckNFC(isChecked: Boolean) {
        viewModelScope.launch {
            Log.d(TAG, "onCheckNFC(${isChecked})")
            if (isChecked) {
                postNFCStatus(NFCStatus.Tap)
                appNfcManager.enableReaderMode(getNFCFlags(), getExtras())
            } else {
                postNFCStatus(NFCStatus.NoOperation)
                postToast("NFC is Disabled, Please Toggle On!")
                appNfcManager.disableReaderMode()
            }
        }
    }

    private suspend fun postNFCStatus(status: NFCStatus) {
        Log.d(TAG, "postNFCStatus(${status})")
        if (appNfcManager.isSupported()) {
            liveNFC.emit(status)
        } else if (!appNfcManager.isEnabled()) {
            liveNFC.emit(NFCStatus.NotEnabled)
            postToast("Please Enable your NFC!")
        } else if (!appNfcManager.isSupported()) {
            liveNFC.emit(NFCStatus.NotSupported)
            postToast("NFC Not Supported!")
        }
    }

    public fun observeNFCStatus(): StateFlow<NFCStatus?> {
        return liveNFC.asStateFlow()
    }

    //endregion
    //region Tags Information Methods
    private fun getDateTimeNow(): String {
        Log.d(TAG, "getDateTimeNow()")
        val TIME_FORMAT: DateFormat = SimpleDateFormat.getDateTimeInstance()
        val now: Date = Date()
        Log.d(ContentValues.TAG, "getDateTimeNow() Return ${TIME_FORMAT.format(now)}")
        return TIME_FORMAT.format(now)
    }

    private fun getHex(bytes: ByteArray): String {
        val sb = StringBuilder()
        for (i in bytes.indices.reversed()) {
            val b: Int = bytes[i].and(0xff.toByte()).toInt()
            if (b < 0x10) sb.append('0')
            sb.append(Integer.toHexString(b))
            if (i > 0)
                sb.append(" ")
        }
        return sb.toString()
    }

    private fun getDec(bytes: ByteArray): Long {
        Log.d(TAG, "getDec()")
        var result: Long = 0
        var factor: Long = 1
        for (i in bytes.indices) {
            val value: Long = bytes[i].and(0xffL.toByte()).toLong()
            result += value * factor
            factor *= 256L
        }
        return result
    }

    private fun getReversed(bytes: ByteArray): Long {
        Log.d(TAG, "getReversed()")
        var result: Long = 0
        var factor: Long = 1
        for (i in bytes.indices.reversed()) {
            val value = bytes[i].and(0xffL.toByte()).toLong()
            result += value * factor
            factor *= 256L
        }
        return result
    }

    public fun observeTag(): StateFlow<String?> {
        return appNfcManager.liveTag.asStateFlow()
    }
    //endregion
}