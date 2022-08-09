package diego.maps.utils.lib.utils.kml

import android.content.Context
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.maps.android.data.geojson.GeoJsonLayer
import com.google.maps.android.data.geojson.GeoJsonLineStringStyle
import com.google.maps.android.data.geojson.GeoJsonPolygonStyle
import com.google.maps.android.data.kml.KmlLayer
import org.json.JSONObject
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Grupo de funciones relacionado con *KML*.
 *
 * Esta clase permite la integración de KML de manera rápida, eficiente y con muchas mayores funcionalidades
 *
 * @param map instancia de GoogleMaps.
 * @param resource permite obtener la información (json) de las coordenadas del KML.
 * @param strokeColor color del borde
 * @param fillColor color del bloque
 * @param zIndex grosor del borde
 */
class KMLUtils {

    fun retrieveKml(map: GoogleMap, kmlRaw: String, context: Context): KmlLayer {
        return KmlLayer(map, kmlRaw.byteInputStream(), context)
    }

    fun retrieveKml(map: GoogleMap, resource: Int, context: Context, strokeColor: Int, fillColor: Int, zIndex: Float): GeoJsonLayer {
        val layer = GeoJsonLayer(map, resource, context)
        val geoPolygonStyle: GeoJsonPolygonStyle = layer.defaultPolygonStyle
        geoPolygonStyle.strokeColor = ContextCompat.getColor(context, strokeColor)
        geoPolygonStyle.fillColor = ContextCompat.getColor(context, fillColor)
        geoPolygonStyle.zIndex = zIndex
        geoPolygonStyle.isClickable = true;
        return layer
    }

    fun retrieveKml(map: GoogleMap, coordinates: MutableList<MutableList<Double>>, context: Context, strokeColor: Int, fillColor: Int, zIndex: Float): GeoJsonLayer {
        val resource = "{ \"type\": \"FeatureCollection\", \"features\": [ {  \"type\": \"Feature\", \"properties\": {}, \"geometry\": {  \"type\": \"LineString\",  \"coordinates\": $coordinates } } ] }"
        val geoJsonData =  JSONObject(resource)
        val layer = GeoJsonLayer(map, geoJsonData)
        val geoPolygonStyle: GeoJsonPolygonStyle = layer.defaultPolygonStyle
        geoPolygonStyle.strokeColor = ContextCompat.getColor(context, strokeColor)
        geoPolygonStyle.fillColor = ContextCompat.getColor(context, fillColor)
        geoPolygonStyle.zIndex = zIndex
        geoPolygonStyle.isClickable = true;
        return layer
    }

    fun retrieveLinesKml(map: GoogleMap, coordinates: MutableList<MutableList<Double>>, context: Context, strokeColor: Int, zIndex: Float): GeoJsonLayer {
        val resource = "{ \"type\": \"FeatureCollection\", \"features\": [ {  \"type\": \"Feature\", \"properties\": {}, \"geometry\": {  \"type\": \"LineString\",  \"coordinates\": $coordinates } } ] }"
        val geoJsonData =  JSONObject(resource)
        val layer = GeoJsonLayer(map, geoJsonData)
        val geoLinesStyle: GeoJsonLineStringStyle = layer.defaultLineStringStyle
        geoLinesStyle.color = ContextCompat.getColor(context, strokeColor)
        geoLinesStyle.zIndex = zIndex
        geoLinesStyle.isClickable = true;
        return layer
    }

    fun processKml(kmlRaw: String, colors: ArrayList<String>, names: ArrayList<String>, ids: ArrayList<String>) {
        val colorPattern: Pattern = Pattern.compile("<color>(.*?)</color>")
        val colorMatcher: Matcher = colorPattern.matcher(kmlRaw)
        while (colorMatcher.find()) {
            if (!colors.contains(colorMatcher.group(1))) {
                colors.add(colorMatcher.group(1))
            }
        }

        val namesPattern: Pattern = Pattern.compile("<name>(.*)</name>")
        val namesMatcher: Matcher = namesPattern.matcher(kmlRaw)
        while (namesMatcher.find()) {
            names.add(namesMatcher.group(1))
        }
        names.removeAt(0)

        val idPattern: Pattern = Pattern.compile("<styleUrl>(.*?)</styleUrl>")
        val idMatcher: Matcher = idPattern.matcher(kmlRaw)
        while (idMatcher.find()) {
            if (idMatcher.group(1).contains("nodesc")) {
                ids.add(idMatcher.group(1))
            }
        }
    }
}