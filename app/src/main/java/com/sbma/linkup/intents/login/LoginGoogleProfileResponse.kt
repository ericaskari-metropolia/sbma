package com.sbma.linkup.intents.login

data class LoginGoogleProfileResponse(
    val email: String,
    val picture: String,
    val displayName: String,
    val givenName: String,
    val familyName: String
    // And more but we do not need in android app
)
