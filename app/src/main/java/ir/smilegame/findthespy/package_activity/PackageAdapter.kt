package ir.smilegame.findthespy.package_activity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ir.smilegame.findthespy.R
import kotlinx.android.synthetic.main._package.view.*

class PackageAdapter(var context: Context, var packs:ArrayList<String>) : androidx.recyclerview.widget.RecyclerView.Adapter<PackageAdapter.viewHolder>() {


    override fun onCreateViewHolder(parnt: ViewGroup, viewType: Int): viewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout._package,parnt,false)
        return viewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return packs.size
    }

    override fun onBindViewHolder(viewHolder: viewHolder, position: Int) {
        viewHolder.textView.text = packs[position]
        viewHolder.textView.setOnClickListener {
            var id = 0
            when (packs[position]){
                "اصلی"->id = R.array.main
                "ایران"->id = R.array.iran
                "همه"->id = 100
            }
            (context as PackageActivity).startGame(id)
        }
    }

    class viewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        var textView: TextView
        init {
            textView = itemView.text
        }
    }
}