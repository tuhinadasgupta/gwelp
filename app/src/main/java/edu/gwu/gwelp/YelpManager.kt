package edu.gwu.gwelp

import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

class YelpManager {

    private val okHttpClient: OkHttpClient

    init {
        val builder = OkHttpClient.Builder()

        // This sets network timeouts (in case the phone can't connect
        // to the server or the server is down)
        builder.connectTimeout(20, TimeUnit.SECONDS)
        builder.readTimeout(20, TimeUnit.SECONDS)
        builder.writeTimeout(20, TimeUnit.SECONDS)

        // This causes all network traffic to be logged to the console
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(logging)

        okHttpClient = builder.build()
    }

    // Get Yelp businesses within 1.25 miles of given address string, sorted by distance
    fun retrieveBusinesses(
        apiKey: String,
        address: String, // Name of the landmark
        successCallback: (List<Business>) -> Unit,
        errorCallback: (Exception) -> Unit
    ) {

        // Build request URL with query parameters
        // https://api.yelp.com/v3/businesses/search
        val url = HttpUrl.Builder()
            .scheme("https")
            .host("api.yelp.com")
            .addPathSegments("v3/businesses/search")
            .addQueryParameter("location", address)
            .addQueryParameter("radius", "2000") // 2000 meters approx 1.25 miles
            .addQueryParameter("limit", "50")
            .addQueryParameter("sort_by", "distance")
            .addQueryParameter("price", "1, 2, 3")
            .build()

        // Building the request, passing the api key as a header
        val request = Request.Builder()
            .url(url)
            .header("Authorization", "Bearer $apiKey")
            .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                errorCallback(e)
            }

            override fun onResponse(call: Call, response: Response) {
                val businesses = mutableListOf<Business>()
                val responseString = response.body()?.string()

                if (response.isSuccessful && responseString != null) {
                    val results = JSONObject(responseString).getJSONArray("businesses")
                    for (i in 0 until results.length()) {
                        val curr = results.getJSONObject(i)
                        val name = curr.getString("name")
                        val id = curr.getString("id")
                        val location = curr.getJSONObject("location")
                        val address1 = location.getString("address1")
                        val coordinates = curr.getJSONObject("coordinates")
                        val latitude = coordinates.getDouble("latitude")
                        val longitude = coordinates.getDouble("longitude")
                        businesses.add(
                            Business(
                                name = name,
                                id = id,
                                address = address1,
                                lat = latitude,
                                lon = longitude
                            )
                        )
                    }
                    successCallback(businesses)
                    //...
                } else {
                    // Invoke the callback passed to our [retrieveBusinesses] function.
                    errorCallback(Exception("Business Search call failed"))
                }
            }
        })
    }
    // Get 3 review excerpts for a business
    fun retrieveReviews(
        apiKey: String,
        businessId: String,
        successCallback: (List<Review>) -> Unit,
        errorCallback: (Exception) -> Unit
    ) {

        val request = Request.Builder()
            .url("https://api.yelp.com/v3/businesses/$businessId/reviews")
            .header("Authorization", "Bearer $apiKey")
            .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                errorCallback(e)
            }

            override fun onResponse(call: Call, response: Response) {
                val reviews = mutableListOf<Review>()
                val responseString = response.body()?.string()

                if (response.isSuccessful && responseString != null) {
                    val results = JSONObject(responseString).getJSONArray("reviews")
                    for (i in 0 until results.length()) {
                        val curr = results.getJSONObject(i)
                        val rating = curr.getInt("rating")
                        val user = curr.getJSONObject("user")
                        val image_url = user.getString("image_url")
                        val name = user.getString("name")
                        val text = curr.getString("text")
                        val time_created = curr.getString("time_created")
                        val url = curr.getString("url")
                        reviews.add(
                            Review(
                                business_id = businessId,   // so we know which business the review is for
                                rating = rating,
                                yelper_name = name,
                                yelper_image_url = image_url,
                                text = text,
                                time_created = time_created,
                                url = url
                            )
                        )
                    }
                    successCallback(reviews)
                } else {
                    // Invoke the callback passed to our [retrieveReviews] function.
                    errorCallback(Exception("Business Reviews call failed"))
                }
            }
        })
    }
}