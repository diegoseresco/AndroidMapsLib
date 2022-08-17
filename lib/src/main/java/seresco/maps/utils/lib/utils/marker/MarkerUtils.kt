package seresco.maps.utils.lib.utils.marker

import androidx.fragment.app.FragmentManager
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.data.geojson.GeoJsonLayer
import seresco.maps.utils.lib.model.MarkerItem
import seresco.maps.utils.lib.ui.DetailBottomSheet
import seresco.maps.utils.lib.ui.MarkerInfoBottomSheet
import org.json.JSONObject

/**
 * Grupo de funciones relacionado con *Markers*.
 *
 * Esta clase permite la integración de Markers de manera rápida, eficiente y con muchas mayores funcionalidades
 *
 * @param map instancia de GoogleMaps.
 * @param supportFragmentManager (opcional) permite mostrar los bottomSheets.
 * @param icon (opcional) ícono del marker
 * @param showPic (opcional) permite mostrar una imagen cuando  se abre el bottomSheet
 */
class MarkerUtils {

    fun retrieveMarkers(map: GoogleMap, data: String, supportFragmentManager: FragmentManager? = null, icon: Int? = null, showPic: Boolean? = true): GeoJsonLayer {

        val geoJsonData =  JSONObject("{ \"type\": \"FeatureCollection\", \"features\": $data}")
        val stopsLayer = GeoJsonLayer(map, geoJsonData)
        val pointStyle = stopsLayer.defaultPointStyle
        icon?.let {
            pointStyle.icon = BitmapDescriptorFactory.fromResource(it)
        }

        supportFragmentManager?.let { fragmentManager ->
            stopsLayer.setOnFeatureClickListener { feature ->
                val marker = MarkerItem(feature.getProperty("name"), null)
                val detailSheet = MarkerInfoBottomSheet.newInstance(true, marker, showPic ?: true)
                detailSheet.show(fragmentManager, DetailBottomSheet.TAG)
            }
        }
        return stopsLayer
    }

    fun addMarker(map: GoogleMap, latLng: LatLng) {
        map.addMarker(
            MarkerOptions()
                .position(latLng)
                .title("Localización actual")
        )
    }



}