package com.sbma.linkup.util

fun ByteArray.toHex(): String = "0x" + joinToString(separator = "") { eachByte ->
    "%02X".format(eachByte).uppercase()
}