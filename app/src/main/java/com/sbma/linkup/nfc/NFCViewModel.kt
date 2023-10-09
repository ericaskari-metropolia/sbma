package com.sbma.linkup.nfc

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

class NFCViewModel(val appNfcManager: AppNfcManager) : ViewModel() {
    companion object {
        private val TAG = NFCViewModel::class.java.simpleName
        private const val prefix = "android.nfc.tech."
    }

    private val liveNFC: MutableStateFlow<NFCStatus?> = MutableStateFlow(null)
    private val liveToast: MutableSharedFlow<String?> = MutableSharedFlow()


    private suspend fun postToast(message: String) {
        Log.d(TAG, "postToast(${message})")
        liveToast.emit(message)
    }

    fun observeToast(): SharedFlow<String?> {
        return liveToast.asSharedFlow()
    }

    //endregion
    fun getNFCFlags(): Int {
        return NfcAdapter.FLAG_READER_NFC_A or
                NfcAdapter.FLAG_READER_NFC_B or
                NfcAdapter.FLAG_READER_NFC_F or
                NfcAdapter.FLAG_READER_NFC_V or
                NfcAdapter.FLAG_READER_NFC_BARCODE //or NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK
    }

    fun getExtras(): Bundle {
        val options: Bundle = Bundle()
        options.putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 30000)
        return options
    }

    //region NFC Methods
    fun onCheckNFC(isChecked: Boolean) {
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

    fun observeTag(): StateFlow<String?> {
        return appNfcManager.liveTag.asStateFlow()
    }


}