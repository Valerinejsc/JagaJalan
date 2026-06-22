package com.example.jagajalan.admin

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.jagajalan.R

class AdminDetailReportActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_admin_detail_report
        )

        val imgReport =
            findViewById<ImageView>(
                R.id.imgReport
            )

        val tvTitle =
            findViewById<TextView>(
                R.id.tvTitle
            )

        val tvStatus =
            findViewById<TextView>(
                R.id.tvStatus
            )

        val tvUser =
            findViewById<TextView>(
                R.id.tvUser
            )

        val tvLocation =
            findViewById<TextView>(
                R.id.tvLocation
            )

        val tvDescription =
            findViewById<TextView>(
                R.id.tvDescription
            )

        tvTitle.text =
            intent.getStringExtra("title")

        tvStatus.text =
            "Status : " +
                    intent.getStringExtra("status")

        tvUser.text =
            "Pelapor : " +
                    intent.getStringExtra("user")

        tvDescription.text =
            intent.getStringExtra("description")

        tvLocation.text =
            "Koordinat : " +
                    intent.getDoubleExtra(
                        "lat",
                        0.0
                    ) +
                    ", " +
                    intent.getDoubleExtra(
                        "lng",
                        0.0
                    )

        Glide.with(this)
            .load(
                intent.getStringExtra(
                    "imageUrl"
                )
            )
            .placeholder(
                R.drawable.ic_camera_placeholder
            )
            .into(imgReport)
    }
}