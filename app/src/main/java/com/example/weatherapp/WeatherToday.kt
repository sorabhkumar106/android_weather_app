package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jetbrains.annotations.NotNull
import org.w3c.dom.Text

class WeatherToday : AppCompatActivity() {
    lateinit var city_name: EditText
    lateinit var show_btn: Button
    lateinit var tempraturec: TextView
    lateinit var tempraturef: TextView
    lateinit var Latitude: TextView
    lateinit var Longitude: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_today)
        viewIntializer()
    }

    private fun viewIntializer() {
        city_name = findViewById(R.id.city_name)
        show_btn = findViewById(R.id.show_btn)
        tempraturec = findViewById(R.id.tempraturec)
        tempraturef = findViewById(R.id.tempraturef)
        Latitude = findViewById(R.id.latitude)
        Longitude = findViewById(R.id.longitude)

    }

    fun callApi(view: android.view.View) = runBlocking {
        CoroutineScope(Dispatchers.Main).launch {
            if (city_name.text.isNotBlank()) {
                var city = city_name.text.toString().toLowerCase()
                var url =
                    "http://api.weatherapi.com/v1/current.json?key=34bb2f5e0ced4bcea4f181743211909&q=" + city + "&aqi=yes"
                val jsonQueue = Volley.newRequestQueue(this@WeatherToday)
                var jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null, {

                    val latitude = it.getJSONObject("location").getString("lat")
                    val longitude = it.getJSONObject("location").getString("lon")
                    val tempc = it.getJSONObject("current").getString("temp_c")
                    val tempf = it.getJSONObject("current").getString("temp_f")
                    var title = "Temprture in "
                    tempraturec.text = title + tempc.toString() + " C"
                    tempraturef.text = title + tempf.toString() + "F"
                    Longitude.text = "Longitude :" + longitude.toString()
                    Latitude.text = "Latitude :" + latitude.toString()

                }, {
                    Toast.makeText(applicationContext, "Enter Valid City Name", Toast.LENGTH_SHORT)
                        .show()
                })
                jsonQueue.add(jsonObjectRequest)

            } else {
                Toast.makeText(applicationContext, "First enter city", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onCreatePanelMenu(featureId: Int, @NotNull menu: Menu): Boolean {
        var menuInflater: MenuInflater = menuInflater
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreatePanelMenu(featureId, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.setting -> Toast.makeText(applicationContext,"Setting Clicked",Toast.LENGTH_SHORT).show()
            R.id.share -> Toast.makeText(applicationContext,"Share Clicked",Toast.LENGTH_SHORT).show()
            R.id.more -> Toast.makeText(applicationContext,"Moare Clicked",Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }
}