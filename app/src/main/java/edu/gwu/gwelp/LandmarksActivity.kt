package edu.gwu.gwelp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import org.json.JSONArray
import java.io.File
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import org.jetbrains.anko.doAsync
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class LandmarksActivity: AppCompatActivity(), AdapterView.OnItemSelectedListener  {
    private lateinit var spinner: Spinner
    private val yelpManager: YelpManager = YelpManager()
    private val businessesList: MutableList<Business> = mutableListOf()
    private val gworldList: MutableList<Business> = mutableListOf()
    private val reviewsList: MutableList<Review> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spinner)
        spinner = findViewById(R.id.spinner)

        parseGworldFile()

        ArrayAdapter.createFromResource(
            this,
            R.array.places_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        val selected : String
        if (pos != 0) {
             selected = parent.getItemAtPosition(pos) as String
//            Toast.makeText(
//                this,
//                "$selected was selected!",
//                Toast.LENGTH_LONG
//            ).show()

            yelpManager.retrieveBusinesses(
                apiKey = getString(R.string.yelp_api_key),
                address = selected,
                successCallback = {businesses ->
                    runOnUiThread {
                        businessesList.clear()
                        businessesList.addAll(businesses)
                        // Testing if i can get the name of the first result
                        val test = businessesList[0].name
                        Toast.makeText(this@LandmarksActivity, "0th result name: $test", Toast.LENGTH_LONG).show()
                        //Toast.makeText(this@LandmarksActivity, "Successfully retrieved businesses", Toast.LENGTH_LONG).show()
                    }
                },
                errorCallback = {
                    runOnUiThread {
                        Toast.makeText(this@LandmarksActivity, getString(R.string.business_error), Toast.LENGTH_LONG).show()
                    }
                }
            )

            findGworld(businessesList, gworldList)

        }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
        Toast.makeText(
            this,
            "Please select one of the landmarks!",
            Toast.LENGTH_LONG
        ).show()

    }

    // Parses gworld.json for future comparison with yelp businesses list
    fun parseGworldFile() {
        gworldList.clear()
        val reader = BufferedReader(
            InputStreamReader(getAssets().open("gworld.json"), "UTF-8")
        )
        var resultsString: String? = null
        try {
            val results = StringBuilder()
            while (true) {
                val line = reader.readLine()
                if (line == null) break
                results.append(line)
            }
           resultsString = results.toString()
        }
        catch (e:IOException) {
            Toast.makeText(this@LandmarksActivity, "Uh oh something went wrong!", Toast.LENGTH_LONG).show()
        }
        finally {
            reader.close()
        }

        val jsonArray = JSONArray(resultsString)
        for (i in 0 until jsonArray.length()) {
            val curr = jsonArray.getJSONObject(i)
            val gworldName = curr.getString("name")
            val gworldAddress = curr.getString("address")
            val gworldLat = curr.getDouble("lat")
            val gworldLon = curr.getDouble("lon")
            gworldList.add (
                Business(
                    name = gworldName,
                    id = "",
                    address = gworldAddress,
                    lat = gworldLat,
                    lon = gworldLon
                )
            )

        }
    }

    // Compares list of yelp businesses to list of gworld businesses
    // Calls yelp api to retrieve review excerpts
    fun findGworld(yelpResponse: List<Business>, gworlds: List<Business>) {
        Log.d("LandmarksActivity","findGworld called")
        yelpResponse.forEach { yelpBusiness ->
            Log.d("LandmarksActivity", "after yelpResponse.forEach")
            gworlds.forEach { gworldBusiness ->
                if (
                    yelpBusiness.lat.compareWithThreshold(gworldBusiness.lat, .015)
                    && yelpBusiness.lon.compareWithThreshold(gworldBusiness.lon, .015)
                    && yelpBusiness.name == gworldBusiness.name
                ) {
                    Log.d("LandmarksActivity","inside the if statement")
                    // Yelp Business Reviews API call using yelpBusiness.id
                    yelpManager.retrieveReviews(
                        apiKey = getString(R.string.yelp_api_key),
                        businessId = yelpBusiness.id,
                        successCallback = { reviews ->
                            Log.d("LandmarksActivity","in successCallback")
                            runOnUiThread {
                                reviewsList.clear()
                                reviewsList.addAll(reviews)
                                // Testing if i can get the reviewer name of the first result
                                val test = reviewsList[0].yelper_name
                                Toast.makeText(this@LandmarksActivity, test, Toast.LENGTH_LONG).show()
                            }
                        },
                        errorCallback = {
                            runOnUiThread {
                                Toast.makeText(this@LandmarksActivity, getString(R.string.review_error), Toast.LENGTH_LONG).show()
                            }
                        }
                    )
                } else {
                    Log.d("LandmarksActivity","inside the else")
                    runOnUiThread {
                        Toast.makeText(this@LandmarksActivity, getString(R.string.no_results), Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    fun Double.compareWithThreshold(other: Double, threshold: Double): Boolean {
        return this >= other && this <= other + threshold ||
                this <= other && this >= other - threshold
    }

}