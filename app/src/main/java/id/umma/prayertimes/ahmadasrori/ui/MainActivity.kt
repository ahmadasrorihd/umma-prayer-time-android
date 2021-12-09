package id.umma.prayertimes.ahmadasrori.ui

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.annotations.AfterPermissionGranted
import com.vmadalin.easypermissions.dialogs.DEFAULT_SETTINGS_REQ_CODE
import id.umma.prayertimes.ahmadasrori.R
import id.umma.prayertimes.ahmadasrori.databinding.ActivityMainBinding
import id.umma.prayertimes.ahmadasrori.model.PrayerTime
import id.umma.prayertimes.ahmadasrori.model.PrayerTimeResponse
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

private const val REQUEST_CODE_LOCATION = 125

class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val viewModel: MainViewModel by viewModel()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var prayerTime: PrayerTime? = null
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
        initListener()
    }

    private fun initView() {
        prayerTime = PrayerTime()
        binding.pgBar.visibility = View.VISIBLE
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        checkPermission()
    }

    private fun initListener() {
        binding.swipeRefresh.setOnRefreshListener {
            initView()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun observe(){
        viewModel.prayerTimeResponse.observe(this, {
            val jsonPrayerTime = gson.toJson(it)
            prayerTime.let { data ->
                data?.response = jsonPrayerTime
            }
            viewModel.insert(prayerTime as PrayerTime)
            val addresses: List<Address>
            val geocoder = Geocoder(this, Locale.getDefault())
            addresses = geocoder.getFromLocation(
                latitude,
                longitude,
                1
            )
            val city: String = addresses[0].locality
            val country: String = addresses[0].countryName
            binding.tvTimezone.text = getString(R.string.timezone).replace("{timezone}", it.results?.location?.timezone.toString())
            binding.tvCountry.text = getString(R.string.country).replace("{country}", country)
            binding.tvCity.text = getString(R.string.city).replace("{city}", city)
            binding.tvGregorian.text = getString(R.string.gregorian).replace("{date}", it.results?.datetime!![0]?.date?.gregorian.toString())
            binding.tvHijri.text = getString(R.string.hijri).replace("{hijri}", it.results.datetime[0]?.date?.hijri.toString())

            binding.tvImsak.text = it.results.datetime[0]?.times?.imsak
            binding.tvSunrise.text = it.results.datetime[0]?.times?.sunrise
            binding.tvFajr.text = it.results.datetime[0]?.times?.fajr
            binding.tvDhuhr.text = it.results.datetime[0]?.times?.dhuhr
            binding.tvAsr.text = it.results.datetime[0]?.times?.asr
            binding.tvMaghrib.text = it.results.datetime[0]?.times?.maghrib
            binding.tvIsha.text = it.results.datetime[0]?.times?.isha
            binding.pgBar.visibility = View.GONE
        })
    }

    private fun observeLocalData() {
        viewModel.getLocalPrayerTime().observe(this, { data->
            if (data==null) {
                Toast.makeText(this, "need internet connection to download data", Toast.LENGTH_SHORT).show()
            } else {
                val addresses: List<Address>
                val geocoder = Geocoder(this, Locale.getDefault())
                addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    1
                )
                val city: String = addresses[0].locality
                val country: String = addresses[0].countryName
                val it = gson.fromJson(data.response.toString(), PrayerTimeResponse::class.java)
                binding.tvTimezone.text = getString(R.string.timezone).replace("{timezone}", it.results?.location?.timezone.toString())
                binding.tvCountry.text = getString(R.string.country).replace("{country}", country)
                binding.tvCity.text = getString(R.string.city).replace("{city}", city)
                binding.tvGregorian.text = getString(R.string.gregorian).replace("{date}", it.results?.datetime!![0]?.date?.gregorian.toString())
                binding.tvHijri.text = getString(R.string.hijri).replace("{hijri}", it.results.datetime[0]?.date?.hijri.toString())
                binding.tvImsak.text = it.results.datetime[0]?.times?.imsak
                binding.tvSunrise.text = it.results.datetime[0]?.times?.sunrise
                binding.tvFajr.text = it.results.datetime[0]?.times?.fajr
                binding.tvDhuhr.text = it.results.datetime[0]?.times?.dhuhr
                binding.tvAsr.text = it.results.datetime[0]?.times?.asr
                binding.tvMaghrib.text = it.results.datetime[0]?.times?.maghrib
                binding.tvIsha.text = it.results.datetime[0]?.times?.isha
            }
            binding.pgBar.visibility = View.GONE
        })
    }

    private fun checkPermission(){
        if (ActivityCompat.checkSelfPermission(
                this,
                ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            reqPermission()
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                latitude = location?.latitude!!
                longitude = location.longitude
                if (isOnline(this)) {
                    viewModel.getPrayerTimes(longitude, latitude)
                } else {
                    observeLocalData()
                }
                observe()
            }
    }

    @AfterPermissionGranted(REQUEST_CODE_LOCATION)
    private fun reqPermission() {
        if (hasLocationAndContactsPermissions()) {
            checkPermission()
        } else {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.permission_location_and_contacts_rationale_message),
                REQUEST_CODE_LOCATION,
                ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION
            )
        }
    }


    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        // Some permissions have been granted
        // ...
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        Toast.makeText(this, "You need to allow location permission to use this app", Toast.LENGTH_SHORT).show()
        checkPermission()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    @SuppressLint("StringFormatInvalid")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == DEFAULT_SETTINGS_REQ_CODE) {
            val yes = getString(R.string.yes)
            val no = getString(R.string.no)

            // Do something after user returned from app settings screen, like showing a Toast.
            Toast.makeText(
                this,
                getString(
                    R.string.returned_from_app_settings_to_activity,
                    if (hasLocationAndContactsPermissions()) yes else no,
                ),
                LENGTH_LONG
            ).show()
        }
    }

    private fun hasLocationAndContactsPermissions(): Boolean {
        return EasyPermissions.hasPermissions(this, ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)
    }

    private fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

}