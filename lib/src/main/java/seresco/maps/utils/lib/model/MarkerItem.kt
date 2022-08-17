package seresco.maps.utils.lib.model

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class MarkerItem : ClusterItem {
    private val mPosition: LatLng
    private var mTitle: String?
    private var mSnippet: String?
    private var mPicture: String?

    constructor(lat: Double, lng: Double, title: String?, snippet: String?, picture: String?) {
        mPosition = LatLng(lat, lng)
        mTitle = title
        mSnippet = snippet
        mPicture = picture
    }

    constructor(title: String?, snippet: String?) {
        mPosition = LatLng(0.0, 0.0)
        mTitle = title
        mSnippet = snippet
        mPicture = null
    }

    override fun getPosition(): LatLng {
        return mPosition
    }

    override fun getTitle(): String? {
        return mTitle
    }

    override fun getSnippet(): String? {
        return mSnippet
    }

    /**
     * Set the title of the marker
     * @param title string to be set as title
     */
    fun setTitle(title: String?) {
        mTitle = title
    }

    /**
     * Set the description of the marker
     * @param snippet string to be set as snippet
     */
    fun setSnippet(snippet: String?) {
        mSnippet = snippet
    }
}