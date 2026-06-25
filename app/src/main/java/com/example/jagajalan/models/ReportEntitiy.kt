package com.example.jagajalan.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reports")
data class ReportEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val userName: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val latitude: Double,
    val longitude: Double,
    val status: String,
    val timestamp: Long
)