package com.example.seeragroup

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.seeragroup.Constants.IMAGE_BASE
import com.example.seeragroup.models.Movie
import com.example.seeragroup.utils.loadImage
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_details.*


class DetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val movieObject = Gson().fromJson(intent.getStringExtra("movieObject"), Movie::class.java)


        img_detail.loadImage(IMAGE_BASE + movieObject.poster)
        txtTitleDetail.text = movieObject.title
        txtRating.text = movieObject.vote_average
        txtParagraph.text="Overview \n\n${movieObject.overview}"
        vote_count.text = "${movieObject.vote_count} votes"
        release_date.text = movieObject.release
        txtLanguage.text = movieObject.language

    }
}