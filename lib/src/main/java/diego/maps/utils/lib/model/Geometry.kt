package diego.maps.utils.lib.model

class Geometry {
    private val type: String
    private val coordinates: MutableList<Double>

    constructor(type: String, coordinates: MutableList<Double>) {
        this.type = type
        this.coordinates = coordinates
    }
}