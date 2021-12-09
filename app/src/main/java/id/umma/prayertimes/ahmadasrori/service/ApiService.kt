package id.umma.prayertimes.ahmadasrori.service

import id.umma.prayertimes.ahmadasrori.model.PrayerTimeResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("today.json?")
    fun getPrayerTimes(
        @Query("longitude") longitude: Double,
        @Query("latitude") latitude: Double,
        @Query("elevation") elevation: Int
        ): Observable<PrayerTimeResponse>
}