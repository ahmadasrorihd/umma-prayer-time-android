package id.umma.prayertimes.ahmadasrori.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import id.umma.prayertimes.ahmadasrori.model.PrayerTime

@Database(entities = [PrayerTime::class], version = 1)
abstract class PrayerTimeDatabase : RoomDatabase() {
    abstract fun favDao(): PrayerTimesDao
    companion object {
        @Volatile
        private var INSTANCE: PrayerTimeDatabase? = null
        @JvmStatic
        fun getDatabase(context: Context): PrayerTimeDatabase {
            if (INSTANCE == null) {
                synchronized(PrayerTimeDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        PrayerTimeDatabase::class.java, "prayertime")
                        .build()
                }
            }
            return INSTANCE as PrayerTimeDatabase
        }
    }
}