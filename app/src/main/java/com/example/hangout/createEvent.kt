package com.example.hangout

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class createEvent : AppCompatActivity() {
    private lateinit var edtTitle: EditText
    private lateinit var edtDate: EditText
    private lateinit var edtTime: EditText
    private lateinit var edtParNumber: EditText
    private lateinit var edtDescription: EditText
    private lateinit var switchFlag: Switch
    private lateinit var finishEvents: Button
    private lateinit var spinnerC: Spinner
    private lateinit var spinnerP: Spinner
    private lateinit var backPage: ImageView

    private val categories = arrayOf("Sport", "Board Game", "Talk", "Reading", "Watching")
    private val places = arrayOf("Computer Engineering Building", "Library", "SKS", "Kelebek", "Molecular Biology Building", "Material Engineering Building")
    private var selectedCategory = categories[0]
    private var selectedPlace = places[0]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        val userID = intent.getStringExtra("ID")

        edtTitle=findViewById<EditText>(R.id.edt_title)
        edtDate=findViewById<EditText>(R.id.edt_date)
        edtTime=findViewById<EditText>(R.id.edt_time)
        edtParNumber=findViewById<EditText>(R.id.edt_par_number)
        edtDescription=findViewById<EditText>(R.id.edt_description)
        finishEvents=findViewById<Button>(R.id.btnFinish)
        backPage=findViewById(R.id.backPage)

        switchFlag=findViewById<Switch>(R.id.SW)
        var message: String = "Public"
        switchFlag.setOnCheckedChangeListener{ _, isChecked ->
            if (isChecked){
                message = "Private"
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
            else{
                message = "Public"
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }

        spinnerC=findViewById<Spinner>(R.id.spinnerC)
        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categories)
        spinnerC.adapter = arrayAdapter
        spinnerC.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedCategory = categories[p2]
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        spinnerP=findViewById<Spinner>(R.id.spinnerP)
        val arrayAdapter2 = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, places)
        spinnerP.adapter = arrayAdapter2
        spinnerP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedPlace = places[p2]
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        finishEvents.setOnClickListener {
            val title=edtTitle.text.toString()
            val date = edtDate.text.toString()
            val time = edtTime.text.toString()
            val parnumber=edtParNumber.text.toString()
            val description=edtDescription.text.toString()

            createEvent(title,date,time,parnumber,description,message,userID!!)
        }

        backPage.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("ID", userID)
            startActivity(intent)
        }
    }
    private fun createEvent(title: String, date: String, time: String,parnumber:String,description:String,message:String,userID:String) {
        if (title.length == 0 || date.length == 0 || time.length == 0 || parnumber.length == 0 || description.length == 0){
            Toast.makeText(this, "Inputs cannot be empty.", Toast.LENGTH_SHORT).show()
            return
        }
        if (parnumber.toInt() <= 0){
            Toast.makeText(this, "Invalid participant numbers.", Toast.LENGTH_SHORT).show()
            return
        }
        val eventID: String = UUID.randomUUID().toString()
        val documentReference:DocumentReference = FirebaseFirestore.getInstance().collection("events").document(eventID)
        val events: HashMap<String, String> = HashMap<String, String>()
        events.put("eventID", eventID)
        events.put("hostID", userID)
        events.put("title", title)
        events.put("date", date)
        events.put("time", time)
        events.put("category", selectedCategory)
        events.put("place", selectedPlace)
        events.put("current", "0")
        events.put("parnumber", parnumber)
        events.put("description", description)
        events.put("private", message)


        documentReference.set(events).addOnSuccessListener { Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!") }
         .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document", e) }

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("ID", userID)
        startActivity(intent)
    }
}