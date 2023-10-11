package com.sbma.linkup.util

import com.sbma.linkup.R
import java.util.UUID


fun String?.toPictureResource(): Int =
    when (this) {
        "phone" -> R.drawable.call
        "description" -> R.drawable.description
        "about" -> R.drawable.aboutme
        "address" -> R.drawable.location
        "email" -> R.drawable.mail
        "website" -> R.drawable.website
        "facebook" -> R.drawable.facebook
        "instagram" -> R.drawable.instagram
        "linkedin" -> R.drawable.linkedin
        "youtube" -> R.drawable.youtube
        "twitter" -> R.drawable.twitter
        "discord" -> R.drawable.discord
        "github" -> R.drawable.github
        "snapchat" -> R.drawable.snapchat
        "pinterest" -> R.drawable.pinterest
        "tiktok" -> R.drawable.tiktok
        "telegram" -> R.drawable.telegram
        "reddit" -> R.drawable.reddit
        else -> R.drawable.person
    }

fun String.uuidOrNull(): UUID? {
    return try {
        UUID.fromString(this)
    } catch (exception: IllegalArgumentException) {
        null
    }
}