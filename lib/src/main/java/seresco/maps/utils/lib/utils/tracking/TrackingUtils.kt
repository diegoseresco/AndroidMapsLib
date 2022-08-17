package seresco.maps.utils.lib.utils.tracking

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import seresco.maps.utils.lib.R
import seresco.maps.utils.lib.ui.ColorsBottomSheet
import seresco.maps.utils.lib.ui.DetailBottomSheet
import seresco.maps.utils.lib.ui.TrackingBottomSheet
import seresco.maps.utils.lib.utils.Constant
import seresco.maps.utils.lib.utils.Preferences
import seresco.maps.utils.lib.utils.kml.KMLUtils
import seresco.maps.utils.lib.utils.marker.MarkerUtils

class TrackingUtils(context: Context, onTrackingCallback: OnTrackingCallback): TrackingBottomSheet.TrackingListener, ColorsBottomSheet.OnColorClicked, Constant {

    private val mOnTrackingCallback = onTrackingCallback
    lateinit var preference: Preferences
    private val mContext = context
    private val markerUtils = MarkerUtils()
    private val kmlUtils = KMLUtils()
    private lateinit var mSupportFragmentManager: FragmentManager
    private lateinit var mGoogleMap: GoogleMap

    fun openTrackingPanel(supportFragmentManager: FragmentManager) {
        val trackingSheet = TrackingBottomSheet.newInstance(true, mContext, this)
        trackingSheet.show(supportFragmentManager, DetailBottomSheet.TAG)
    }

    fun getSavedCoordinates() : MutableList<MutableList<Double>>? {
        preference = Preferences(mContext)
        return preference.getCoordinates(COORDS_DATA)
    }

    fun showSavedCoordinates(supportFragmentManager: FragmentManager, googleMap: GoogleMap) {
        mSupportFragmentManager = supportFragmentManager
        mGoogleMap = googleMap

        preference = Preferences(mContext)
        val coordinates = preference.getCoordinates(COORDS_DATA)
        googleMap.clear()
        coordinates?.let {
            val strokeColor = getStrokeColor(preference)
            val lays = kmlUtils.retrieveLinesKml(googleMap, it, mContext, strokeColor,1.0f)
            lays.setOnFeatureClickListener {
                val trackingSheet = ColorsBottomSheet.newInstance(true, this)
                trackingSheet.show(supportFragmentManager, DetailBottomSheet.TAG)
            }
            lays.addLayerToMap()
            val currentPosition = LatLng(it.last().last(),it.last().first())
            markerUtils.addMarker(googleMap, currentPosition)
        }
    }

    override fun getCoordinates(coords: MutableList<MutableList<Double>>) {
        mOnTrackingCallback.showTrackCoordinates(coords)
    }

    override fun onColorItemClicked(color: Int) {
        preference = Preferences(mContext)
        preference.saveInt(CURRENT_COLOR_DATA, color)
        showSavedCoordinates(mSupportFragmentManager, mGoogleMap)
    }

    private fun getStrokeColor(preferences: Preferences): Int {
        var strokeColor = preferences.getInt(CURRENT_COLOR_DATA)
        if (strokeColor == 0) {
            strokeColor = R.color.black
        }
        return strokeColor
    }
}

interface OnTrackingCallback {
    fun showTrackCoordinates(coords: MutableList<MutableList<Double>>)
}