package seresco.maps.utils.lib.model

class CustomMarker {
    private val type: String
    private val properties: Property
    private val geometry: Geometry

    constructor(type: String, properties: Property, geometry: Geometry) {
        this.type = type
        this.properties = properties
        this.geometry = geometry
    }
}