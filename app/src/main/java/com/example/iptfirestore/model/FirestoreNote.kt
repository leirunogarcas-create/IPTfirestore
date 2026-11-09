package com.example.iptfirestore.model

data class FirestoreNote(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val userEmail: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
