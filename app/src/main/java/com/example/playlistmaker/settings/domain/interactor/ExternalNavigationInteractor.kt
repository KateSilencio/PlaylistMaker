package com.example.playlistmaker.settings.domain.interactor

interface ExternalNavigationInteractor {

    fun onShareLink(link: String)

    fun onEmailSupport(email: String, subject: String, text: String)

    fun onOpenAgreementLink(url: String)

}