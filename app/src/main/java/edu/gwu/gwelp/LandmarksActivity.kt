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
import org.jetbrains.anko.doAsync
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class LandmarksActivity: AppCompatActivity(), AdapterView.OnItemSelectedListener  {
    private lateinit var spinner: Spinner
    private val yelpManager: YelpManager = YelpManager()
    private val businessesList: MutableList<Business> = mutableListOf()
    private val gworldList: MutableList<Business> = mutableListOf()
    private val reviews: MutableList<Review> = mutableListOf()

    private val gworldJson = """
        [{
        "name": "&pizza",
        "address": "2224 F St NW, Washington, DC 20037",
        "lat": 38.897110,
        "lon": -77.049591
        },
        {
        "name": "7th Hill Pizza Palisades",
        "address": "4885 MacArthur Blvd NW, Washington, DC 20007",
        "lat": 38.917827,
        "lon": -77.096881
        },
        {
        "name": "7-Eleven",
        "address": "514 19th St NW, Washington, DC 20052",
        "lat": 38.896614,
        "lon": -77.043693
        },
        {
        "name": "Abunai Poke",
        "address": "1920 L St NW, Washington, DC 20036",
        "lat": 38.903500,
        "lon": -77.044480
        },
        {
        "name": "Asia 54",
        "address": "2122 P St NW, Washington, DC 20037",
        "lat": 38.909478,
        "lon": -77.096881
        },
        {
        "name": "Au Bon Pain",
        "address": "2000 Pennsylvania Ave NW, Washington, DC 20006",
        "lat": 38.900372,
        "lon": -77.045532
        },
        {
        "name": "Beefsteak",
        "address": "800 22nd St NW, Washington, DC 20052",
        "lat": 38.899971,
        "lon": -77.049156
        },
        {
        "name": "Bindaas",
        "address": "2000 Pennsylvania Ave NW, Washington, DC 20006",
        "lat": 38.900372,
        "lon": -77.045532
        },
        {
        "name": "Bobby's Burger Palace",
        "address": "2121 K St NW, Washington, DC 20037",
        "lat": 38.902910,
        "lon": -77.047250
        },
        {
        "name": "Bodega",
        "address": "3116 M St NW, Washington, DC 20007",
        "lat": 38.905060,
        "lon": -77.061770
        },
        {
        "name": "Brooklyn Sandwich",
        "address": "800 21st St NW, Washington, DC 20052",
        "lat": 38.899933,
        "lon": -77.047203
        },
        {
        "name": "Bryson's",
        "address": "1020 19th St NW, Washington, DC 20036",
        "lat": 38.903140,
        "lon": -77.043770
        },
        {
        "name": "Buredo",
        "address": "825 14th St NW, Washington, DC 20005",
        "lat": 38.900990,
        "lon": -77.032100
        },
        {
        "name": "Burger Tap & Shake Foggy Bottom",
        "address": "2200 Pennsylvania Ave NW, Washington, DC 20037",
        "lat": 38.901810,
        "lon": -77.049438
        },
        {
        "name": "Burgeria",
        "address": "2121 I St NW, Washington, DC 20052",
        "lat": 38.900928,
        "lon": -77.047722
        },
        {
        "name": "Cafe Aria",
        "address": "1917 F St NW, Washington, DC 20006",
        "lat": 38.897560,
        "lon": -77.043890
        },
        {
        "name": "California Tortilla",
        "address": "1809 E St NW, Washington, DC 20006",
        "lat": 38.896190,
        "lon": -77.042330
        },
        {
        "name": "Captain Cookie and the Milkman",
        "address": "2000 Pennsylvania Ave NW, Washington, DC 20006",
        "lat": 38.900372,
        "lon": -77.045532
        },
        {
        "name": "Carvings",
        "address": "2021 F St NW, Washington, DC 20006",
        "lat": 38.897360,
        "lon": -77.048090
        },
        {
        "name": "Chalin's Restaurant",
        "address": "1912 I St NW, Washington, DC 20006",
        "lat": 38.901190,
        "lon": -77.044060
        },
        {
        "name": "Char Bar",
        "address": "2142 L St NW, Washington, DC 20037",
        "lat": 38.903590,
        "lon": -77.048340
        },
        {
        "name": "Charm Thai",
        "address": "2514 L St NW, Washington, DC 20037",
        "lat": 38.903490,
        "lon": -77.054260
        },
        {
        "name": "Chick-fil-A",
        "address": "2121 H St NW, Washington, DC 20052",
        "lat": 38.899840,
        "lon": -77.047860
        },
        {
        "name": "Chipotle",
        "address": "2000 Pennsylvania Ave NW, Washington, DC 20006",
        "lat": 38.900372,
        "lon": -77.045532
        },
        {
        "name": "Chipotle",
        "address": "1837 M St NW, Washington, DC 20036",
        "lat": 38.905860,
        "lon": -77.043220
        },
        {
        "name": "Chopt Creative Salad Co.",
        "address": "1105 1/2 19th St NW, Washington, DC 20036",
        "lat": 38.908950,
        "lon": -77.044540
        },
        {
        "name": "Chopt Creative Salad Co.",
        "address": "1730 Pennsylvania Ave NW, Washington, DC 20006",
        "lat": 38.898708,
        "lon": -77.040398
        },
        {
        "name": "The Coffee Cart",
        "address": "2122 H St NW, Washington, DC 20052",
        "lat": 38.899190,
        "lon": -77.047560
        },
        {
        "name": "Crepeaway",
        "address": "2001 L St NW, Washington, DC 200362",
        "lat": 38.904030,
        "lon": -77.045320
        },
        {
        "name": "Crepe N Creme",
        "address": "2818 Pennsylvania Ave NW, Washington, DC 20007",
        "lat": 38.904960,
        "lon": -77.057690
        },
        {
        "name": "DC Pizza",
        "address": "1103 19th St NW, Washington, DC 20036",
        "lat": 38.904010,
        "lon": -77.043170
        },
        {
        "name": "Devon and Blakely",
        "address": "2200 Pennsylvania Ave NW, Washington, DC 20052",
        "lat": 38.902490,
        "lon": -77.050780
        },
        {
        "name": "Domino's Pizza",
        "address": "2029 K St NW, Washington, DC 20006",
        "lat": 38.902840,
        "lon": -77.046170
        },
        {
        "name": "The Dough Jar",
        "address": "1332 Wisconsin Ave NW, Washington, DC 20007",
        "lat": 38.907551,
        "lon": -77.063560
        },
        {
        "name": "Dunkin' Donuts",
        "address": "616 23rd St NW, Washington, DC 20052",
        "lat": 38.902490,
        "lon": -77.050780
        },
        {
        "name": "Flavors of India",
        "address": "2524 L St NW, Washington, DC 20037",
        "lat": 38.903490,
        "lon": -77.054320
        },
        {
        "name": "FoBoGro",
        "address": "2140 F St NW, Washington, DC 20037",
        "lat": 38.897090,
        "lon": -77.048160
        },
        {
        "name": "Froggy Bottom Pub ",
        "address": "2021 K St NW, Washington, DC 20006",
        "lat": 38.902850,
        "lon": -77.045800
        },
        {
        "name": "Gallery Cafe",
        "address": "616 23rd St NW, Washington, DC 20037",
        "lat": 38.898040,
        "lon": -77.050650
        },
        {
        "name": "Gallery Market",
        "address": "616 23rd St NW, Washington, DC 20037",
        "lat": 38.898040,
        "lon": -77.050650
        },
        {
        "name": "GCDC Grilled Cheese Bar",
        "address": "1730 Pennsylvania Ave NW, Washington, DC 20006",
        "lat": 38.898708,
        "lon": -77.040398
        },
        {
        "name": "Grk Fresh Gwu",
        "address": "2121 H St NW, Washington, DC 20052",
        "lat": 38.899840,
        "lon": -77.047860
        },
        {
        "name": "GNC",
        "address": "1754 Pennsylvania Ave NW, Washington, DC 20006",
        "lat": 38.899050,
        "lon": -77.040980
        },
        {
        "name": "GW Delicatessen",
        "address": "2133 G St NW, Washington, DC 20037",
        "lat": 38.898510,
        "lon": -77.048248
        },
        {
        "name": "GW Hospital Cafeteria",
        "address": "900 23rd St NW, Washington, DC 20037",
        "lat": 38.901199,
        "lon": -77.050484
        },
        {
        "name": "The Halal Guys",
        "address": "1331 Connecticut Ave NW, Washington, DC 20036",
        "lat": 38.908160,
        "lon": -77.042140
        },
        {
        "name": "Higher Grounds Coffee Shop",
        "address": "2100 Foxhall Rd NW, Washington, DC 20007",
        "lat": 38.918110,
        "lon": -77.090260
        },
        {
        "name": "HomeSlyce",
        "address": "2121 K St NW, Washington, DC 20037",
        "lat": 38.902908,
        "lon": -77.047249
        },
        {
        "name": "Jetties",
        "address": "1921 I St NW, Washington, DC 20006",
        "lat": 38.901560,
        "lon": -77.044560
        },
        {
        "name": "Jetties",
        "address": "1609 Foxhall Rd NW, Washington, DC 20007",
        "lat": 38.911289,
        "lon": -77.084030
        },
        {
        "name": "JRINK Juicery",
        "address": "1922 I St NW, Washington, DC 20006",
        "lat": 38.901180,
        "lon": -77.044500
        },
        {
        "name": "Kaz Sushi Bistro",
        "address": "1915 I St NW, Washington, DC 20006",
        "lat": 38.901540,
        "lon": -77.044260
        },
        {
        "name": "Laoban Dumplings",
        "address": "2000 Pennsylvania Ave NW, Washington, DC 20006",
        "lat": 38.900372,
        "lon": -77.045532
        },
        {
        "name": "Los Cuates",
        "address": "1564 Wisconsin Ave NW, Washington, DC 20007",
        "lat": 38.910460,
        "lon": -77.065100
        },
        {
        "name": "Magic Gourd Restaurant",
        "address": "528 23rd St NW, Washington, DC 20037",
        "lat": 38.896800,
        "lon": -77.050680
        },
        {
        "name": "Manny & Olga's Pizza",
        "address": "1641 Wisconsin Ave NW, Washington, DC 20007",
        "lat": 38.911490,
        "lon": -77.065360
        },
        {
        "name": "1 Fish 2 Fish",
        "address": "2423 Pennsylvania Ave NW, Washington, DC 20037",
        "lat": 38.903540,
        "lon": -77.052290
        },
        {
        "name": "Paisano's",
        "address": "1815 Wisconsin Ave NW Suite C, Washington, DC 20007",
        "lat": 38.915240,
        "lon": -77.067200
        },
        {
        "name": "Panera Bread",
        "address": "800 21st St NW, Washington, DC 20052",
        "lat": 38.899933,
        "lon": -77.047203
        },
        {
        "name": "Papa John's Pizza",
        "address": "2525 Pennsylvania Ave NW, Washington, DC 20037",
        "lat": 38.904170,
        "lon": -77.053930
        },
        {
        "name": "PAUL USA",
        "address": "2000 Pennsylvania Ave NW, Washington, DC 20006",
        "lat": 38.900372,
        "lon": -77.045532
        },
        {
        "name": "Peet's Coffee",
        "address": "2121 H St NW, Washington, DC 20052",
        "lat": 38.899840,
        "lon": -77.047860
        },
        {
        "name": "Pelham Commons Cafe",
        "address": "2100 Foxhall Rd NW, Washington, DC 20007",
        "lat": 38.918110,
        "lon": -77.090260
        },
        {
        "name": "Penn Grill",
        "address": "825 20th St NW, Washington, DC 20006",
        "lat": 38.901000,
        "lon": -77.044690
        },
        {
        "name": "The Perfect Pita",
        "address": "2000 Pennsylvania Ave NW, Washington, DC 20006",
        "lat": 38.900372,
        "lon": -77.045532
        },
        {
        "name": "Pizza Movers and Calzones",
        "address": "1618 Wisconsin Ave NW, Washington, DC 20007",
        "lat": 38.911490,
        "lon": -77.065560
        },
        {
        "name": "Point Chaud Cafe & Crepes",
        "address": "2201 G St NW, Washington, DC 20052",
        "lat": 38.898602,
        "lon": -77.049469
        },
        {
        "name": "Poppabox",
        "address": "1928 I St NW, Washington, DC 20006",
        "lat": 38.901190,
        "lon": -77.044720
        },
        {
        "name": "Potbelly Sandwich Shop",
        "address": "616 23rd St NW, Washington, DC 20037",
        "lat": 38.898041,
        "lon": -77.050652
        },
        {
        "name": "Rasoi Indian Kitchen",
        "address": "1810 K Street NW K Street Entrance, Washington, DC 20006",
        "lat": 38.902220,
        "lon": -77.042530
        },
        {
        "name": "Roti Modern Mediterranean",
        "address": "2221 I St NW, Washington, DC 20037",
        "lat": 38.901020,
        "lon": -77.049858
        },
        {
        "name": "Safeway",
        "address": "1855 Wisconsin Ave NW, Washington, DC 20007",
        "lat": 38.916510,
        "lon": -77.065900
        },
        {
        "name": "Sizzling Express",
        "address": "538 23rd St NW, Washington, DC 20037",
        "lat": 38.896970,
        "lon": -77.051160
        },
        {
        "name": "Sol Mexican Grill",
        "address": "2121 H St NW, Washington, DC 20052",
        "lat": 38.899840,
        "lon": -77.047860
        },
        {
        "name": "South Block",
        "address": "2301 G St NW, Washington, DC 20037",
        "lat": 38.898618,
        "lon": -77.050517
        },
        {
        "name": "Starbucks",
        "address": "1957 E St NW, Washington, DC 20052",
        "lat": 38.896291,
        "lon": -77.04424,
        },
        {
        "name": "Starbucks",
        "address": "2130 H St NW, Washington, DC 20052",
        "lat": 38.8992,
        "lon": -77.048419
        },
        {
        "name": "Starbucks",
        "address": "900 23rd St NW, Washington, DC 20037",
        "lat": 38.901584,
        "lon": -77.050338
        },
        {
        "name": "Subway",
        "address": "900 23rd St NW, Washington, DC 20037",
        "lat": 38.901584,
        "lon": -77.050338,
        },
        {
        "name": "Subway",
        "address": "1959 E St NW Space C, Washington, DC 20052",
        "lat": 38.89604,
        "lon": -77.04429
        },
        {
        "name": "Surfside Taco Stand",
        "address": "1800 N St NW, Washington, DC 20036",
        "lat": 38.907096,
        "lon": -77.042621
        },
        {
        "name": "sweetgreen",
        "address": "2221 I St NW, Washington, DC 20052",
        "lat": 38.900982,
        "lon": -77.049623
        },
        {
        "name": "Taj of India",
        "address": "2809 M St NW, Washington, DC 20007",
        "lat": 38.905495,
        "lon": -77.057366
        },
        {
        "name": "Tasty Kabob",
        "address": "2130 H St NW, Washington, DC 20052",
        "lat": 38.8992,
        "lon": -77.048419
        },
        {
        "name": "Thunder Burger & Bar",
        "address": "3056 M St NW, Washington, DC 20007",
        "lat": 38.904985,
        "lon": -77.060617
        },
        {
        "name": "Tonic",
        "address": "2036 G St NW, Washington, DC 20036",
        "lat": 38.898194,
        "lon": -77.046497
        },
        {
        "name": "Toryumon Japanese House",
        "address": "1901 Pennsylvania Ave NW #0001, Washington, DC 20006",
        "lat": 38.900689,
        "lon": -77.043797
        },
        {
        "name": "UpTowner Cafe",
        "address": "2023 G St NW, Washington, DC 20052",
        "lat": 38.898613,
        "lon": -77.045785
        },
        {
        "name": "Washington Deli",
        "address": "1990 K St NW, Washington, DC 20006",
        "lat": 38.902192,
        "lon": -77.04455
        },
        {
        "name": "Whole Foods Market",
        "address": "2201 I St NW, Washington, DC 20037",
        "lat": 38.900919,
        "lon": -77.04907
        },
        {
        "name": "Wicked Waffle",
        "address": "1712 I St NW, Washington, DC 20006",
        "lat": 38.90111,
        "lon": -77.040224
        },
        {
        "name": "Wingo's",
        "address": "2218 Wisconsin Ave NW, Washington, DC 20007",
        "lat": 38.919585,
        "lon": -77.071304
        },
        {
        "name": "Wiseguy Pizza",
        "address": "2121 H St NW, Washington, DC 20052",
        "lat": 38.899840,
        "lon": -77.047860
        }]
    """.trimIndent()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spinner)
        spinner = findViewById(R.id.spinner)

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

            doAsync {
                parseGworldFile()
                //findGworld(businessesList, gworldList)
            }


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

    // Parses gworld.json for future comparison with yelp businesses list
    fun parseGworldFile() {
        //gworldList.clear()
//        val reader = BufferedReader(
//            InputStreamReader(getAssets().open("gworld.json"), "UTF-8")
//        )
//        var resultsString: String? = null
//        try {
//            val results = StringBuilder()
//            while (true) {
//                val line = reader.readLine()
//                if (line == null) break
//                results.append(line)
//            }
//           resultsString = results.toString()
//        }
//        catch (e:IOException) {
//            //log the exception
//        }
//        finally {
//            reader.close()
//        }
//
//        val jsonArray = JSONArray(resultsString)
        val jsonArray = JSONArray(gworldJson)
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
        runOnUiThread{
            val testParse = gworldList[0].name
            Toast.makeText(this@LandmarksActivity, "test parse: $testParse", Toast.LENGTH_LONG).show()
        }
    }

    // Compares list of yelp businesses to list of gworld businesses
    // Calls yelp api to retrieve review excerpts
    fun findGworld(yelpResponse: List<Business>, gworlds: List<Business>) {
        yelpResponse.forEach { yelpBusiness ->
            gworlds.forEach { gworldBusiness ->
                if (
                    yelpBusiness.lat.compareWithThreshold(gworldBusiness.lat, .02)
                    && yelpBusiness.lon.compareWithThreshold(gworldBusiness.lon, .02)
                    && yelpBusiness.name == gworldBusiness.name
                ) {
                    // Yelp Business Reviews API call using yelpBusiness.id
                    // Go to next activity displaying review excerpts
//                    yelpManager.retrieveReviews(
//                        apiKey = getString(R.string.yelp_api_key),
//                        businessId = yelpBusiness.id,
//                        successCallback = { review ->
//                            runOnUiThread {
//                                reviews.clear()
//                                reviews.addAll(review)
//                                // Testing if i can get the yelper name of the first result
//                                val test = reviews[0].yelper_name
//                                Toast.makeText(this@LandmarksActivity, "first result name: $test", Toast.LENGTH_LONG).show()
//                            }
//                        },
//                        errorCallback = {
//                            runOnUiThread {
//                                Toast.makeText(this@LandmarksActivity, "Error retrieving reviews", Toast.LENGTH_LONG).show()
//                            }
//                        }
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