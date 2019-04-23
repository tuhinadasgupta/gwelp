package edu.gwu.gwelp

data class Review (
    val business_id: String,
    val rating: Int,
    val yelper_name: String,
    val yelper_image_url: String,
    val text: String,
    val time_created: String,
    val url: String
)