package edu.du.tubridyproject3


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject

class DetailsFragment : Fragment() {
    // Initialize variables
    private lateinit var recyclerViewDefinitions: RecyclerView
    private lateinit var recyclerViewDefinitionExamples: RecyclerView
    private lateinit var PartOfSpeech: TextView
    private lateinit var DefinitionsHeading: TextView
    private lateinit var DefinitionExamples: TextView
    private lateinit var dataList: ArrayList<JSONObject>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_details, container, false)

        // Initialize UI elements
        recyclerViewDefinitions = view.findViewById(R.id.rv_definitions)
        recyclerViewDefinitionExamples = view.findViewById(R.id.rv_definition_examples)

        PartOfSpeech = view.findViewById(R.id.partOfSpeechView)
        DefinitionsHeading = view.findViewById(R.id.definitionView)
        DefinitionExamples = view.findViewById(R.id.exampleView)

        // Set text for TextViews
        val details = JSONObject(arguments?.getString("details"))
        val definitions = details.getJSONArray("definitions")

        PartOfSpeech.text = "${details.getString("partOfSpeech").capitalize()}"
        DefinitionsHeading.text = "Definitions"

        if(definitions.length()>0){
            dataList = ArrayList()

            for (i in 0 until definitions.length()) {
                val jsonObject = definitions.getJSONObject(i)
                dataList.add(jsonObject)
            }


            recyclerViewDefinitions.layoutManager = LinearLayoutManager(activity)
            recyclerViewDefinitions.adapter = DetailsAdapter2(
                dataList,
            )


        }
        return view
    }

    class DetailsAdapter2(private val dataList: ArrayList<JSONObject>) : RecyclerView.Adapter<DetailsAdapter2.ViewHolder>() {


        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tv_definition: TextView = itemView.findViewById(R.id.textView)
            val tv_example: TextView = itemView.findViewById(R.id.exampleView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.definition_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val data = dataList[position]
            holder.tv_definition.text = (position+1).toString()+". " +data.getString("definition")
            if(data.has("example")){
                holder.tv_example.text = "Example: "+ data.getString("example")
            } else{
                holder.tv_example.visibility=View.GONE
            }

        }

        override fun getItemCount(): Int {
            return dataList.size
        }
    }

}
