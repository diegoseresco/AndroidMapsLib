package diego.maps.utils.lib.utils.cluster

import diego.maps.utils.lib.model.MarkerItem
import org.json.JSONArray
import org.json.JSONException
import java.io.InputStream
import java.util.*

class MarkerReader {

    fun processMarkersItems(inputStream: InputStream?): List<MarkerItem> {
        val markers: MutableList<MarkerItem> = ArrayList<MarkerItem>()
        val jsonToProcess = Scanner(inputStream).useDelimiter(REGEX_INPUT_BOUNDARY_BEGINNING).next()
        val arrayToProcess = JSONArray(jsonToProcess)
        for (i in 0 until arrayToProcess.length()) {
            var title: String? = null
            var snippet: String? = null
            val `object` = arrayToProcess.getJSONObject(i)
            val lat = `object`.getDouble("lat")
            val lng = `object`.getDouble("lng")
            if (!`object`.isNull("title")) {
                title = `object`.getString("title")
            }
            if (!`object`.isNull("snippet")) {
                snippet = `object`.getString("snippet")
            }
            markers.add(MarkerItem(lat, lng, title, snippet))
        }
        return markers
    }

    companion object {
        private const val REGEX_INPUT_BOUNDARY_BEGINNING = "\\A"
    }
}