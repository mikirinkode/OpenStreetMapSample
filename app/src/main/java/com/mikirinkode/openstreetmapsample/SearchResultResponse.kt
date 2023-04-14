package com.mikirinkode.openstreetmapsample

import com.google.gson.annotations.SerializedName

data class SearchResultResponse(

	@field:SerializedName("osm_id")
	val osmId: String,

	@field:SerializedName("licence")
	val licence: String,

	@field:SerializedName("boundingbox")
	val boundingbox: List<String>,

	@field:SerializedName("address")
	val address: Address,

	@field:SerializedName("importance")
	val importance: Any,

	@field:SerializedName("icon")
	val icon: String,

	@field:SerializedName("lon")
	val lon: String,

	@field:SerializedName("display_name")
	val displayName: String,

	@field:SerializedName("type")
	val type: String,

	@field:SerializedName("osm_type")
	val osmType: String,

	@field:SerializedName("extratags")
	val extratags: Extratags,

	@field:SerializedName("class")
	val jsonMemberClass: String,

	@field:SerializedName("place_id")
	val placeId: String,

	@field:SerializedName("lat")
	val lat: String
)

data class Address(

	@field:SerializedName("country")
	val country: String,

	@field:SerializedName("country_code")
	val countryCode: String,

	@field:SerializedName("city")
	val city: String,

	@field:SerializedName("state_district")
	val stateDistrict: String,

	@field:SerializedName("ISO3166-2-lvl4")
	val iSO31662Lvl4: String,

	@field:SerializedName("postcode")
	val postcode: String,

	@field:SerializedName("state")
	val state: String
)

data class Extratags(

	@field:SerializedName("capital")
	val capital: String,

	@field:SerializedName("website")
	val website: String,

	@field:SerializedName("wikipedia")
	val wikipedia: String,

	@field:SerializedName("wikidata")
	val wikidata: String,

	@field:SerializedName("population")
	val population: String
)
