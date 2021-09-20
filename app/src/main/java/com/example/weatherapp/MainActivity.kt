package com.example.weatherapp

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemSelectedListener
import androidx.annotation.RequiresApi
import androidx.core.text.isDigitsOnly
import androidx.core.view.isVisible
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.*
import org.json.JSONArray
import java.lang.Runnable
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity() {
    lateinit var check: Button
    lateinit var register: Button

    lateinit var phone: EditText
    lateinit var dob: EditText
    lateinit var name: EditText
    lateinit var gender: Spinner
    lateinit var pincode: EditText
    lateinit var address1: EditText
    lateinit var address2: EditText

    lateinit var state: TextView
    lateinit var distric: TextView

    lateinit var arrayAdapter: ArrayAdapter<String>
    val genderList = ArrayList(
        listOf<String>("Male", "Female", "Other")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewInitializer()
        genderInitializer()
        check.isEnabled = false
        register.isEnabled = false
        dob.setOnClickListener(View.OnClickListener { dobIntializer() })
        enableChekButton()

    }

    private fun enableChekButton() = runBlocking {
        CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                if (phone.text.length == 10 && name.text.isNotBlank() && address1.text.isNotBlank() && pincode.length() == 6 && dob.text.isNotBlank()) {
                    check.isEnabled = true
                    register.isEnabled = true
                } else {
                    check.isEnabled = false
                    register.isEnabled = false
                }
                delay(1000)
            }
        }
    }


    private fun viewInitializer() {
        check = findViewById(R.id.check)
        register = findViewById(R.id.register)
        phone = findViewById(R.id.phone)
        dob = findViewById(R.id.dob)
        name = findViewById(R.id.name)
        gender = findViewById(R.id.gender)
        pincode = findViewById(R.id.pincode)
        address1 = findViewById(R.id.address1)
        address2 = findViewById(R.id.address2)

        distric = findViewById(R.id.distric)
        state = findViewById(R.id.state)
        state = findViewById(R.id.state)
    }

    private fun genderInitializer() {
        arrayAdapter = ArrayAdapter<String>(
            this@MainActivity,
            android.R.layout.simple_spinner_dropdown_item, genderList
        )
        gender.adapter = arrayAdapter
    }

    private fun dobIntializer() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dpd = DatePickerDialog(
            this@MainActivity,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox
                dob.setText("" + dayOfMonth + " / " + month + " / " + year)

            },
            year,
            month,
            day
        )

        dpd.show()
    }


    fun featchingData(view: android.view.View) = runBlocking {
        CoroutineScope(Dispatchers.Main).launch {
            var url = "https://api.postalpincode.in/pincode/" + pincode.text


            val jsonQueue = Volley.newRequestQueue(this@MainActivity)
            var jsonArrayRequest = JsonArrayRequest(Request.Method.GET, url, null, {
                val Distric: String =
                    it.getJSONObject(0).getJSONArray("PostOffice").getJSONObject(0)
                        .getString("District")
                val State: String = it.getJSONObject(0).getJSONArray("PostOffice").getJSONObject(0)
                    .getString("State")
                state.text = "State : " + State
                distric.text = "Distric : " + Distric
            }, {
                Log.d("error", it.toString())
            })
            jsonQueue.add(jsonArrayRequest)
        }

    }

    fun toRegister(view: android.view.View) = runBlocking {
        CoroutineScope(Dispatchers.Main).launch {
            var intent: Intent = Intent(this@MainActivity, WeatherToday::class.java)
            intent.putExtra("city", distric.text.toString())
            startActivity(intent)
        }


    }


}