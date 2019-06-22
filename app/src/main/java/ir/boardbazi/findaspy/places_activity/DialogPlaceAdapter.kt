package ir.boardbazi.findaspy.places_activity

import android.app.Dialog
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
    var dialog: Dialog,
    var save_tv: TextView
) : RecyclerView.Adapter<DialogPlaceAdapter.viewHolder>() {
    init {
        replacedPlaces.addAll(gameOption.places)
    }
    override fun onCreateViewHolder(parnt: ViewGroup, viewType: Int): viewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.dialog_place,parnt,false)
        return viewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return places.size
    }

    override fun onBindViewHolder(viewHolder: viewHolder, position: Int) {
        var place = places[position]
        if (place !in replacedPlaces){
            viewHolder.check_img.visibility = View.INVISIBLE
        }
        viewHolder.textView.text = place.name
        viewHolder.iconView.setImageResource(place.icon)
        viewHolder.layout.setOnClickListener {
            if (place in replacedPlaces){
                replacedPlaces.remove(place)
                viewHolder.check_img.visibility = View.INVISIBLE
            }else{
                if (replacedPlaces.size != gameOption.places.size){
                    replacedPlaces.add(places[position])
                    viewHolder.check_img.visibility = View.VISIBLE
                }else{
                    Toast.makeText(context,"ظرفیت پر شده...",Toast.LENGTH_SHORT).show()
                }
            }
            if (replacedPlaces.size == gameOption.places.size){
                save_tv.visibility = View.VISIBLE
            }else{
                save_tv.visibility = View.GONE
            }
        }
        save_tv.setOnClickListener {
            gameOption.places = replacedPlaces
            adapter = PlaceAdapter(context,gameOption.places)
            recyclerView.adapter = adapter
            dialog.dismiss()
        }
    }

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView
        var iconView: ImageView
        var check_img: ImageView
        var layout:ViewGroup
        init {
            textView = itemView.d_list_text
            iconView = itemView.d_list_image
            check_img = itemView.check_img
            layout = itemView.dialogLayout
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


}