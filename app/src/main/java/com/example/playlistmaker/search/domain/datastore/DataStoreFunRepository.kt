package com.example.playlistmaker.search.domain.datastore

interface DataStoreFunRepository {

    fun saveString(name:String,key: String, value: String)

    fun getString(name:String,key: String): String?

    fun remove(name:String,key: String)
}