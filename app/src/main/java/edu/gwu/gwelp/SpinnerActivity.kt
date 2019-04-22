package edu.gwu.gwelp

import android.app.Activity
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast

class SpinnerActivity : Activity(), AdapterView.OnItemSelectedListener {
    val spinner: Spinner = findViewById(R.id.spinner)

    override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        val selected: String = parent.getItemAtPosition(pos) as String
        Toast.makeText(
            this,
            "$selected was selected!",
            Toast.LENGTH_LONG
        ).show()

        spinner.onItemSelectedListener = this
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