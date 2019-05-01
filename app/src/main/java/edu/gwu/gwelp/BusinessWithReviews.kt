package edu.gwu.gwelp

import java.io.Serializable

data class BusinessWithReviews(val business: Business, val reviews: List<Review>) :Serializable{
    val rating: Double = (reviews.sumBy { review -> review.rating } / reviews.size).toDouble()
    val name: String = business.name
    //val text: String= reviews.get(0).toString()
}

