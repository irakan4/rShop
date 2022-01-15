package com.devrakan.rshop.Model

class Users {

    private var uid: String = ""
    private var email: String = ""
    private var username: String = ""
    private var phone: String = ""
    private var image: String = ""
    private var work: String = ""

    constructor()


    constructor(uid: String, email: String, username: String, phone: String, image: String,work:String) {
        this.uid = uid
        this.email = email
        this.username = username
        this.phone = phone
        this.image = image
        this.work = work
    }


    fun getUsername(): String {
        return username
    }

    fun setUsername(username: String) {
        this.username = username
    }

    fun getEmail(): String {
        return email
    }

    fun setEmail(email: String) {
        this.email = email
    }

    fun getPhone(): String {
        return phone
    }

    fun setPhone(phone: String) {
        this.phone = phone
    }


    fun getUID(): String {
        return uid
    }

    fun setUID(uid: String) {
        this.uid = uid
    }

    fun getImage(): String {
        return image
    }

    fun setImage(image: String) {
        this.image = image
    }
    fun getWork(): String {
        return work
    }

    fun setWork(work: String) {
        this.work = work
    }
}