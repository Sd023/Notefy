package com.sdapps.utilities.notefy

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.sdapps.utilities.notefy.databinding.ActivityAddNotesViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class AddNotesView : Activity(), NewNotesInteractor.View {

    private lateinit var binding : ActivityAddNotesViewBinding
    private lateinit var presenter: NewNotesInteractor.Presenter

    private lateinit var title : String
    private lateinit var content: String
    private var position: Int? = null
    private var isUpdateScreen: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNotesViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = NewNotesPresenter(applicationContext)
        presenter.attachView(this,applicationContext)
        var bundle = Bundle()
        bundle = intent.extras!!
        position = bundle.getInt("POSITION")
        isUpdateScreen = bundle.getBoolean("isUpdateScreen")

        if(isUpdateScreen!!)
                (presenter as NewNotesPresenter).sendData(position!!)


        binding.backBtn.setOnClickListener{
           validateAndSave(binding,isUpdateScreen!!)
        }
    }


    fun validateAndSave(binding: ActivityAddNotesViewBinding, isUpdateScreen: Boolean){
        title = binding.title.text.toString()
        content = binding.content.text.toString()
        if(!title.trim().isEmpty() && !content.trim().isEmpty() && !isUpdateScreen){
            saveNotes(title,content)
        }else if(!title.trim().isEmpty() && !content.trim().isEmpty() && isUpdateScreen){
            updateNotes(title,content,position!!)
        } else{
            onBackPressed()
        }
    }
    fun saveNotes(title: String, content: String){
        CoroutineScope(Dispatchers.IO).launch {
            val currentDate: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
            val currentTime: String = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())

            val bo = NotesBO().apply {
                notesTitle = title
                notesContent = content
                dateCreated = currentDate
                timeCreated = currentTime
            }
            presenter.saveNotes(bo)
        }
    }


    fun updateNotes(title : String, content: String, position : Int){
        CoroutineScope(Dispatchers.IO).launch {
            presenter.updateData(title,content, position)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(isUpdateScreen!!){
            CoroutineScope(Dispatchers.IO).launch {
                validateAndSave(binding,true)
            }

        }else{
        }
    }
    override fun moveBackToScreen() {
        startActivity(Intent(this@AddNotesView, HomeNotesActivity::class.java))
        finish()
    }

    override fun getDataFromAliens(list: ArrayList<NotesBO>) {
        if(list != null){
            for(data in list){
                binding.title.setText(data.notesTitle)
                binding.content.setText(data.notesContent)
            }
        }

    }

}