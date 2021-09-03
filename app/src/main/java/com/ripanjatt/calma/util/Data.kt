package com.ripanjatt.calma.util

import android.content.Context
import android.util.Log
import java.io.*
import kotlin.concurrent.thread

object Data {

    fun setData(list: ArrayList<Audio>, context: Context) {
        thread {
            try {
                val dir = context.getExternalFilesDir(null)
                if (dir != null) {
                    if(!dir.exists()) {
                        dir.mkdirs()
                    }
                    val objectOutputStream = ObjectOutputStream(FileOutputStream(File("$dir/database.db")))
                    objectOutputStream.writeObject(list)
                    objectOutputStream.close()
                }
            } catch (e: Exception) {
                Log.e("Error", "$e")
            }
        }
    }

    fun getData(context: Context) : ArrayList<Audio> {
        val list = ArrayList<Audio>()
        try {
            val dir = context.getExternalFilesDir(null)
            if (dir != null) {
                if(!dir.exists()) {
                    dir.mkdirs()
                    return list
                }
                if(!File("$dir/database.db").exists()) {
                    return list
                }
                val objectInputStream = ObjectInputStream(FileInputStream(File("$dir/database.db")))
                val temp = objectInputStream.readObject() as ArrayList<Audio>
                list.addAll(temp)
            }
        } catch (e: Exception) {
            Log.e("Error", "$e")
        }
        return list
    }

    fun setFav(list: ArrayList<Audio>, context: Context) {
        thread {
            try {
                val dir = context.getExternalFilesDir(null)
                if (dir != null) {
                    if(!dir.exists()) {
                        dir.mkdirs()
                    }
                    val objectOutputStream = ObjectOutputStream(FileOutputStream(File("$dir/fav.db")))
                    objectOutputStream.writeObject(list)
                    objectOutputStream.close()
                }
            } catch (e: Exception) {
                Log.e("Error", "$e")
            }
        }
    }

    fun getFav(context: Context) : ArrayList<Audio> {
        val list = ArrayList<Audio>()
        try {
            val dir = context.getExternalFilesDir(null)
            if (dir != null) {
                if(!dir.exists()) {
                    dir.mkdirs()
                    return list
                }
                if(!File("$dir/fav.db").exists()) {
                    return list
                }
                val objectInputStream = ObjectInputStream(FileInputStream(File("$dir/fav.db")))
                val temp = objectInputStream.readObject() as ArrayList<Audio>
                list.addAll(temp)
            }
        } catch (e: Exception) {
            Log.e("Error", "$e")
        }
        return list
    }

    fun setCurrent(current: Audio, context: Context) {
        thread {
            try {
                val dir = context.getExternalFilesDir(null)
                if (dir != null) {
                    if(!dir.exists()) {
                        dir.mkdirs()
                    }
                    val objectOutputStream = ObjectOutputStream(FileOutputStream(File("$dir/current.db")))
                    objectOutputStream.writeObject(current)
                    objectOutputStream.close()
                }
            } catch (e: Exception) {
                Log.e("Error", "$e")
            }
        }
    }

    fun getCurrent(context: Context) : Audio? {
        var current: Audio? = null
        try {
            val dir = context.getExternalFilesDir(null)
            if (dir != null) {
                if(!dir.exists()) {
                    dir.mkdirs()
                    return null
                }
                if(!File("$dir/current.db").exists()) {
                    return null
                }
                val objectInputStream = ObjectInputStream(FileInputStream(File("$dir/current.db")))
                return objectInputStream.readObject() as Audio
            }
        } catch (e: Exception) {
            Log.e("Error", "$e")
        }
        return current
    }
}