package diego.maps.utils.lib.utils.cluster

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.clustering.ClusterManager
import diego.maps.utils.lib.R
import diego.maps.utils.lib.model.MarkerItem
import diego.maps.utils.lib.ui.DetailBottomSheet
import diego.maps.utils.lib.ui.MarkerInfoBottomSheet
import diego.maps.utils.lib.ui.SettingsBottomSheet


class ClusterUtils {

    fun retrieveCluster(map: GoogleMap, context: Context, supportFragmentManager: FragmentManager? = null): ClusterManager<MarkerItem> {
        val clusterManager: ClusterManager<MarkerItem> = ClusterManager(context, map)
        map.setOnCameraIdleListener(clusterManager)
//        setSimpleInteraction(clusterManager, context)
        setBottomSheetInteraction(clusterManager, context, supportFragmentManager)
        return clusterManager
    }

    private fun setSimpleInteraction(clusterManager: ClusterManager<MarkerItem>, context: Context) {
        clusterManager.markerCollection.setInfoWindowAdapter(object : InfoWindowAdapter {
            override fun getInfoWindow(marker: Marker): View {
                val inflater = LayoutInflater.from(context)
                val view: View = inflater.inflate(R.layout.custom_info_window, null)
                val textView = view.findViewById<TextView>(R.id.textViewTitle)
                val text = if (marker.title != null) marker.title else "Cluster Item"
                textView.text = text
                return view
            }

            override fun getInfoContents(marker: Marker): View? {
                return null
            }
        })
    }

    private fun setBottomSheetInteraction(clusterManager: ClusterManager<MarkerItem>, context: Context, supportFragmentManager: FragmentManager? = null) {
        clusterManager.markerCollection.setInfoWindowAdapter(object : InfoWindowAdapter {
            override fun getInfoWindow(marker: Marker): View? {
                val inflater = LayoutInflater.from(context)
                val view: View = inflater.inflate(R.layout.view_marker_info, null)
                val textViewName = view.findViewById<TextView>(R.id.tvNameMarker)
                val textViewDescription = view.findViewById<TextView>(R.id.tvDescriptionMarker)
                val textName = if (marker.title != null) marker.title else "Marker"
                val textDescription = if (marker.snippet != null) marker.snippet else "Descripción"
                textViewName.text = textName
                textViewDescription.text = textDescription
                return view
            }

            override fun getInfoContents(marker: Marker): View? {
                return null
            }
        })

        clusterManager.markerCollection.setOnInfoWindowClickListener { itMarker ->
            supportFragmentManager?.let { itFragmentManager ->
                val detailSheet = MarkerInfoBottomSheet.newInstance(true, itMarker)
                detailSheet.show(itFragmentManager, DetailBottomSheet.TAG)
            } ?: kotlin.run {
                Toast.makeText(context, "Falta instanciar supportFragmentManager", Toast.LENGTH_SHORT).show()
            }

        }
    }

    fun retrieveMarkers(data: String): List<MarkerItem> {
        val inputStream = data.byteInputStream()
        return MarkerReader().processMarkersItems(inputStream)
    }
}