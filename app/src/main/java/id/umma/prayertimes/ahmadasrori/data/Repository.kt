package id.umma.prayertimes.ahmadasrori.data

import id.umma.prayertimes.ahmadasrori.model.PrayerTimeResponse
import id.umma.prayertimes.ahmadasrori.service.ApiService
import io.reactivex.Observable

class Repository(private val api: ApiService) {

    fun getPrayerTimes(longitude: Double, latitude: Double): Observable<PrayerTimeResponse> {
        return api.getPrayerTimes(longitude, latitude, 333)
    }
}