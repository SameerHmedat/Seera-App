package com.example.seeragroup

import com.example.seeragroup.models.Movie

data class Model(val moviesMutableLiveData: ArrayList<Movie> = arrayListOf(), val name:String) {
//    var isLoaidng: Boolean
}