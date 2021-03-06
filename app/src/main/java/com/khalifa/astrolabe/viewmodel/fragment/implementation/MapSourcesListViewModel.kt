package com.khalifa.astrolabe.viewmodel.fragment.implementation

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModelProviders
import com.khalifa.astrolabe.data.factory.MapSourceFactory
import com.khalifa.astrolabe.ui.fragment.MapSourcesListFragment
import com.khalifa.astrolabe.viewmodel.BaseRxViewModel
import com.khalifa.astrolabe.viewmodel.fragment.IMapSourcesListViewModel

/**
 * @author Ahmad Khalifa
 */

class MapSourcesListViewModel : BaseRxViewModel(), IMapSourcesListViewModel {

    companion object {
        @JvmStatic
        fun getInstance(mapSourcesListFragment: MapSourcesListFragment) =
                ViewModelProviders.of(mapSourcesListFragment)
                        .get(MapSourcesListViewModel::class.java)
    }

    override val mapSources = MutableLiveData<ArrayList<MapSourceFactory.MapSource>>()

    override fun loadMapSources() {
        mapSources.value = MapSourceFactory.onlineMapSources
    }
}