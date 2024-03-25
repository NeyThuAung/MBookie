package com.example.mbookie.util

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AppSharedPreference @Inject constructor(@ApplicationContext context: Context) {

    private val fileName = "DATA"
    private val sharedPref: SharedPreferences =
        context.getSharedPreferences(fileName, Context.MODE_PRIVATE)

    fun save(KEY_NAME: String, text: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(KEY_NAME, text)
        editor.apply()
    }

    fun save(KEY_NAME: String, value: Int) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putInt(KEY_NAME, value)
        editor.apply()
    }

    fun save(KEY_NAME: String, status: Boolean) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putBoolean(KEY_NAME, status)
        editor.apply()
    }

    fun getValueString(KEY_NAME: String): String? {
        return sharedPref.getString(KEY_NAME, null)
    }

    fun getValueInt(KEY_NAME: String): Int {
        return sharedPref.getInt(KEY_NAME, 0)
    }

    fun getValueBoolean(KEY_NAME: String, defaultValue: Boolean): Boolean {
        return sharedPref.getBoolean(KEY_NAME, defaultValue)
    }

    fun clearSharedPreference() {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.clear()
        editor.apply()
    }

    fun removeValue(KEY_NAME: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.remove(KEY_NAME)
        editor.apply()
    }

//    fun saveLanguageCode(languageCode:Int){
//        sharedPref.edit().putInt(ProductConfig.LANGUAGE,languageCode).apply()
//    }
//
//
//    fun saveLoginType(loginType:Int){
//        sharedPref.edit().putInt(ProductConfig.LOGIN_TYPE,loginType).apply()
//    }
//    fun getLoginType() :Int{
//        return sharedPref.getInt(ProductConfig.LOGIN_TYPE,0)
//    }
//
//
//    fun getLanguageCode():Int{
//        return sharedPref.getInt(ProductConfig.LANGUAGE,1)
//    }
//    fun saveLanguageCode(languageCode:Int){
//        sharedPref.edit().putInt(ProductConfig.LANGUAGE,languageCode).apply()
//    }
//
//
//    fun saveLoginType(loginType:Int){
//        sharedPref.edit().putInt(ProductConfig.LOGIN_TYPE,loginType).apply()
//    }
//    fun getLoginType() :Int{
//        return sharedPref.getInt(ProductConfig.LOGIN_TYPE,0)
//    }
//
//
//    fun getLanguageCode():Int{
//        return sharedPref.getInt(ProductConfig.LANGUAGE,1)
//    }

}