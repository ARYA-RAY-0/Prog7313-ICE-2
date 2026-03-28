package com.mzansi.microtrips.data.models


import kotlinx.serialization.Serializable

@Serializable
data class Province(
    val id: Int,
    val name: String,
    val code: String
)