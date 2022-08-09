package diego.maps.utils.lib.utils.cluster

import android.util.Log
import diego.maps.utils.lib.model.CustomMarker
import diego.maps.utils.lib.model.MarkerItem
import diego.maps.utils.lib.model.Property
import org.json.JSONArray
import org.json.JSONException
import java.io.InputStream
import java.util.*

/**
 * Grupo de funciones relacionado con *Clusters*.
 *
 * Esta clase permite procesar la data de mapas (String) y convertirlas al formato para trabajar con los Markers
 *
 * @param inputStream formato con el cual se convertirá a JSON
 */
class MarkerReader {

    fun processMarkersItems(inputStream: InputStream?): List<MarkerItem> {
        val markers: MutableList<MarkerItem> = ArrayList<MarkerItem>()
        val jsonToProcess = Scanner(inputStream).useDelimiter(REGEX_INPUT_BOUNDARY_BEGINNING).next()
        val arrayToProcess = JSONArray(jsonToProcess)
        for (i in 0 until arrayToProcess.length()) {
            var title: String? = null
            var snippet: String? = null
            var picture: String? = null
            val `object` = arrayToProcess.getJSONObject(i)
            Log.e("hey! u", `object`.toString())
            val lat = `object`.getDouble("lat")
            val lng = `object`.getDouble("lng")
            if (!`object`.isNull("title")) {
                title = `object`.getString("title")
            }
            if (!`object`.isNull("snippet")) {
                snippet = `object`.getString("snippet")
            }
            if (!`object`.isNull("picture")) {
                picture = `object`.getString("picture")
            }
            markers.add(MarkerItem(lat, lng, title, snippet, picture))
        }
        return markers
    }

    /*fun processMarkersItems2(inputStream: InputStream?): List<CustomMarker> {
        val markers: MutableList<CustomMarker> = ArrayList<CustomMarker>()
        val jsonToProcess = Scanner(inputStream).useDelimiter(REGEX_INPUT_BOUNDARY_BEGINNING).next()
        val arrayToProcess = JSONArray(jsonToProcess)
        for (i in 0 until arrayToProcess.length()) {
            var property: Property? = null
            val `object` = arrayToProcess.getJSONObject(i)
            val lat = `object`.getDouble("lat")
            val lng = `object`.getDouble("lng")
            if (!`object`.isNull("title")) {
                title = `object`.getString("title")
            }
            if (!`object`.isNull("snippet")) {
                snippet = `object`.getString("snippet")
            }
            if (!`object`.isNull("picture")) {
                picture = `object`.getString("picture")
            }
            markers.add(MarkerItem(lat, lng, title, snippet, picture))
        }
        return markers
    }*/

    companion object {
        private const val REGEX_INPUT_BOUNDARY_BEGINNING = "\\A"
    }
}