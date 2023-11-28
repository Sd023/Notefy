package com.sdapps.utilities.notefy

import android.content.Context

interface HomeNotesInteractor {


    interface View{
        fun showLoading()
        fun hideLoading()
        fun showAlert()
        fun updateUI()

    }


    interface Presenter {

        fun attachView(view: HomeNotesInteractor.View, context : Context)
        fun detachView()
        fun getAllNotesData():ArrayList<NotesBO>
        fun addNotes(notesBO: NotesBO)
        fun deleteNotesByPosition(position: Int)
        fun deleteAllNotes()
        fun editNotes(id: Int, notesBO : NotesBO)
    }
}