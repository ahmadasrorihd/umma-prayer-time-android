package id.umma.prayertimes.ahmadasrori.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.umma.prayertimes.ahmadasrori.model.PrayerTime

@Dao
interface PrayerTimesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(fav: PrayerTime)

    @Query("SELECT * from PrayerTime ORDER BY id DESC LIMIT 1")
    fun getLocalPrayerTime(): LiveData<PrayerTime>
}