package com.sdapps.utilities.notefy.db

object DBHelper {


    fun createTables(dbHandler: DbHandler){

        try{
            dbHandler.exe("create table if not exists NotesMaster (id INTEGER PRIMARY KEY AUTOINCREMENT, dateCreated TEXT, timeCreated TEXT, title TEXT, content TEXT)")
        }catch (ex: Exception){
            ex.printStackTrace()
        }

    }
}