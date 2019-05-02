package edu.gwu.gwelp


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Toast

class DisplayActivity : AppCompatActivity() {

    // private val displayManager: YelpManager = YelpManager()
    private lateinit var recyclerView: RecyclerView

    //creates recyclerView of Alerts
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this) //as RecyclerView.LayoutManager?
        val stringBR: MutableList<BusinessWithReviews> =
            intent.getSerializableExtra("businessReview") as MutableList<BusinessWithReviews>
        //println(stringBR[0])
        if (stringBR.isNotEmpty()) {
            recyclerView.adapter = DisplayAdapter(stringBR)
        } else {
            Toast.makeText(this@DisplayActivity, "No Reviews to show", Toast.LENGTH_LONG).show()
        }
    }
}
