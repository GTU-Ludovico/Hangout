package com.example.recylerviewkotlin

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.hangout.Events
import com.example.hangout.R
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class MyCreatedAdapter(private val newsList : ArrayList<Events>, private val context: Context) : RecyclerView.Adapter<MyCreatedAdapter.MyViewHolder>(),Filterable {

    private lateinit var mListener : onItemClickListener

    interface onItemClickListener{

        fun onItemClick(position : Int)

    }

    fun setOnItemClickListener(listener: onItemClickListener){

        mListener = listener

    }

    fun deleteItem(i : Int){

        newsList.removeAt(i)
        notifyDataSetChanged()

    }

    fun addItem(i : Int, news : Events){

        newsList.add(i, news)
        notifyDataSetChanged()

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.cards_events_created,
            parent,false)

        return MyViewHolder(itemView,mListener)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = newsList[position]
        holder.date.text = currentItem.date
        holder.title.text = currentItem.title
        holder.description.text = currentItem.description
        holder.time.text = currentItem.time
        holder.location.text = currentItem.place

        holder.cancel.setOnClickListener {
            //TO BE IMPLEMENTED
        }
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    class MyViewHolder(itemView : View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){
        val date : TextView = itemView.findViewById(R.id.date)
        val title : TextView = itemView.findViewById(R.id.title)
        val description : TextView = itemView.findViewById(R.id.description)
        val time : TextView = itemView.findViewById(R.id.time)
        val location : TextView = itemView.findViewById(R.id.location)
        val cancel: ImageView = itemView.findViewById(R.id.cancel)

        init {

            itemView.setOnClickListener {

                listener.onItemClick(adapterPosition)

            }



        }

    }

    override fun getFilter(): Filter {
        TODO("Not yet implemented")
    }

}
