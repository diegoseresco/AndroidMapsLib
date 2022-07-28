package diego.maps.utils.lib.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.Marker
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.progressindicator.CircularProgressIndicator
import diego.maps.utils.lib.R
import kotlinx.android.synthetic.main.bottom_sheet_marker_info.*

class MarkerInfoBottomSheet(context: Context, marker: Marker, showPic: Boolean): BottomSheetDialogFragment() {

    private var dismissWithAnimation = false
    private val markerInfo = marker
    private val showPic = showPic

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.bottom_sheet_marker_info, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        setUpListeners()
    }

    private fun setUpViews() {
        val textName = if (markerInfo.title != null) markerInfo.title else "Marker"
        val picture = if (markerInfo.snippet != null) markerInfo.snippet else "Descripción"
        tvNameMarker.text = textName

        if (showPic) {
            context?.let {
                Glide
                    .with(it)
                    .load(picture)
                    .centerCrop()
                    .placeholder(R.drawable.progress_animation)
                    .into(ivMarker)
            };
            ivMarker.visibility = View.VISIBLE
        } else {
            ivMarker.visibility = View.GONE
        }

    }

    private fun setUpListeners() {

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dismissWithAnimation = arguments?.getBoolean(DetailBottomSheet.ARG_DISMISS_WITH_ANIMATION) ?: false
        (requireDialog() as BottomSheetDialog).dismissWithAnimation = dismissWithAnimation
    }

    companion object {
        const val TAG = "modalDetailSheet"
        const val ARG_DISMISS_WITH_ANIMATION = "dismiss_with_animation"
        fun newInstance(dismissWithAnimation: Boolean, context: Context, marker: Marker, showPic: Boolean): MarkerInfoBottomSheet {
            val modalSheet = MarkerInfoBottomSheet(context, marker, showPic)
            modalSheet.arguments = bundleOf(ARG_DISMISS_WITH_ANIMATION to dismissWithAnimation)
            return modalSheet
        }
    }
}