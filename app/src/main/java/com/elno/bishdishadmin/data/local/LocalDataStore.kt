package com.elno.bishdishadmin.data.local

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class LocalDataStore (context: Context?) {

    val sharedPreferences: SharedPreferences? = context?.getSharedPreferences("dataFile", Context.MODE_PRIVATE)
    val editor = sharedPreferences?.edit()

    inline fun<reified T> addToList(item: T?, key: String) {
        val currentList = getList<T>(key)
        if(currentList.contains(item).not()) {
            currentList.add(0, item)
            setList(currentList, key)
        }
    }

    inline fun<reified T> removeFromList(item: T?, key: String) {
        val currentList = getList<T>(key)
        currentList.remove(item)
        setList(currentList, key)
    }

    inline fun<reified T> setList(list: List<T?>, key: String) {
        val gson = Gson()
        val json = gson.toJson(list)
        editor?.putString(key, json)
        editor?.commit()
    }

    fun removeList(key: String) {
        editor?.remove(key)
        editor?.commit()
    }

    inline fun <reified T>  getList(key: String): MutableList<T?> {
        var arrayItems: List<T> = listOf()
        val serializedObject = this.sharedPreferences?.getString(key, null)
        if (serializedObject != null) {
            val gson = Gson()
            val type: Type = object : TypeToken<List<T?>?>() {}.type
            arrayItems = gson.fromJson(serializedObject, type)
        }
        return arrayItems.toMutableList()
    }
}