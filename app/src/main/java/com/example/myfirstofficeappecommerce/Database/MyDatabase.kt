package com.example.myfirstofficeappecommerce.Database

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myfirstofficeappecommerce.Models.VariantsModelClass

@Database(entities = [VariantsModelClass::class],version = 2,exportSchema = false)
abstract class MyDatabase :RoomDatabase(){

    abstract fun dao(): MyDao

    companion object{
        private var instance: MyDatabase?=null
        fun getDbInstance(application: Context): MyDatabase {
           synchronized(this)
           {
               return instance ?: Room.databaseBuilder(application, MyDatabase::class.java,"db").fallbackToDestructiveMigration()
                   .build()
           }
        }
    }
}