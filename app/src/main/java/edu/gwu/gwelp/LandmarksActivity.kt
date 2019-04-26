package edu.gwu.gwelp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.json.JSONArray
import java.io.File
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast

class LandmarksActivity: AppCompatActivity(), AdapterView.OnItemSelectedListener  {
    private lateinit var spinner: Spinner
    private val yelpManager: YelpManager = YelpManager()
    private val businessesList: MutableList<Business> = mutableListOf()

   // private lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spinner)
        spinner = findViewById(R.id.spinner)

//        recyclerView = findViewById(R.id.recyclerView)
//        recyclerView.layoutManager = LinearLayoutManager(this)

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
            Toast.makeText(
                this,
                "$selected was selected!",
                Toast.LENGTH_LONG
            ).show()

            yelpManager.retrieveBusinesses(
                apiKey = getString(R.string.yelp_api_key),
                address = selected,
                successCallback = {businesses ->
                    runOnUiThread {
                        businessesList.clear()
                        businessesList.addAll(businesses)
                        // Testing if i can get the name of the first result
                        val test = businessesList[0].name
                        Toast.makeText(this@LandmarksActivity, "first result name: $test", Toast.LENGTH_LONG).show()
                        //Toast.makeText(this@LandmarksActivity, "Successfully retrieved businesses", Toast.LENGTH_LONG).show()
                    }
                },
                errorCallback = {
                    runOnUiThread {
                        Toast.makeText(this@LandmarksActivity, "Error retrieving businesses", Toast.LENGTH_LONG).show()
                    }
                }
            )

            //new intent page to be opened
//            val intent = Intent(this, ::class.java)
//            intent.putExtra("Landmark", selected)
//            startActivity(intent)
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

    fun findGworldRestaurants(yelpResponse: List<Business>) {
        val gworldString: String = File("/gworld.json").readText(Charsets.UTF_8)
        val jsonArray = JSONArray(gworldString)
        yelpResponse.forEach  { yelpBusiness->
            for (i in 0 until jsonArray.length()) {
                val curr = jsonArray.getJSONObject(i)
                val gworldName = curr.getString("name")
                val gworldLat = curr.getDouble("lat")
                val gworldLon = curr.getDouble("lon")

                if (
                    yelpBusiness.lat.compareWithThreshold(gworldLat, .02)
                    && yelpBusiness.lon.compareWithThreshold(gworldLon, .02)
                    && yelpBusiness.name == gworldName
                ) {
                    // Yelp Business Reviews API call using yelpBusiness.id
                    // Go to next activity displaying review excerpts
//                    yelpManager.retrieveReviews(
//                        apiKey = getString(R.string.yelp_api_key),
//                        businessId = yelpBusiness.id,
//                        successCallback = {},
//                        errorCallback = {}
//                    )
                }
            }
        }

    }

    fun Double.compareWithThreshold(other: Double, threshold: Double): Boolean {
        return this >= other && this <= other + threshold ||
                this <= other && this >= other - threshold
    }

}