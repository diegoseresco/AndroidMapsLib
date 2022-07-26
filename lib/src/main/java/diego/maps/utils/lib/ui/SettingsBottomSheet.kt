package diego.maps.utils.lib.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import diego.maps.utils.lib.R
import kotlinx.android.synthetic.main.bottom_sheet_detail.*
import kotlinx.android.synthetic.main.bottom_sheet_settings.*

/*
  Initializes the Settings Map View
  This allowed to change the type of the map
*/
class SettingsBottomSheet(listener: SettingsItemClicked, name: String): BottomSheetDialogFragment() {

    private var dismissWithAnimation = false
    private val mListener = listener
    private val mName = name

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.bottom_sheet_settings, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        setUpListeners()
    }

    private fun setUpViews() {
    }

    private fun setUpListeners() {
        ivNormal.setOnClickListener {
            mListener.onSettingsItemClicked(1)
            dismiss()
        }
        ivHybrid.setOnClickListener {
            mListener.onSettingsItemClicked(2)
            dismiss()

        }
        ivSatellite.setOnClickListener {
            mListener.onSettingsItemClicked(3)
            dismiss()
        }
        ivTerrain.setOnClickListener {
            mListener.onSettingsItemClicked(4)
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
        fun newInstance(dismissWithAnimation: Boolean, listener: SettingsItemClicked, name: String): SettingsBottomSheet {
            val modalSimpleListSheet = SettingsBottomSheet(listener, name)
            modalSimpleListSheet.arguments = bundleOf(ARG_DISMISS_WITH_ANIMATION to dismissWithAnimation)
            return modalSimpleListSheet
        }
    }

    interface SettingsItemClicked {
        fun onSettingsItemClicked(way: Int, aux: String = "")
    }
}