package ir.boardbazi.findaspy.places_activity

import android.app.Dialog
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import ir.boardbazi.findaspy.GameOption
import ir.boardbazi.findaspy.R
import kotlinx.android.synthetic.main.dialog_place.view.*

class DialogPlaceAdapter(
    var context: Context,
    var places: ArrayList<Place>,
    var gameOption: GameOption,
    var adapter: PlaceAdapter,
    var replacedPlaces: ArrayList<Place>,
    var recyclerView: RecyclerView,
    var dialog: Dialog
) : RecyclerView.Adapter<DialogPlaceAdapter.viewHolder>() {
    override fun onCreateViewHolder(parnt: ViewGroup, viewType: Int): viewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.dialog_place,parnt,false)
        return viewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return places.size
    }

    override fun onBindViewHolder(viewHolder: viewHolder, position: Int) {
        var place = places[position]
        viewHolder.textView.text = place.name
        viewHolder.iconView.setImageResource(place.icon)
        viewHolder.layout.setOnClickListener {
            replacedPlaces.add(places[position])
            places.remove(places[position])
            notifyDataSetChanged()
            if (replacedPlaces.size == gameOption.places.size){
                gameOption.places = replacedPlaces
                adapter = PlaceAdapter(context,gameOption.places)
                recyclerView.adapter = adapter
                dialog.dismiss()
            }
        }
    }

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView
        var iconView: ImageView
        var layout:ViewGroup
        init {
            textView = itemView.d_list_text
            iconView = itemView.d_list_image
            layout = itemView.dialogLayout
        }
    }
}