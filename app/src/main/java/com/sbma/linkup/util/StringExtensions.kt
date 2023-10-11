package com.sbma.linkup.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sbma.linkup.R
import com.sbma.linkup.presentation.icons.Aboutme
import com.sbma.linkup.presentation.icons.Address
import com.sbma.linkup.presentation.icons.Mail
import com.sbma.linkup.presentation.icons.Phone
import com.sbma.linkup.presentation.icons.Title
import com.sbma.linkup.presentation.icons.Website
import java.util.UUID


fun String?.toPictureResource(): Int? =
    when (this) {
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
        else -> null
    }
fun String?.toImageVector(): ImageVector? =
    when (this) {
        "phone" -> Icons.Filled.Phone
        "email" -> Icons.Filled.Mail
        "title" -> Icons.Filled.Title
        "about" -> Icons.Filled.Aboutme
        "address" -> Icons.Filled.Address
        "website" -> Icons.Filled.Website

            else -> null
    }

fun String.uuidOrNull(): UUID? {
    return try {
        UUID.fromString(this)
    } catch (exception: IllegalArgumentException) {
        null
    }
}

@Composable
fun CardIcon(picture: String?, modifier: Modifier = Modifier){
    picture?.let {
        it.toPictureResource()?.let { resourceId ->
            Image(
                painterResource(resourceId),
                contentDescription = "Icon name",
                modifier = modifier
            )

        }
            ?: it.toImageVector()?.let {imageVector ->
                Icon(
                    imageVector = imageVector,
                    contentDescription = "Delete",
                    modifier = modifier
                )

            }
    }

}