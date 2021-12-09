package id.umma.prayertimes.ahmadasrori.model

import com.google.gson.annotations.SerializedName

data class PrayerTimeResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("results")
	val results: Results? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Results(

	@field:SerializedName("settings")
	val settings: Settings? = null,

	@field:SerializedName("datetime")
	val datetime: List<DatetimeItem?>? = null,

	@field:SerializedName("location")
	val location: Location? = null
)

data class Settings(

	@field:SerializedName("school")
	val school: String? = null,

	@field:SerializedName("juristic")
	val juristic: String? = null,

	@field:SerializedName("timeformat")
	val timeformat: String? = null,

	@field:SerializedName("highlat")
	val highlat: String? = null,

	@field:SerializedName("fajr_angle")
	val fajrAngle: Double? = null,

	@field:SerializedName("isha_angle")
	val ishaAngle: Double? = null
)

data class Date(

	@field:SerializedName("hijri")
	val hijri: String? = null,

	@field:SerializedName("gregorian")
	val gregorian: String? = null,

	@field:SerializedName("timestamp")
	val timestamp: Int? = null
)

data class Location(

	@field:SerializedName("elevation")
	val elevation: Double? = null,

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("country_code")
	val countryCode: String? = null,

	@field:SerializedName("local_offset")
	val localOffset: Double? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("timezone")
	val timezone: String? = null,

	@field:SerializedName("latitude")
	val latitude: Double? = null,

	@field:SerializedName("longitude")
	val longitude: Double? = null
)

data class Times(

	@field:SerializedName("Sunset")
	val sunset: String? = null,

	@field:SerializedName("Asr")
	val asr: String? = null,

	@field:SerializedName("Isha")
	val isha: String? = null,

	@field:SerializedName("Fajr")
	val fajr: String? = null,

	@field:SerializedName("Dhuhr")
	val dhuhr: String? = null,

	@field:SerializedName("Maghrib")
	val maghrib: String? = null,

	@field:SerializedName("Sunrise")
	val sunrise: String? = null,

	@field:SerializedName("Midnight")
	val midnight: String? = null,

	@field:SerializedName("Imsak")
	val imsak: String? = null
)

data class DatetimeItem(

	@field:SerializedName("date")
	val date: Date? = null,

	@field:SerializedName("times")
	val times: Times? = null
)
