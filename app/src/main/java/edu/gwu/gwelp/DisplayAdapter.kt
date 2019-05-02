package edu.gwu.gwelp

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


class DisplayAdapter constructor(private val array: List<BusinessWithReviews>) : RecyclerView.Adapter<DisplayAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.row_display, parent, false)
        return ViewHolder(itemView)
    }
    override fun getItemCount(): Int = array.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currBusinesses = array[position]
        holder.lineNameTextView.text = currBusinesses.name
        //sketchy line below
        holder.lineNameTextView2.text = currBusinesses.rating.toString()

    }
    class ViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {
        val lineNameTextView: TextView = view.findViewById(R.id.name)
        val lineNameTextView2: TextView = view.findViewById(R.id.rating)
    }
}
