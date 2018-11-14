package com.khalifa.mapViewer.data.repository.local.tileSource

import com.khalifa.mapViewer.data.model.tileSource.TileSource
import com.khalifa.mapViewer.data.repository.local.BaseLocalTileSourcesRepository
import com.khalifa.mapViewer.data.storage.room.TileSourcesDao
import com.khalifa.mapViewer.data.storage.room.TileSourcesDatabase

class RoomTileSourcesRepository : BaseLocalTileSourcesRepository() {

    private var _tileSourcesDao: TileSourcesDao? = null

    private val tileSourcesDao: TileSourcesDao
        get() {
            _tileSourcesDao = _tileSourcesDao ?: TileSourcesDatabase.getInstance().tileSourcesDao()
            return if (_tileSourcesDao != null) _tileSourcesDao as TileSourcesDao
            else throw IllegalStateException(
                    "TileSourcesDatabase.getInstance().getInstance() " +
                            "implementation returns null!"
            )
        }

    override fun addTileSource(vararg tileSource: TileSource) =
            tileSourcesDao.addTileSources(*tileSource)

    override fun getTileSources() =
            tileSourcesDao.getTileSources()

    override fun deleteTileSource(tileSource: TileSource) =
            tileSourcesDao.deleteTileSource(tileSource)
}