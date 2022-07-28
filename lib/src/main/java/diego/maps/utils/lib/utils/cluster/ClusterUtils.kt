package diego.maps.utils.lib.utils.cluster

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator
import diego.maps.utils.lib.R
import diego.maps.utils.lib.model.MarkerItem
import diego.maps.utils.lib.ui.DetailBottomSheet
import diego.maps.utils.lib.ui.MarkerInfoBottomSheet


class ClusterUtils {

    fun retrieveCluster(map: GoogleMap, context: Context, supportFragmentManager: FragmentManager? = null, icon: Int? = null, showPic: Boolean? = true): ClusterManager<MarkerItem> {
        val clusterManager: ClusterManager<MarkerItem> = ClusterManager(context, map)
        icon?.let {
            val bitMapIcon = BitmapDescriptorFactory.fromResource(it)
            clusterManager.renderer = MarkerItemClusterRenderer(context, map, clusterManager, bitMapIcon)
        }
        map.setOnCameraIdleListener(clusterManager)
        setBottomSheetInteraction(clusterManager, context, supportFragmentManager, showPic ?: true)
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

    private fun setBottomSheetInteraction(clusterManager: ClusterManager<MarkerItem>, context: Context, supportFragmentManager: FragmentManager? = null, showPic: Boolean) {
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
                val detailSheet = MarkerInfoBottomSheet.newInstance(true, context, itMarker, showPic)
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

class MarkerItemClusterRenderer(
    context: Context?, map: GoogleMap?,
    clusterManager: ClusterManager<MarkerItem>,
    icon: BitmapDescriptor
) : DefaultClusterRenderer<MarkerItem>(context, map, clusterManager) {
    private val iconItem = icon

    override fun onBeforeClusterItemRendered(item: MarkerItem, markerOptions: MarkerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions)
        val markerDescriptor = iconItem
        markerOptions.icon(markerDescriptor)
    }
}