package com.example.wattup.utils

object StringMod {
    fun cutAfterEmail(email: String): String{
        return email.substringBefore('@')
    }

    fun isNumber(string: String): Boolean{
        return string.matches("\\d+".toRegex())
    }
}