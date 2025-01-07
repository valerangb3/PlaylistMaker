package com.practicum.playlistmaker.sharing.domain.model

class EmailData(
    val emailList: Array<String>,
    val emailSubject: String,
    val emailText: String,
    val uri: String
)