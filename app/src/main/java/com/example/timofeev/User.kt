package com.example.timofeev

class User(val name:String?="",val phone:String?="",val pass:String?="")  {
    companion object{
        var currentuser:User?=null
    }
}