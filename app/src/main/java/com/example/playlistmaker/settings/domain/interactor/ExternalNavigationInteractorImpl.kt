package com.example.playlistmaker.settings.domain.interactor

import com.example.playlistmaker.settings.domain.ExternalNavigation

class ExternalNavigationInteractorImpl(private val externalNavigation: ExternalNavigation) :
    ExternalNavigationInteractor {

    override fun onShareLink(link: String){
        externalNavigation.shareLink(link)
    }

    override fun onEmailSupport(email: String, subject: String, text: String){
        externalNavigation.emailSupport(email,subject,text)
    }

    override fun onOpenAgreementLink(url: String){
        externalNavigation.openAgreementLink(url)
    }

}