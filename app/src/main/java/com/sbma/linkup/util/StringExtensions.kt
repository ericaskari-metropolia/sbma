package com.sbma.linkup.util

import com.sbma.linkup.R

fun String.toPictureResource(): Int  =
    when(this){
        "phone" -> R.drawable.call
        "description" -> R.drawable.description
        "about" -> R.drawable.aboutme
        "location" -> R.drawable.location
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
        else -> R.drawable.profile_photo
    }
