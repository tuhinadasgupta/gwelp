package edu.gwu.gwelp
import java.io.Serializable

data class Business (
    val name: String,
    val id: String,
    val address: String,
    val lat: Double,
    val lon: Double
): Serializable


