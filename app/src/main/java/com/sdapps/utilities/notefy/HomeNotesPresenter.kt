package com.sdapps.utilities.notefy

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract.Data
import android.util.Log
import com.sdapps.utilities.notefy.db.DbHandler
import java.util.Collections
import java.util.Date

class HomeNotesPresenter(private val appContext: Context): HomeNotesInteractor.Presenter {

    private lateinit var view: HomeNotesInteractor.View
    private lateinit var context : Context
    private lateinit var list : ArrayList<NotesBO>


    override fun attachView(view: HomeNotesInteractor.View, context : Context) {
        this.view = view
        this.context = context
    }

    override fun detachView() {
    }

    override fun getAllNotesData(): ArrayList<NotesBO> {

        try{
            list = arrayListOf()
            val db = DbHandler(context, DataMember.DB_NAME)
            db.openDB()
            val sql = "select id,dateCreated,timeCreated,title,content from notesmaster order by timecreated DESC"
            val cursor = db.selectSql(sql)

            if(cursor != null){
                while(cursor.moveToNext()){
                    val bor = NotesBO().apply {
                        id = cursor.getInt(0)
                        dateCreated = cursor.getString(1)
                        timeCreated = cursor.getString(2)
                        notesTitle = cursor.getString(3)
                        notesContent = cursor.getString(4)
                    }

                    list.add(bor)
                }
                cursor.close()
            }
            return list
        }catch (ex: Exception){
            ex.printStackTrace()
            return ArrayList()
        }


    }

    fun sortList(){
        if(list != null){
            val timeList = list
            Collections.sort(timeList, object : Comparator<NotesBO> {
                override fun compare(time1: NotesBO, time2: NotesBO): Int {
                    return time1.timeCreated!!.compareTo(time2.timeCreated!!)
                }
            })
        }
    }

    override fun addNotes(notesBO: NotesBO) {
        TODO("Not yet implemented")
    }

    override fun deleteNotesByPosition(position: Int) {
        val db = DbHandler(context, DataMember.DB_NAME)
        db.openDB()
        val sql = "DELETE FROM NotesMaster where rowid = $position"
        db.exe(sql)
        db.closeDB()
    }
    override fun deleteAllNotes() {
        TODO("Not yet implemented")
    }

    override fun editNotes(id: Int, notesBO: NotesBO) {
        TODO("Not yet implemented")
    }
}