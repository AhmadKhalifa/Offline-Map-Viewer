package com.khalifa.astrolabe.ui.fragment

import android.arch.lifecycle.Observer
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.khalifa.astrolabe.R
import com.khalifa.astrolabe.ui.base.BaseBottomSheetDialogFragment
import com.khalifa.astrolabe.ui.widget.osmdroid.TilesOverlayWithOpacity
import com.khalifa.astrolabe.ui.widget.osmdroid.WMSOverlayWithOpacity
import com.khalifa.astrolabe.util.MapSourceUtil
import com.khalifa.astrolabe.viewmodel.Error
import com.khalifa.astrolabe.viewmodel.Event
import com.khalifa.astrolabe.viewmodel.fragment.implementation.TransparencyControllerViewModel
import com.xw.repo.BubbleSeekBar
import kotlinx.android.synthetic.main.fragment_transparency_controller.*
import java.lang.IllegalStateException

/**
 * @author Ahmad Khalifa
 */

class TransparencyControllerFragment :
        BaseBottomSheetDialogFragment<TransparencyControllerViewModel>() {

    companion object {

        private val TAG: String = TransparencyControllerFragment::class.java.simpleName

        private const val KEY_TILE_OVERLAY =
                "com.khalifa.astrolabe.ui.fragment.TransparencyControllerFragment.KEY_TILE_OVERLAY"

        private const val KEY_IS_WMS =
                "com.khalifa.astrolabe.ui.fragment.TransparencyControllerFragment.KEY_IS_WMS"

        fun showFragment(fragmentManager: FragmentManager?,
                         tilesOverlay: TilesOverlayWithOpacity,
                         isWMS: Boolean,
                         onFragmentInteractionListener: OnFragmentInteractionListener) =
                fragmentManager?.let { manager ->
                    TransparencyControllerFragment().also {
                        it.fragmentInteractionListener = onFragmentInteractionListener
                        it.arguments = Bundle().apply {
                            putSerializable(KEY_TILE_OVERLAY, tilesOverlay)
                            putBoolean(KEY_IS_WMS, isWMS)
                        }
                        it.show(manager, TAG)
                    }
                }
    }

    interface OnFragmentInteractionListener {

        fun onTransparencyChanged(transparencyPercentage: Int)
    }

    private var fragmentInteractionListener: OnFragmentInteractionListener? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_transparency_controller, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.isWMS = arguments?.getBoolean(KEY_IS_WMS) ?: false
        val tilesOverlay = arguments?.getSerializable(KEY_TILE_OVERLAY)?.let {
            it as TilesOverlayWithOpacity
        } ?: throw IllegalStateException("Unable to extract tile overlay from fragment arguments.")
        viewModel.tileOverlay.value = tilesOverlay
        viewModel.initialValue = tilesOverlay.transparencyPercentage.toDouble()
        doneButton.setOnClickListener {
            viewModel.isFinalValue = true
            dismiss()
        }
        transparencySeekBar.onProgressChangedListener = object : BubbleSeekBar.OnProgressChangedListener {

            override fun onProgressChanged(bubbleSeekBar: BubbleSeekBar?,
                                           progress: Int,
                                           progressFloat: Float) {
                fragmentInteractionListener?.onTransparencyChanged(progress)
            }

            override fun getProgressOnActionUp(bubbleSeekBar: BubbleSeekBar?,
                                               progress: Int,
                                               progressFloat: Float) {

            }

            override fun getProgressOnFinally(bubbleSeekBar: BubbleSeekBar?,
                                              progress: Int,
                                              progressFloat: Float) {

            }
        }
    }

    private fun setTileOverlay(tilesOverlay: TilesOverlayWithOpacity?) {
        tilesOverlay?.run {
            if (viewModel.isWMS) {
                val wmsOverlay = this as WMSOverlayWithOpacity
                iconImageView.setImageResource(R.drawable.defaultmap)
                nameTextView.text = wmsOverlay.wmsLayer.name
                typeTextView.text = wmsOverlay.wmsLayer.title
                transparencySeekBar.setProgress(transparencyPercentage.toFloat())
            } else {
                iconImageView.setImageResource(MapSourceUtil.getIcon(tileProvider().tileSource))
                nameTextView.text = MapSourceUtil.getName(tileProvider().tileSource)
                typeTextView.text = MapSourceUtil.getType(tileProvider().tileSource)
                transparencySeekBar.setProgress(transparencyPercentage.toFloat())
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        if (!viewModel.isFinalValue) {
            viewModel.tileOverlay.value?.transparencyPercentage = viewModel.initialValue.toInt()
        }
    }

    override fun getViewModelInstance() = TransparencyControllerViewModel.getInstance(this)

    override fun onEvent(event: Event) {}

    override fun onError(error: Error) {}

    override fun registerLiveDataObservers() {
        viewModel.tileOverlay.observe(this, Observer(::setTileOverlay))
    }
}