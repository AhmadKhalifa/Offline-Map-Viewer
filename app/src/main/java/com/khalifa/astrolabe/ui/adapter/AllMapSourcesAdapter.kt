package com.khalifa.astrolabe.ui.adapter

import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.khalifa.astrolabe.R
import com.khalifa.astrolabe.data.model.tileSource.MapSourceFactory
import com.khalifa.astrolabe.util.MapSourceUtil
import kotlinx.android.synthetic.main.list_group_item_tile_source.view.*
import kotlinx.android.synthetic.main.list_item_tile_source_group.view.*
import org.osmdroid.tileprovider.tilesource.ITileSource
import java.util.ArrayList

class AllMapSourcesAdapter(private val itemInteractionListener: OnItemInteractionListener?) :
        RecyclerView.Adapter<AllMapSourcesAdapter.AllMapSourcesViewHolder>() {

    var mapSources: ArrayList<MapSourceFactory.MapSource>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = AllMapSourcesViewHolder(
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_tile_source_group, parent, false)
    )

    override fun getItemCount() = mapSources?.size ?: 0

    override fun onBindViewHolder(mapSourcesViewHolder: AllMapSourcesViewHolder, position: Int) =
            mapSourcesViewHolder.setContent(this)

    class AllMapSourcesViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun setContent(adapter: AllMapSourcesAdapter) = with(view) {
            val mapSource = adapter.mapSources?.get(adapterPosition)
            mapSource?.let {
                groupIconImageView.setImageResource(it.icon)
                groupTitleTextView.text = it.name
                with(groupRecyclerView) {
                    itemAnimator = DefaultItemAnimator()
                    layoutManager = LinearLayoutManager(
                            context,
                            LinearLayoutManager.HORIZONTAL,
                            false
                    )
                    isNestedScrollingEnabled = false
                    this.adapter = TileSourceAdapter(adapter.itemInteractionListener).also {
                        adapter -> adapter.tileSources = mapSource.getAllSources()
                    }
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

private class TileSourceAdapter(
        private val itemInteractionListener: AllMapSourcesAdapter.OnItemInteractionListener?
) : RecyclerView.Adapter<TileSourceAdapter.MapSourceViewHolder>() {

    var tileSources: List<ITileSource>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MapSourceViewHolder(
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_group_item_tile_source, parent, false)
    )

    override fun getItemCount() = tileSources?.size ?: 0

    override fun onBindViewHolder(mapSourceViewHolder: MapSourceViewHolder, position: Int) =
            mapSourceViewHolder.setContent(this)

    class MapSourceViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun setContent(adapter: TileSourceAdapter) = with(view) {
            val tileSource = adapter.tileSources?.get(adapterPosition)
            tileSource?.let {
                thumbnailImageView.setImageResource(MapSourceUtil.getThumbnail(tileSource))
                nameTextView.text = MapSourceUtil.getName(tileSource)
                typeTextView.text = MapSourceUtil.getType(tileSource)
                useMapButton.setOnClickListener {
                    adapter.itemInteractionListener?.onTileSourceSelectedAsBaseMap(tileSource)
                }
                addLayerButton.setOnClickListener {
                    adapter.itemInteractionListener?.onTileSourceSelectedAsLayer(tileSource)
                }
            }
            Unit
        }
    }
}