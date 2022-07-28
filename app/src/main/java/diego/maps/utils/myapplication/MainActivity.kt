package diego.maps.utils.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.data.Feature
import com.google.maps.android.data.Layer
import com.google.maps.android.data.geojson.GeoJsonLayer
import com.google.maps.android.data.geojson.GeoJsonPolygonStyle
import com.google.maps.android.data.kml.KmlLayer
import diego.maps.utils.lib.model.MarkerItem
import diego.maps.utils.lib.ui.DetailBottomSheet
import diego.maps.utils.lib.ui.SettingsBottomSheet
import diego.maps.utils.lib.utils.cluster.ClusterUtils
import diego.maps.utils.lib.utils.cluster.MarkerReader
import diego.maps.utils.lib.utils.kml.KMLUtils
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException

class MainActivity : AppCompatActivity(), DetailBottomSheet.DetailItemClicked,  SettingsBottomSheet.SettingsItemClicked, OnMapReadyCallback {

    lateinit var googleMap: GoogleMap
    lateinit var kmlLayer: KmlLayer
    lateinit var clusterManager: ClusterManager<MarkerItem>
    var colors : ArrayList<String> = ArrayList()
    var names: ArrayList<String> = ArrayList()
    var ids: ArrayList<String> = ArrayList()
    var currentkmlIndex = -1
    private val kmlUtils = KMLUtils()
    private val clusterUtils = ClusterUtils()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpMap()
    }

    private fun setUpMap() {
        (supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)?.getMapAsync(
            this
        )
    }

    private fun setupView() {

        //Kml
        kmlLayer = kmlUtils.retrieveKml(googleMap, kml, this)
//        kmlLayer.addLayerToMap()
//        kmlUtils.processKml(kml, colors, names, ids)


        val layerSpain = kmlUtils.retrieveKml(googleMap, R.raw.geojson_spain, this, R.color.black, android.R.color.transparent,1.0f)
        layerSpain.addLayerToMap()

        val layer = kmlUtils.retrieveKml(googleMap, R.raw.geojson_layer, this, R.color.purple_200, android.R.color.transparent, 2.0f)
        layer.addLayerToMap()
        //Markers
        val markers = clusterUtils.retrieveMarkers(raw)
        clusterManager = clusterUtils.retrieveCluster(googleMap, this, supportFragmentManager, R.drawable.ic_marker, true)
        clusterManager.addItems(markers)

        setupInteraction()
        moveCamera()

    }

    private fun setupInteraction() {
        fab.setOnClickListener {
            val detailSheet = SettingsBottomSheet.newInstance(true, this, "")
            detailSheet.show(supportFragmentManager, DetailBottomSheet.TAG)
        }
        kmlLayer.setOnFeatureClickListener(Layer.OnFeatureClickListener { feature: Feature ->
            currentkmlIndex = getPosition(feature.id)
            val detailSheet = DetailBottomSheet.newInstance(true, this, names[getPosition(feature.id)])
            detailSheet.show(supportFragmentManager, DetailBottomSheet.TAG)
        })
    }

    override fun onDetailItemClicked(way: Int, color: String) {
        when (way) {
            4 -> {
                val newKml = kml.replace(colors[currentkmlIndex], color)
                colors[currentkmlIndex] = color
                kmlLayer.removeLayerFromMap()
                kmlLayer = KmlLayer(googleMap, newKml.byteInputStream(),this)
                kmlLayer.addLayerToMap()
                kml = newKml
                setupInteraction()
            }
            5 -> {
                var newKml = kml.replace(names[currentkmlIndex], color)
                kmlLayer.removeLayerFromMap()
                names[currentkmlIndex] = color
                kmlLayer = KmlLayer(googleMap, newKml.byteInputStream(),this)
                kmlLayer.addLayerToMap()
                kml = newKml
                setupInteraction()
            }
            6 -> {
                var newKml = kml.replace(colors[currentkmlIndex], "${color}${colors[currentkmlIndex].substring(2)}")
                kmlLayer.removeLayerFromMap()
                colors[currentkmlIndex] = "${color}${colors[currentkmlIndex].substring(2)}"
                kmlLayer = KmlLayer(googleMap, newKml.byteInputStream(),this)
                kmlLayer.addLayerToMap()
                kml = newKml
                setupInteraction()
            }
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
        setupView()
    }

    override fun onSettingsItemClicked(way: Int, aux: String) {
        when (way) {
            1 -> {
                googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL;
            }
            2 -> {
                googleMap.mapType = GoogleMap.MAP_TYPE_HYBRID;

            }
            3 -> {
                googleMap.mapType = GoogleMap.MAP_TYPE_SATELLITE;

            }
            4 -> {
                googleMap.mapType = GoogleMap.MAP_TYPE_TERRAIN;
            }
        }
    }

    fun getPosition(id: String): Int {
        val position = findIndex(ids, ids.first { it == id })
        return position
    }

    fun findIndex(arr: ArrayList<String>, item: String): Int {
        return arr.indexOf(item)
    }

    private fun moveCamera() {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(41.51834, 0.87013), 20f))
    }

//    private val raw = """[
//        { "lat" : 43.361518, "lng" : -5.854480, "title" : "La Gabinona", "snippet": "La fuente de la plaza de América"},
//        { "lat" : 43.360720, "lng" : -5.852762, "title" : "Plaza España", "snippet": "La céntrica fuente de la plaza de España"},
//        { "lat" : 43.364060, "lng" : -5.851448,  "title" : "El Corte Inglés", "snippet" : "Ventas"},
//        { "lat" : 43.363058, "lng" : -5.849815,  "title" : "Estatua de Woody Allen", "snippet" : "Conocidog director de cine"},
//        { "lat" : 43.363292, "lng" : -5.845501,  "title" : "Plaza Juan XXIII", "snippet" : "Plaza con áreas verdes"},
//        { "lat" : 43.362825, "lng" : -5.844710,  "title" : "Palacio de Valdecarzana-Heredia", "snippet" : "Es una gran residencia palaciega urbana"},
//        { "lat" : 43.362702, "lng" : -5.845466,  "title" : "Palacio de Camposagrado", "snippet" : "Es un edificio palaciego de estilo barroco"},
//        { "lat" : 43.362899, "lng" : -5.844023,  "title" : "Jardín de los Reyes Caudillos", "snippet" : "El conjunto escultórico"}
//        ]
//        """

//    private val raw = "[\n" +
//            "  {\n" +
//            "    \"lat\": 41.51834,\n" +
//            "    \"lng\": 0.87013,\n" +
//            "    \"title\": \"Les Borges Blanques\",\n" +
//            "    \"snippet\": \"La fuente de la plaza de América\"\n" +
//            "  },\n" +
//            "  {\n" +
//            "    \"lat\": 41.422702,\n" +
//            "    \"lng\": 1.021196,\n" +
//            "    \"title\": \"Tarrés\",\n" +
//            "    \"snippet\": \"La céntrica fuente de la plaza de España\"\n" +
//            "  },\n" +
//            "    {\n" +
//            "    \"lat\": 41.606063,\n" +
//            "    \"lng\": 0.879895,\n" +
//            "    \"title\": \"Miralcamp\",\n" +
//            "    \"snippet\": \"La céntrica fuente de la plaza de España\"\n" +
//            "  },\n" +
//            "    {\n" +
//            "    \"lat\": 41.551069,\n" +
//            "    \"lng\": 0.88901,\n" +
//            "    \"title\": \"Puiggròs\",\n" +
//            "    \"snippet\": \"La céntrica fuente de la plaza de España\"\n" +
//            "  },\n" +
//            "    {\n" +
//            "    \"lat\": 41.382993,\n" +
//            "    \"lng\": 0.946906,\n" +
//            "    \"title\": \"El Vilosell\",\n" +
//            "    \"snippet\": \"La céntrica fuente de la plaza de España\"\n" +
//            "  },\n" +
//            "    {\n" +
//            "    \"lat\": 41.36679,\n" +
//            "    \"lng\": 0.916169,\n" +
//            "    \"title\": \"La Pobla de Cérvoles\",\n" +
//            "    \"snippet\": \"La céntrica fuente de la plaza de España\"\n" +
//            "  }\n" +
//            "]"

    private var raw = "[\n" +
            "  {\n" +
            "    \"lat\": 41.51834,\n" +
            "    \"lng\": 0.87013,\n" +
            "    \"snippet\": \"https://cdn01.segre.com/uploads/imagenes/bajacalidad/2021/01/15/_altat3h3745706_075eb995.jpg?71de88fb5abe502568f6584703613003\",\n" +
            "    \"title\": \"Les Borges Blanques\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"lat\": 41.422702,\n" +
            "    \"lng\": 1.021196,\n" +
            "    \"title\": \"Tarrés\",\n" +
            "    \"snippet\": \"http://www.cerespain.com/images/tarres2.jpg\"\n" +
            "  },\n" +
            "    {\n" +
            "    \"lat\": 41.606063,\n" +
            "    \"lng\": 0.879895,\n" +
            "    \"title\": \"Miralcamp\",\n" +
            "    \"snippet\": \"https://www.rutadelvidelleida.cat/wp-content/uploads/pobladecervoles1.jpg\"\n" +
            "  },\n" +
            "    {\n" +
            "    \"lat\": 41.551069,\n" +
            "    \"lng\": 0.88901,\n" +
            "    \"title\": \"Puiggròs\",\n" +
            "    \"snippet\": \"https://www.rutadelvidelleida.cat/wp-content/uploads/pobladecervoles1.jpg\"\n" +
            "  },\n" +
            "    {\n" +
            "    \"lat\": 41.382993,\n" +
            "    \"lng\": 0.946906,\n" +
            "    \"title\": \"El Vilosell\",\n" +
            "    \"snippet\": \"https://vilosellwinehotel.com/wp-content/uploads/2016/11/EL_VILOSELL.jpg\"\n" +
            "  },\n" +
            "    {\n" +
            "    \"lat\": 41.36679,\n" +
            "    \"lng\": 0.916169,\n" +
            "    \"title\": \"La Pobla de Cérvoles\",\n" +
            "    \"snippet\": \"https://www.rutadelvidelleida.cat/wp-content/uploads/pobladecervoles1.jpg\"\n" +
            "  }\n" +
            "]"

    var kml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n" +
            "  <Document>\n" +
            "    <name>Google Campus</name>\n" +
            "    <Style id=\"styleBlue\">\n" +
            "      <LineStyle>\n" +
            "        <color>7dff0000</color>\n" +
            "        <width>2</width>\n" +
            "      </LineStyle>\n" +
            "      <PolyStyle>\n" +
            "        <color>7dff0000</color>\n" +
            "        <fill>1</fill>\n" +
            "        <outline>1</outline>\n" +
            "      </PolyStyle>\n" +
            "      <BalloonStyle>\n" +
            "        <text><![CDATA[<h3>\$[name]</h3>]]></text>\n" +
            "      </BalloonStyle>\n" +
            "    </Style>\n" +
            "    <Style id=\"styleGreen\">\n" +
            "      <LineStyle>\n" +
            "        <color>ff2fac15</color>\n" +
            "        <width>2</width>\n" +
            "      </LineStyle>\n" +
            "      <PolyStyle>\n" +
            "        <color>ff2fac15</color>\n" +
            "        <fill>1</fill>\n" +
            "        <outline>1</outline>\n" +
            "      </PolyStyle>\n" +
            "      <BalloonStyle>\n" +
            "        <text><![CDATA[<h3>\$[name]</h3>]]></text>\n" +
            "      </BalloonStyle>\n" +
            "    </Style>\n" +
            "        <Style id=\"styleRed\">\n" +
            "      <LineStyle>\n" +
            "        <color>ff585bec</color>\n" +
            "        <width>2</width>\n" +
            "      </LineStyle>\n" +
            "      <PolyStyle>\n" +
            "        <color>ff585bec</color>\n" +
            "        <fill>1</fill>\n" +
            "        <outline>1</outline>\n" +
            "      </PolyStyle>\n" +
            "      <BalloonStyle>\n" +
            "        <text><![CDATA[<h3>\$[name]</h3>]]></text>\n" +
            "      </BalloonStyle>\n" +
            "    </Style>\n" +
            "    <StyleMap id=\"styleBlue-nodesc\">\n" +
            "      <Pair>\n" +
            "        <key>normal</key>\n" +
            "        <styleUrl>#styleBlue</styleUrl>\n" +
            "      </Pair>\n" +
            "    </StyleMap>\n" +
            "    <StyleMap id=\"styleRed-nodesc\">\n" +
            "      <Pair>\n" +
            "        <key>normal</key>\n" +
            "        <styleUrl>#styleRed</styleUrl>\n" +
            "      </Pair>\n" +
            "    </StyleMap>\n" +
            "    <StyleMap id=\"styleGreen-nodesc\">\n" +
            "      <Pair>\n" +
            "        <key>normal</key>\n" +
            "        <styleUrl>#styleGreen</styleUrl>\n" +
            "      </Pair>\n" +
            "    </StyleMap>\n" +
            "    <Placemark>\n" +
            "      <name>Zone 1</name>\n" +
            "      <styleUrl>#styleBlue-nodesc</styleUrl>\n" +
            "      <Polygon>\n" +
            "        <outerBoundaryIs>\n" +
            "          <LinearRing>\n" +
            "            <tessellate>1</tessellate>\n" +
            "            <coordinates>\n" +
            "              -77.054779,-12.090146,0\n" +
            "              -77.050766,-12.088037,0\n" +
            "              -77.053126,-12.092821,0\n" +
            "              -77.055819,-12.091048,0\n" +
            "              -77.053351,-12.089674,0\n" +
            "            </coordinates>\n" +
            "          </LinearRing>\n" +
            "        </outerBoundaryIs>\n" +
            "      </Polygon>\n" +
            "    </Placemark>\n" +
            "    <Placemark>\n" +
            "      <name>Zone 2</name>\n" +
            "      <styleUrl>#styleRed-nodesc</styleUrl>\n" +
            "      <Polygon>\n" +
            "        <outerBoundaryIs>\n" +
            "          <LinearRing>\n" +
            "            <tessellate>1</tessellate>\n" +
            "            <coordinates>\n" +
            "              -77.048584,-12.085500,0\n" +
            "              -77.046267,-12.085143,0\n" +
            "              -77.046028,-12.082943,0\n" +
            "              -77.048818,-12.085136,0\n" +
            "            </coordinates>\n" +
            "          </LinearRing>\n" +
            "        </outerBoundaryIs>\n" +
            "      </Polygon>\n" +
            "    </Placemark>\n" +
            "    <Placemark>\n" +
            "      <name>Zone 3</name>\n" +
            "      <styleUrl>#styleGreen-nodesc</styleUrl>\n" +
            "      <Polygon>\n" +
            "        <outerBoundaryIs>\n" +
            "          <LinearRing>\n" +
            "            <tessellate>1</tessellate>\n" +
            "            <coordinates>\n" +
            "              -77.051015,-12.092025,0\n" +
            "              -77.049095,-12.091742,0\n" +
            "              -77.049179,-12.089754,0\n" +
            "              -77.052660,-12.089093,0\n" +//"              -77.050536,-12.089890,0\n" +
            "              -77.053315,-12.091275,0\n" +//"              -77.051566,-12.091925,0\n" +
            "            </coordinates>\n" +
            "          </LinearRing>\n" +
            "        </outerBoundaryIs>\n" +
            "      </Polygon>\n" +
            "    </Placemark>\n" +
            "  </Document>\n" +
            "</kml>\n"

}