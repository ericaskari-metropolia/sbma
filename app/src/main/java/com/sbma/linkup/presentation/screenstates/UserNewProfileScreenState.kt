package com.sbma.linkup.presentation.screenstates

data class UserNewProfileScreenState(
    val username: String,
    val email: String,
    val phoneNumber: String,
    val address: String,
    val description: String,
    val instagramLink: String,
    val twitterLink: String,
    val facebookLink: String,
    val linkedinLink: String
)