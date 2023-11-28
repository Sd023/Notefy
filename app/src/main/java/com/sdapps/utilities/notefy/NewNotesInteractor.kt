package com.sdapps.utilities.notefy

import android.content.Context

interface NewNotesInteractor {

    interface View{
        fun moveBackToScreen()

        fun getDataFromAliens(list : ArrayList<NotesBO>)
    }

    interface Presenter{
        fun saveNotes(bo : NotesBO)
        fun attachView(view : NewNotesInteractor.View,context: Context)
        fun detachView()

        fun sendData(position : Int)
        fun updateData(title: String,content: String, position: Int)
        fun deleteFromDB(index : Int)
    }
}