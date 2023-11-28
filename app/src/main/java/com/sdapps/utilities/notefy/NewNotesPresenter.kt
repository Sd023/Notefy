package com.sdapps.utilities.notefy

import android.content.Context
import com.sdapps.utilities.notefy.db.DbHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import com.sdapps.utilities.notefy.DataMember.DB_NAME
import kotlinx.coroutines.launch

class NewNotesPresenter(context: Context) : NewNotesInteractor.Presenter{
    private lateinit var context: Context
    private lateinit var view : NewNotesInteractor.View

    private lateinit var notesDetails : ArrayList<NotesBO>

    override fun attachView(view: NewNotesInteractor.View, context: Context) {
        this.view = view
        this.context = context

    }

    override fun detachView() {
        TODO("Not yet implemented")
    }

    override fun sendData(position: Int) {
        notesDetails = arrayListOf()
        try {
            val db = DbHandler(context, DB_NAME)
            db.openDB()
            val sql = "SELECT dateCreated,timeCreated,title,content FROM NotesMaster where rowid = $position"
            val c = db.selectSql(sql)
            if( c != null){
                while(c.moveToNext()){
                    val bo = NotesBO().apply {
                        dateCreated = c.getString(0)
                        timeCreated = c.getString(1)
                        notesTitle = c.getString(2)
                        notesContent = c.getString(3)
                    }
                    notesDetails.add(bo)
                }
                c.close()
            }
            view.getDataFromAliens(notesDetails)

            db.closeDB()

        }catch (ex: Exception){
            ex.printStackTrace()
            notesDetails = ArrayList()
        }
    }

    override fun updateData(title: String, content: String, position: Int) {
        try{
            val db = DbHandler(context, DB_NAME)
            db.openDB()
            val sql = "update NotesMaster set title = ${queryFormat(title)}, content = ${queryFormat(content)} where rowid = $position"
            db.updateSQL(sql)
            db.closeDB()
            view.moveBackToScreen()
        }catch (ex: Exception){
            ex.printStackTrace()

        }
    }

    override fun deleteFromDB(index: Int) {
        val db = DbHandler(context,DB_NAME)
        db.openDB()
        val sql = "DELETE FROM NotesMaster where id = $index"
        db.exe(sql)
        db.closeDB()
    }


    override fun saveNotes(bo: NotesBO) {
        try{
            CoroutineScope(Dispatchers.IO).launch {
                val db = DbHandler(context,DB_NAME)
                db.openDB()

                val values = columnvalues(bo)
                db.insertSQL(DataMember.TABLE_NAME,DataMember.columns, values)
                db.closeDB()
                view.moveBackToScreen()
            }
        }catch (ex: Exception){
            ex.printStackTrace()
        }

    }


    fun columnvalues(bo: NotesBO): String{
        val sb = StringBuilder()
        val values =
            sb.append(queryFormat(bo.dateCreated!!))
            .append(",")
            .append(queryFormat(bo.timeCreated!!))
            .append(",")
            .append(queryFormat(bo.notesTitle!!))
            .append(",")
            .append(queryFormat(bo.notesContent!!))

        return values.toString()

    }
    fun queryFormat(data: String): String{
        return "'$data'"
    }
}