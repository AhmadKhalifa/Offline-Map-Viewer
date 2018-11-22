package com.khalifa.astrolabe.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.khalifa.astrolabe.R
import com.khalifa.astrolabe.util.MapSourceUtil
import kotlinx.android.synthetic.main.list_item_tile_source.view.*
import org.osmdroid.tileprovider.tilesource.ITileSource

class MapSourceAdapter(private val itemInteractionListener: OnItemInteractionListener?) :
        RecyclerView.Adapter<MapSourceAdapter.MapSourceViewHolder>() {

    var tileSources: List<ITileSource>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MapSourceViewHolder(
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_tile_source, parent, false)
    )

    override fun getItemCount() = tileSources?.size ?: 0

    override fun onBindViewHolder(mapSourceViewHolder: MapSourceViewHolder, position: Int) =
            mapSourceViewHolder.setContent(this)

    class MapSourceViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun setContent(adapter: MapSourceAdapter) = with(view) {
            val tileSource = adapter.tileSources?.get(adapterPosition)
            tileSource?.let {
                iconImageView.setImageResource(MapSourceUtil.getIconImage(tileSource))
                nameTextView.text = MapSourceUtil.getName(tileSource)
                typeTextView.text = MapSourceUtil.getType(tileSource)
                useAsBaseMapButton.setOnClickListener {
                    adapter.itemInteractionListener?.onTileSourceSelectedAsBaseMap(tileSource)
                }
                addAsLayerButton.setOnClickListener {
                    adapter.itemInteractionListener?.onTileSourceSelectedAsLayer(tileSource)
                }
            }
            Unit
        }
    }

    interface OnItemInteractionListener {

        fun onTileSourceSelectedAsBaseMap(tileSource: ITileSource)

        fun onTileSourceSelectedAsLayer(tileSource: ITileSource)
    }
}