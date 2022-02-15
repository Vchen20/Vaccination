package com.example.vaccination

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class VaccinationAdapter(var dataSet: List<VaccinationInfo>) :
    RecyclerView.Adapter<VaccinationAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewCountry: TextView
        val textViewLatestVax: TextView
        val layout : ConstraintLayout


        init {
            textViewCountry = view.findViewById(R.id.textView_vaxItem_country)
            textViewLatestVax = view.findViewById(R.id.textView_vaxItem_latestVax)
            layout = view.findViewById(R.id.layout_vaxItem)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_vax, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val countryInfo = dataSet[position]
        viewHolder.textViewCountry.text = countryInfo.country
        viewHolder.textViewLatestVax.text =
            countryInfo.timeline[countryInfo.timeline.lastKey()].toString()
        viewHolder.layout.setOnClickListener {
            val context = viewHolder.layout.context
            val vaxDetailIntent = Intent(context, VaccinationDetailActivity::class.java).apply {
                putExtra(VaccinationDetailActivity.EXTRA_COUNTRY, countryInfo)
            }
            context.startActivity(vaxDetailIntent)
        }
    }

    override fun getItemCount() = dataSet.size
}