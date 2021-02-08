package com.axa.brastlewark.ui.gnomes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.axa.brastlewark.R
import com.axa.brastlewark.databinding.ItemGnomeBinding
import com.axa.brastlewark.model.Gnome
import com.axa.brastlewark.utils.loadCircularImage

class GnomeAdapter internal constructor(
    private val listener: (Int) -> Unit
) : RecyclerView.Adapter<GnomeAdapter.GnomeViewHolder>() {

    private var data = emptyList<Gnome>()

    override fun getItemCount() = data.size
    override fun getItemId(position: Int) = position.toLong()
    override fun getItemViewType(position: Int) = position

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GnomeViewHolder {
        val binding: ItemGnomeBinding = ItemGnomeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GnomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GnomeViewHolder, position: Int) = with(holder) {
        val gnome: Gnome = data[position]
        holder.bind(gnome)
        holder.itemView.setOnClickListener { listener(gnome.id) }
    }

    internal fun setData(data: List<Gnome>) {
        this.data = data
        notifyDataSetChanged()
    }

    class GnomeViewHolder(private val itemGnomeBinding: ItemGnomeBinding) : RecyclerView.ViewHolder(
        itemGnomeBinding.root
    ) {
        fun bind(item: Gnome) {
            itemGnomeBinding.nameTextView.text = item.name
            itemGnomeBinding.dataTextView.text =
                "${itemView.context.resources.getString(R.string.age)} ${item.age}\n" +
                "${itemView.context.resources.getString(R.string.height)} ${item.height}\n" +
                "${itemView.context.resources.getString(R.string.weight)} ${item.weight}"

            itemGnomeBinding.gnomeImage.loadCircularImage(item.thumbnail, itemGnomeBinding.root)
        }

    }

}