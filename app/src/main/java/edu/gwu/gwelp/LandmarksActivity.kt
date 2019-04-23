package edu.gwu.gwelp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import org.json.JSONArray
import java.io.File

class LandmarksActivity: AppCompatActivity()  {
    companion object {
        val LANDMARK_NAMES: List<String> =
            listOf(
                "UN Foundation",
                "Farragut West Metro Station",
                "Foggy Bottom Metro Station",
                "National Museum of Women in the Arts",
                "Lincoln Memorial",
                "The White House",
                "World Bank Group",
                "AMC Georgetown 14",
                "Dupont Circle",
                "Farragut Square"
            )
    }

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landmarks)

        recyclerView = findViewById(R.id.recyclerView)

        // Set the direction of list to be vertical
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Create the adapter and assign it to the RecyclerView
        recyclerView.adapter = LandmarksAdapter(LANDMARK_NAMES)

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
                    yelpBusiness.lat > gworldLat - 1
                    && yelpBusiness.lat < gworldLat + 1
                    && yelpBusiness.lon > gworldLon - 1
                    && yelpBusiness.lon < gworldLon + 1
                    && yelpBusiness.name == gworldName
                ) {
                    // Yelp Business Reviews API call using yelpBusiness.id
                    // Go to next activity displaying review excerpts
//                    val yelpManager = YelpManager()
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

}