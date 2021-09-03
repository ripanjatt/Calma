package com.ripanjatt.calma.util

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.ripanjatt.calma.activities.Home
import com.ripanjatt.calma.adapters.AudioAdapter

object SearchListener {

    fun setSearchListener(editText: EditText, list: ArrayList<Audio>, result: ArrayList<Audio>, recycler: RecyclerView, home: Home) {
        val adapter = AudioAdapter(result, home, true)
        recycler.adapter = adapter
        editText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                result.clear()
                if(p0.toString().isNotEmpty()) {
                    recycler.visibility = View.VISIBLE
                    home.setVisibility(View.GONE)
                    result.addAll(list.filter { it.name.lowercase().startsWith(p0.toString().lowercase()) })
                } else {
                    recycler.visibility = View.GONE
                    home.setVisibility(View.VISIBLE)
                }
                adapter.notifyDataSetChanged()
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }
}