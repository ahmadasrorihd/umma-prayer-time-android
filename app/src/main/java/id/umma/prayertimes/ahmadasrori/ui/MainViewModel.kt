package id.umma.prayertimes.ahmadasrori.ui

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.umma.prayertimes.ahmadasrori.data.PrayerTimeRepository
import id.umma.prayertimes.ahmadasrori.data.Repository
import id.umma.prayertimes.ahmadasrori.model.PrayerTime
import id.umma.prayertimes.ahmadasrori.model.PrayerTimeResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainViewModel(private val repository: Repository, application: Application) : ViewModel() {
    private val prayerTimeRepository: PrayerTimeRepository = PrayerTimeRepository(application)
    val prayerTimeResponse = MutableLiveData<PrayerTimeResponse>()
    private val apiResponse = MutableLiveData<String>()

    private val compositeDisposable by lazy {
        CompositeDisposable()
    }

    fun getPrayerTimes(longitude: Double, latitude: Double) {
        compositeDisposable.add(
            repository.getPrayerTimes(longitude, latitude)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe ({
                    val response = it
                    prayerTimeResponse.postValue(response)
                },{
                    apiResponse.postValue(it.message)
                })
        )
    }

    fun insert(prayerTime: PrayerTime) {
        prayerTimeRepository.insert(prayerTime)
    }

    fun getLocalPrayerTime(): LiveData<PrayerTime> = prayerTimeRepository.getLocalPrayerTime()

}