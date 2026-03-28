package com.mzansi.microtrips.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: Int,
    val name: String,
    val icon: String,
    val color: String
)