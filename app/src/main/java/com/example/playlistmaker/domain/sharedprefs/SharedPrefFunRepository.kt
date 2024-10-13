package com.example.playlistmaker.domain.sharedprefs

interface SharedPrefFunRepository {

    fun saveString(name:String,key: String, value: String)

    fun getString(name:String,key: String): String?

    fun saveBoolean(name:String,key: String, value: Boolean)

    fun getBoolean(name: String,key: String,value: Boolean): Boolean

    fun remove(name:String,key: String)
}