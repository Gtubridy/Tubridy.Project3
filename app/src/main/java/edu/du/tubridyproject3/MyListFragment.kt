package edu.du.tubridyproject3

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray


class MyListFragment : Fragment() {
    // Initialize variables

    private lateinit var recyclerView: RecyclerView
    private lateinit var headingTextView: TextView
    private lateinit var dataList: ArrayList<String>



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_my_list, container, false)
        // Initialize UI elements

        recyclerView = view.findViewById(R.id.recyclerView)
        headingTextView = view.findViewById(R.id.headingTextView)


        val word = arguments?.getString("word")




        val meanings = JSONArray(arguments?.getString("meanings"))
        Log.i(TAG, "meanings"+meanings)


        headingTextView.text = word?.capitalize()

        dataList = ArrayList()

        for (i in 0 until meanings.length()) {
            val jsonObject = meanings.getJSONObject(i)
            val partOfSpeech = jsonObject.getString("partOfSpeech")
            dataList.add((i+1).toString() +". "+partOfSpeech.capitalize())
        }

        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = MyListAdapter(dataList, object : MyListAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(activity, DetailsActivity::class.java)
                Log.i(TAG, "onItemClick: "+meanings[position].toString())
                intent.putExtra("details", meanings[position].toString())
                startActivity(intent)
            }
        })



        return view
    }



    // Adapter for RecyclerView
    private class MyListAdapter(private val dataList: ArrayList<String>, private val listener: OnItemClickListener) : RecyclerView.Adapter<MyListAdapter.ViewHolder>() {
        interface OnItemClickListener {
            fun onItemClick(position: Int)
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val textView: TextView = itemView.findViewById(R.id.textView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.part_of_speech_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val data = dataList[position]
            holder.textView.text = data
            holder.itemView.setOnClickListener {
                listener.onItemClick(position)
            }
        }

        override fun getItemCount(): Int {
            return dataList.size
        }
    }

}
