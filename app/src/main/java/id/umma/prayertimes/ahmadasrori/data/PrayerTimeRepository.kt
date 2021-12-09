package id.umma.prayertimes.ahmadasrori.data

import android.app.Application
import androidx.lifecycle.LiveData
import id.umma.prayertimes.ahmadasrori.db.PrayerTimeDatabase
import id.umma.prayertimes.ahmadasrori.db.PrayerTimesDao
import id.umma.prayertimes.ahmadasrori.model.PrayerTime
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class PrayerTimeRepository(application: Application) {
    private val prayerTimesDao: PrayerTimesDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = PrayerTimeDatabase.getDatabase(application)
        prayerTimesDao = db.favDao()
    }

    fun getLocalPrayerTime(): LiveData<PrayerTime> = prayerTimesDao.getLocalPrayerTime()

    fun insert(favorite: PrayerTime) {
        executorService.execute { prayerTimesDao.insert(favorite) }
    }

}