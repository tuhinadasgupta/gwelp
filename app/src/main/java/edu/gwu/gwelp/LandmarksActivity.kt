package edu.gwu.gwelp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

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

}