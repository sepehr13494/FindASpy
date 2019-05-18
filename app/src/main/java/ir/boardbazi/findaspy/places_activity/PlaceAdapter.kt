package ir.boardbazi.findaspy.places_activity

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import ir.boardbazi.findaspy.R

import kotlinx.android.synthetic.main.place.*
import kotlinx.android.synthetic.main.place.view.*

class PlaceAdapter(var context: Context, var places:ArrayList<Place>) : RecyclerView.Adapter<PlaceAdapter.viewHolder>() {
    override fun onCreateViewHolder(parnt: ViewGroup, viewType: Int): viewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.place,parnt,false)
        return viewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return places.size
    }

    override fun onBindViewHolder(viewHolder: viewHolder, position: Int) {
        var place = places[position]
        viewHolder.textView.text = place.name
        viewHolder.iconView.setImageResource(place.icon)
    }

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView:TextView
        var iconView:ImageView
        init {
            textView = itemView.text
            iconView = itemView.image
        }
    }
}