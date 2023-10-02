package com.sbma.linkup.nfc

import android.app.Activity
import android.content.Context
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.DefaultLifecycleObserver
import com.sbma.linkup.util.toHex

 class AppNfcManager(
    val context: Context,
    val activity: Activity,
    val nfcAdapter: NfcAdapter?
) :
    DefaultLifecycleObserver, NfcAdapter.ReaderCallback {

    private val TAG = AppNfcManager::class.java.getSimpleName()

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    public fun enableReaderMode(
        flags: Int,
        extras: Bundle
    ) {
        nfcAdapter?.let {
            try {
                it.enableReaderMode(activity, this, flags, extras)
            } catch (ex: UnsupportedOperationException) {
                Log.e(TAG, "UnsupportedOperationException ${ex.message}", ex)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    public fun disableReaderMode() {
        nfcAdapter?.let {
            try {
                nfcAdapter.disableReaderMode(activity)
            } catch (ex: UnsupportedOperationException) {
                Log.e(TAG, "UnsupportedOperationException ${ex.message}", ex)
            }
        }
    }

    fun isSupported(): Boolean {
        return nfcAdapter != null
    }

    fun isEnabled(): Boolean {
        return nfcAdapter?.isEnabled ?: false
    }

    public fun isSupportedAndEnabled(): Boolean {
        return isSupported() && isEnabled()
    }

    override fun onTagDiscovered(tag: Tag?) {
        if (tag != null) {
            println("OnTagDiscovered: ${tag.id.toHex()}")
        }
    }
}