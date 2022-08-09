package diego.maps.utils.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.data.Feature
import com.google.maps.android.data.Layer
import com.google.maps.android.data.geojson.GeoJsonLayer
import com.google.maps.android.data.geojson.GeoJsonPolygonStyle
import com.google.maps.android.data.kml.KmlLayer
import diego.maps.utils.lib.model.Coordinate
import diego.maps.utils.lib.model.MarkerItem
import diego.maps.utils.lib.ui.DetailBottomSheet
import diego.maps.utils.lib.ui.SettingsBottomSheet
import diego.maps.utils.lib.ui.TrackingBottomSheet
import diego.maps.utils.lib.utils.cluster.ClusterUtils
import diego.maps.utils.lib.utils.cluster.MarkerReader
import diego.maps.utils.lib.utils.kml.KMLUtils
import diego.maps.utils.lib.utils.marker.MarkerUtils
import diego.maps.utils.lib.utils.tracking.OnTrackingCallback
import diego.maps.utils.lib.utils.tracking.TrackingUtils
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity(), DetailBottomSheet.DetailItemClicked, SettingsBottomSheet.SettingsItemClicked, OnMapReadyCallback,
    OnTrackingCallback {

    lateinit var googleMap: GoogleMap
    private val kmlUtils = KMLUtils()
    private val trackingUtils = TrackingUtils(this, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpMap()
    }

    private fun setUpMap() {
        (supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)?.getMapAsync(this)
    }

    private fun setupView() {
        trackingUtils.showSavedCoordinates(supportFragmentManager, googleMap)
        setupInteraction()
        moveCamera()
    }

    override fun showTrackCoordinates(coords: MutableList<MutableList<Double>>) {
        trackingUtils.showSavedCoordinates(supportFragmentManager, googleMap)
    }

    private fun setupInteraction() {
        fab.setOnClickListener {
            trackingUtils.openTrackingPanel(supportFragmentManager)
        }
    }

    override fun onDetailItemClicked(way: Int, color: String) {
      //
    }

    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
        setupView()
    }

    override fun onSettingsItemClicked(way: Int, aux: String) {
     //
    }

    private fun moveCamera() {
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(41.51834, 0.87013), 20f))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-12.062959, -77.058556), 20f))

    }


}