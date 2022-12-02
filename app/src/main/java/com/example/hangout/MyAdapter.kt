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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var flag = false

        val currentItem = newsList[position]
        //holder.image.setImageResource(R.drawable.person)
        holder.title.text = currentItem.title
        holder.category.text = currentItem.category
        holder.participants.text = currentItem.current + "/" + currentItem.parnumber
        holder.date.text = currentItem.date

        val storageReference = FirebaseStorage.getInstance().getReference()
        val profileRef = storageReference.child("users/"+currentItem.hostID+"/profile.jpg")
        profileRef.downloadUrl.addOnSuccessListener { uri ->
            Picasso.get().load(uri).into(holder.profileImage)
        }

        holder.btnAttend.setOnClickListener {
            holder.btnAttend.text = "Attended"
            if (!flag) {
                Toast.makeText(context, "You are attended!", Toast.LENGTH_SHORT).show()

                val documentID: String = UUID.randomUUID().toString()
                val documentReference: DocumentReference = FirebaseFirestore.getInstance().collection("eventDetails").document(documentID)
                val attendee: HashMap<String, String> = HashMap<String, String>()
                attendee.put("eventID", newsList[position].eventID)
                attendee.put("participantID", newsList[position].userID)
                documentReference.set(attendee).addOnSuccessListener { Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!") }
                    .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document", e) }

                val x = newsList[position].current.toInt() + 1
                Toast.makeText(context, x.toString(), Toast.LENGTH_SHORT).show()
                FirebaseFirestore.getInstance().collection("events").document(newsList[position].eventID).update("current", x.toString())
                holder.participants.text=x.toString()+"/"+currentItem.parnumber
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

        val image : ImageView = itemView.findViewById(R.id.eventImage)
        val profileImage: ImageView = itemView.findViewById(R.id.profileImage)
        val title : TextView = itemView.findViewById(R.id.title)
        val category : TextView = itemView.findViewById(R.id.category)
        val participants : TextView = itemView.findViewById(R.id.goings)
        val date : TextView = itemView.findViewById(R.id.date)
        val btnAttend: Button = itemView.findViewById<Button>(R.id.btnAttend)
        val btnInfo: Button = itemView.findViewById(R.id.btnInfo)



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
