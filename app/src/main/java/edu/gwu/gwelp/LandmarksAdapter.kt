package edu.gwu.gwelp

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class LandmarksAdapter constructor(private val landmarks: List<String>) : RecyclerView.Adapter<LandmarksAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Open & parse XML file
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row_landmark, parent, false)

        // Create a new ViewHolder
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = landmarks.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentLandmark = landmarks[position]

        holder.nameTextView.text = currentLandmark

    }

    /**
     * Holds references to the views that make up an individual row. findViewById can be an
     * expensive operation, so this prevents you from needing to do it again when a row is recycled.
     */
    class ViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {

        val nameTextView: TextView = view.findViewById(R.id.landmark_name)

    }
}