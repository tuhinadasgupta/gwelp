package edu.gwu.gwelp

import java.io.Serializable

data class BusinessWithReviews(val business: Business, val reviews: List<Review>) :Serializable{
    val rating = if (reviews.isNotEmpty()){
        reviews[0].rating.toDouble()
    }
    else{
        0.0
    }
//        if (reviews.isNotEmpty()) {
//        (reviews.sumBy { review -> review.rating } / reviews.size).toDouble()
//    } else {
//        0.0
//    }
    val name: String = business.name
    //val text: String= reviews.get(0).toString()
}

