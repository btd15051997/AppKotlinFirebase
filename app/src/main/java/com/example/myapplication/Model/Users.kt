package com.example.myapplication.Model

class Users {

    private var uid: String = ""
    private var email: String = ""
    private var cover: String = ""
    private var name: String = ""
    private var profile: String = ""
    private var search: String = ""
    private var status: String = ""


    constructor(
        uid: String,
        email: String,
        cover: String,
        name: String,
        profile: String,
        search: String,
        status: String
    ) {
        this.uid = uid
        this.email = email
        this.cover = cover
        this.name = name
        this.profile = profile
        this.search = search
        this.status = status
    }

    constructor(){


    }
    fun getCover(): String {

        return cover
    }

    fun setCover(profile: String) {

        this.cover = cover

    }

    fun setUid(uid: String) {

        this.uid = uid

    }

    fun getProfile(): String {

        return profile
    }

    fun setProfile(profile: String) {

        this.profile = profile

    }

    fun getUid(): String {

        return uid
    }

    fun setName(name: String) {

        this.name = name

    }

    fun getName(): String {

        return name
    }

    fun setEmail(email: String) {

        this.email = email

    }

    fun getEmail(): String {

        return email
    }


    fun setStatus(status: String) {

        this.status = status

    }

    fun getStatus(): String {

        return status
    }


    fun setSearch(search: String) {

        this.search = search

    }

    fun getSearch(): String {

        return search
    }
}