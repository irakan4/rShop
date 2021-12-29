package com.devrakan.rshop.Model

class Store {
    private var uid: String = ""
    private var email: String = ""
    private var username: String = ""
    private var phone: String = ""
    private var image: String = ""

    constructor()

    constructor(uid: String, email: String, username: String, phone: String, image: String) {
        this.uid = uid
        this.email = email
        this.username = username
        this.phone = phone
        this.image = image
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
}