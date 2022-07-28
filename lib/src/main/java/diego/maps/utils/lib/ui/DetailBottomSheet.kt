package diego.maps.utils.lib.ui

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.os.bundleOf
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import diego.maps.utils.lib.R
import kotlinx.android.synthetic.main.bottom_sheet_detail.*
import java.util.prefs.Preferences

/*
  Initializes the Detail KML View
  This shows three options: change the color of the layer, edit the name and the transparency
*/
class DetailBottomSheet(listener: DetailItemClicked, name: String): BottomSheetDialogFragment() {

    private var dismissWithAnimation = false
    private val mListener = listener
    private val mName = name

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.bottom_sheet_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        setUpListeners()
    }

    private fun setUpViews() {
        tvName.text = mName
    }

    private fun setUpListeners() {
        ivEdit.setOnClickListener {
            rlUpdateName.visibility = View.VISIBLE
            llColors.visibility = View.GONE
            llTransparency.visibility = View.GONE
            tvName.isEnabled = true
            mListener.onDetailItemClicked(1)
        }
        ivPaint.setOnClickListener {
            rlUpdateName.visibility = View.GONE
            llColors.visibility = View.VISIBLE
            llTransparency.visibility = View.GONE
            tvName.isEnabled = false
            mListener.onDetailItemClicked(2)

        }
        ivTransparency.setOnClickListener {
            rlUpdateName.visibility = View.GONE
            llColors.visibility = View.GONE
            llTransparency.visibility = View.VISIBLE
            tvName.isEnabled = false
            mListener.onDetailItemClicked(3)
        }
        llRed.setOnClickListener {
            mListener.onDetailItemClicked(4, "ff0000ff")
            dismiss()
        }
        llYellow.setOnClickListener {
            mListener.onDetailItemClicked(4, "ff00c8ff")
            dismiss()
        }
        llBlue.setOnClickListener {
            mListener.onDetailItemClicked(4, "fff3372d")
            dismiss()
        }
        llSkyblue.setOnClickListener {
            mListener.onDetailItemClicked(4, "fff3ac2d")
            dismiss()
        }
        llGreen.setOnClickListener {
            mListener.onDetailItemClicked(4, "ff44a648")
            dismiss()
        }
        llBrown.setOnClickListener {
            mListener.onDetailItemClicked(4, "ff606da1")
            dismiss()
        }
        bUpdate.setOnClickListener {
            mListener.onDetailItemClicked(5, etName.text.toString())
            dismiss()
        }
        sbTransparency.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                var decimal = (p1 * 255)/100
                val hexString = java.lang.Integer.toHexString(decimal)
                mListener.onDetailItemClicked(6, hexString)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dismissWithAnimation = arguments?.getBoolean(DetailBottomSheet.ARG_DISMISS_WITH_ANIMATION) ?: false
        (requireDialog() as BottomSheetDialog).dismissWithAnimation = dismissWithAnimation
    }

    companion object {
        const val TAG = "modalDetailSheet"
        const val ARG_DISMISS_WITH_ANIMATION = "dismiss_with_animation"
        fun newInstance(dismissWithAnimation: Boolean, listener: DetailItemClicked, name: String): DetailBottomSheet {
            val modalSimpleListSheet = DetailBottomSheet(listener, name)
            modalSimpleListSheet.arguments = bundleOf(ARG_DISMISS_WITH_ANIMATION to dismissWithAnimation)
            return modalSimpleListSheet
        }
    }

    interface DetailItemClicked {
        fun onDetailItemClicked(way: Int, color: String = "")
    }
}