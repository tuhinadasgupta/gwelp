package edu.gwu.gwelp


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast

class DisplayActivity : AppCompatActivity() {

   // private val displayManager: YelpManager = YelpManager()
    private lateinit var recyclerView: RecyclerView
    //creates recyclerView of Alerts
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val stringBR: List<BusinessWithReviews> =intent.getSerializableExtra("businessReview") as List<BusinessWithReviews>
        for(i in stringBR){
            println(stringBR)
        }
//        displayManager.retrieveBusinessWithReviews(
//            successCallback =
// { reviewsWB ->
//                runOnUiThread {
//                    if (reviewsWB.isNotEmpty()){
//                        recyclerView.adapter = DisplayAdapter(reviewsWB)
//                    }
//                    else{
//                        // defensive error check in case of nothing to display
//                        Toast.makeText(this@DisplayActivity, "Nothing to show", Toast.LENGTH_LONG).show()
//                    }
//                }
//            },
//            errorCallback = {
//                runOnUiThread {
//                    // defensive error check if something goes wrong
//                    Toast.makeText(this@DisplayActivity, "Error retrieving information", Toast.LENGTH_LONG).show()
//                }
//            }
//        )

    }
}
