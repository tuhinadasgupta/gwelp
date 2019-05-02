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
    private var businessWithReviews: MutableList<BusinessWithReviews> = mutableListOf()

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

            yelpManager.retrieveBusinesses(
                apiKey = getString(R.string.yelp_api_key),
                address = selected,
                successCallback = {businesses ->
                    businessesList.clear()
                    businessesList.addAll(businesses)
                    findGworld(businessesList, gworldList)
                    Log.d("LandmarksActivity", "reviewsList: $reviewsList")
                },
                errorCallback = {
                    runOnUiThread {
                        Toast.makeText(this@LandmarksActivity, getString(R.string.business_error), Toast.LENGTH_LONG).show()
                    }
                }
            )

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
        reviewsList.clear()
        var matchCount = 0
        Log.d("LandmarksActivity","findGworld called")
        // Loops to get only 3 matches
        yelpResponse.takeWhile{matchCount < 3}.forEach { yelpBusiness ->
            gworlds.takeWhile{matchCount < 3}.forEach { gworldBusiness ->
                if (
                    yelpBusiness.lat.compareWithThreshold(gworldBusiness.lat, .005)
                    && yelpBusiness.lon.compareWithThreshold(gworldBusiness.lon, .005)
                    && yelpBusiness.name == gworldBusiness.name
                ) {
                    Log.d("LandmarksActivity","it's a match! $gworldBusiness")
                    matchCount += 1
                    Log.d("LandmarksActivity", "matchCount = $matchCount")
                    // Yelp Business Reviews API call using yelpBusiness.id
                    businessWithReviews.add(BusinessWithReviews(yelpBusiness,ArrayList(reviewsList)));
                    reviewsList.addAll(
                        yelpManager.retrieveReviews(
                            getString(R.string.yelp_api_key),
                            yelpBusiness.id
                        )
                    )
                } else {
                    Log.d("LandmarksActivity","not a match")
                }
            }
        }
        runOnUiThread {
            //Toast.makeText(this@LandmarksActivity, reviewsList[0].yelper_name, Toast.LENGTH_LONG).show()
            // ***Go to new activity here to display results***
            val intent = Intent(this, DisplayActivity::class.java)
            intent.putExtra("businessReview", ArrayList(businessWithReviews))
            startActivity(intent)
        }
    }

    fun Double.compareWithThreshold(other: Double, threshold: Double): Boolean {
        return this >= other && this <= other + threshold ||
                this <= other && this >= other - threshold
    }

}