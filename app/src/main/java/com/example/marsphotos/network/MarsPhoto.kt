package com.example.marsphotos.network

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class MarsPhoto(
    @SerialName(value = "img_src")
    val imgsrc: String
)