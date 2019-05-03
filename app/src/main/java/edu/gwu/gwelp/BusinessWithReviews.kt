package edu.gwu.gwelp

import java.io.Serializable

data class BusinessWithReviews(val business: Business, val reviews: List<Review>) :Serializable{
   val rating =   if (reviews.isNotEmpty()) {
        (reviews.sumBy { review -> review.rating } / reviews.size).toDouble()
    } else {
        0.0
    }
    val name: String = business.name
    val text: String = if (reviews.isNotEmpty()) {
        reviews.find { business.id == it.business_id }?.text ?: "No review message listed"
    } else {
        "No review message listed"
    }
    //val text: String= reviews.get(0).toString()
}

