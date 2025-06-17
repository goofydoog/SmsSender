package com.example.smsSender


data class ContactModel(
    val id: Int = 0,
    val firstName: String = "",
    val lastName: String? = null,
    val phoneNumber: String = ""
)