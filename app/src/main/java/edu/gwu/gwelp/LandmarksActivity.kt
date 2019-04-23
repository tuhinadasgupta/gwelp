package edu.gwu.gwelp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast

class LandmarksActivity: AppCompatActivity(), AdapterView.OnItemSelectedListener  {
    private lateinit var spinner: Spinner

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
            Toast.makeText(
                this,
                "$selected was selected!",
                Toast.LENGTH_LONG
            ).show()
        }
        //new intent page to be opened
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
        Toast.makeText(
            this,
            "Please select one of the landmarks!",
            Toast.LENGTH_LONG
        ).show()

    }

}