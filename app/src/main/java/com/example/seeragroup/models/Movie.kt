package com.example.seeragroup.models

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Movie(
    @PrimaryKey
    var id: String="",
    var title: String?=null,
    var overview: String?=null,
    var vote_count: String?=null,
    var vote_average: String?=null,

    @SerializedName("poster_path")
    var poster: String?=null,

    @SerializedName("release_date")
    var release: String?=null,

    @SerializedName("original_language")
    var language: String?=null,
):RealmObject()