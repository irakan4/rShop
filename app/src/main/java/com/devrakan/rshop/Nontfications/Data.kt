package com.devrakan.rshop.Nontfications

import okhttp3.FormBody

class Data {
    private var user:String = ""
    private var icon = 0
    private var body:String = ""
    private var title:String = ""
    private var sented:String = ""
    constructor(){}
    constructor(user:String,icon:Int,body:String,title:String,sented:String){
        this.user = user
        this.icon = icon
        this.title = title
        this.sented = sented
        this.body = body
    }
    fun getUser():String?{
        return user
    }
    fun getIcon():Int?{
        return icon
    }

    fun getBody():String?{
        return body
    }

    fun getTitle():String?{
        return title
    }

    fun getSented():String?{
        return sented
    }

    fun setUser(user: String){
        this.user = user
    }

    fun setIconn(icon: Int){
        this.icon = icon
    }

    fun setTitle(title:String){
        this.title = title
    }

    fun setBody(body: String){
        this.body = body
    }

    fun setSented(sented: String){
        this.sented = sented
    }
}