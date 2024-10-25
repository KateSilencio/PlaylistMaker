package com.example.playlistmaker.settings.domain

interface ExternalNavigation {

    fun shareLink(link: String)

    fun  emailSupport(email: String, subject: String, text: String)

    fun openAgreementLink(url: String)

}