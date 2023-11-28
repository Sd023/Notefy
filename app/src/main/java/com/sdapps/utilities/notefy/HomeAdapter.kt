package com.sdapps.utilities.notefy

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

class HomeAdapter(val list : ArrayList<NotesBO>) : RecyclerView.Adapter<HomeAdapter.ViewHolder>(), NewNotesInteractor.View {
    private lateinit var context: Context
    private lateinit var viewGroup: ViewGroup
    private lateinit var presenter : NewNotesPresenter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.home_cardview_item, parent, false)
        context = parent.context
        viewGroup = parent
        return ViewHolder(layout)
    }

    override fun getItemCount(): Int {
       return list.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cardTitle.text  = list[position].notesTitle

        holder.cardView.setOnClickListener {
            openView(holder,list[position].id!!)
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        var cardTitle : TextView
        var cardView : MaterialCardView

        init {
            cardTitle = itemView.findViewById(R.id.cardTitle) as TextView
            cardView = itemView.findViewById(R.id.cardView)
        }

    }
    fun removeAt(index: Int) {
        list.removeAt(index)
    }

    fun openView(holder: ViewHolder,position : Int){
        presenter = NewNotesPresenter(context)
        presenter.attachView(this, context.applicationContext)
        val intent = Intent(context.applicationContext, AddNotesView::class.java)
        intent.putExtra("POSITION", position)
        intent.putExtra("isUpdateScreen", true)
        holder.itemView.context.startActivity(intent)
        (context as Activity).finish()
    }

    override fun moveBackToScreen() {
    }

    override fun getDataFromAliens(list: ArrayList<NotesBO>) {
    }

}