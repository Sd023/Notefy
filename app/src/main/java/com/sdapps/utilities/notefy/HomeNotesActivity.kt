package com.sdapps.utilities.notefy

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.transition.Explode
import android.view.Window
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sdapps.utilities.notefy.databinding.ActivityMainBinding
import com.sdapps.utilities.notefy.db.DbHandler

class HomeNotesActivity : Activity(), HomeNotesInteractor.View {


    private lateinit var binding : ActivityMainBinding
    private var presenter = HomeNotesPresenter(this)
    private lateinit var dbHandler : DbHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter.attachView(this,applicationContext)

        dbHandler  = DbHandler(applicationContext, DataMember.DB_NAME)
        dbHandler.createDB()

        updateUI()
        binding.fab.setOnClickListener {
            switchToAddView()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    fun switchToAddView(){
        val intent = Intent(this, AddNotesView::class.java)
        val bundle = Bundle()
        bundle.putInt("POSITION", 0)
        bundle.putBoolean("isUpdateScreen", false)

        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }

    override fun showLoading() {
        TODO("Not yet implemented")
    }

    override fun hideLoading() {
        TODO("Not yet implemented")
    }

    override fun showAlert() {
        TODO("Not yet implemented")
    }


    override fun updateUI() {
        val values = presenter.getAllNotesData()
        binding.recyclerView.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        val adapter = HomeAdapter(values)
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(v: RecyclerView, h: RecyclerView.ViewHolder, t: RecyclerView.ViewHolder) = false
            override fun onSwiped(h: RecyclerView.ViewHolder, dir: Int){
                presenter.deleteNotesByPosition(h.adapterPosition)
                adapter.removeAt(h.adapterPosition)
                adapter.notifyDataSetChanged()
            }
        }).attachToRecyclerView(binding.recyclerView)
        binding.recyclerView.adapter = adapter
    }
}