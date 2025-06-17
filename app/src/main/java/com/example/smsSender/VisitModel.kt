package com.example.smsSender

data class VisitModel(
    val id: Int = 0,
    val contactId: Int = 0,
    val timeOfVisit: Long? = null,
    val listOfSmsDates: List<Long> = emptyList()
)
