package seresco.maps.utils.lib.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import seresco.maps.utils.lib.R
import kotlinx.android.synthetic.main.bottom_sheet_colors.*
import kotlinx.android.synthetic.main.bottom_sheet_detail.*

class ColorsBottomSheet(callback: OnColorClicked): BottomSheetDialogFragment() {

    private var dismissWithAnimation = false
    private val mCallback = callback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.bottom_sheet_colors, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        setUpListeners()
    }

    private fun setUpViews() {

    }

    private fun setUpListeners() {
        llColorRed.setOnClickListener {
            mCallback.onColorItemClicked(R.color.red)
            dismiss()
        }
        llColorYellow.setOnClickListener {
            mCallback.onColorItemClicked(R.color.yellow)
            dismiss()
        }
        llColorBlue.setOnClickListener {
            mCallback.onColorItemClicked(R.color.blue)
            dismiss()
        }
        llColorSkyblue.setOnClickListener {
            mCallback.onColorItemClicked(R.color.sky_blue)
            dismiss()
        }
        llColorGreen.setOnClickListener {
            mCallback.onColorItemClicked(R.color.green)
            dismiss()
        }
        llColorBrown.setOnClickListener {
            mCallback.onColorItemClicked(R.color.brown)
            dismiss()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dismissWithAnimation = arguments?.getBoolean(DetailBottomSheet.ARG_DISMISS_WITH_ANIMATION) ?: false
        (requireDialog() as BottomSheetDialog).dismissWithAnimation = dismissWithAnimation
    }

    companion object {
        const val TAG = "modalDetailSheet"
        const val ARG_DISMISS_WITH_ANIMATION = "dismiss_with_animation"
        fun newInstance(dismissWithAnimation: Boolean, callback: OnColorClicked): ColorsBottomSheet {
            val modalSimpleListSheet = ColorsBottomSheet(callback)
            modalSimpleListSheet.arguments = bundleOf(ARG_DISMISS_WITH_ANIMATION to dismissWithAnimation)
            return modalSimpleListSheet
        }
    }

    interface OnColorClicked {
        fun onColorItemClicked(color: Int)
    }
}