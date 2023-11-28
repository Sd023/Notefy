package com.sdapps.utilities.notefy

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sdapps.utilities.notefy.databinding.ActivityAddNotesViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class AddNotesView : AppCompatActivity(), NewNotesInteractor.View {

    private lateinit var binding : ActivityAddNotesViewBinding
    private lateinit var presenter: NewNotesInteractor.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNotesViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = NewNotesPresenter(applicationContext)
        presenter.attachView(this,applicationContext)
        var bundle = Bundle()
        bundle = intent.extras!!
        val position = bundle.getInt("POSITION")
        val isUpdateScreen = bundle.getBoolean("isUpdateScreen")

        if(isUpdateScreen)
                (presenter as NewNotesPresenter).sendData(position)


        binding.backBtn.setOnClickListener{
            val title : String = binding.title.text.toString()
            val content : String = binding.content.text.toString()
            if(!title.trim().isEmpty() && !content.trim().isEmpty() && !isUpdateScreen){
                saveNotes(title,content)
            }else if(!title.trim().isEmpty() && !content.trim().isEmpty() && isUpdateScreen){
                updateNotes(title,content,position)
            } else{
                onBackPressed()
            }
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