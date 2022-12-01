package com.example.recylerviewkotlin

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.hangout.Events
import com.example.hangout.R
import com.google.android.material.imageview.ShapeableImageView

class MyAdapter(private val newsList : ArrayList<Events>, private val context: Context) : RecyclerView.Adapter<MyAdapter.MyViewHolder>(),Filterable {

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
            R.layout.item,
            parent,false)

        return MyViewHolder(itemView,mListener)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var flag = false

        val currentItem = newsList[position]
        holder.image.setImageResource(R.drawable.person)
        holder.title.text = currentItem.title
        holder.category.text = currentItem.category
        holder.participants.text = currentItem.parnumber
        holder.location.text = currentItem.place

        holder.btnAttend.setOnClickListener {

            holder.btnAttend.text = "Attended"
            if (!flag) {
                Toast.makeText(context, "Bravo!", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(context, "You are already attended.", Toast.LENGTH_SHORT).show()

            }
            flag = true
        }
    }

    override fun getItemCount(): Int {

        return newsList.size
    }

    class MyViewHolder(itemView : View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){

        val image : ImageView = itemView.findViewById(R.id.image)
        val title : TextView = itemView.findViewById(R.id.title)
        val category : TextView = itemView.findViewById(R.id.category)
        val participants : TextView = itemView.findViewById(R.id.participants)
        val location : TextView = itemView.findViewById(R.id.location)
        val btnAttend: Button = itemView.findViewById<Button>(R.id.btnAttend)



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
