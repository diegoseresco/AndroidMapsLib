package diego.maps.utils.lib.utils.kml

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.maps.android.data.kml.KmlLayer
import java.util.regex.Matcher
import java.util.regex.Pattern

class KMLUtils {

    fun retrieveKml(map: GoogleMap, kmlRaw: String, context: Context): KmlLayer {
        return KmlLayer(map, kmlRaw.byteInputStream(), context)
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