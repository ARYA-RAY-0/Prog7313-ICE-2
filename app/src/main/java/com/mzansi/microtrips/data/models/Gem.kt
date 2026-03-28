package com.mzansi.microtrips.data.models


import kotlinx.serialization.Serializable

@Serializable
data class Gem(
    val id: Int,
    val name: String,
    val description: String,
    val province: String,
    val category: String,
    val image: String,
    val latitude: Double,
    val longitude: Double,
    val budgetBreakdown: BudgetBreakdown
)

@Serializable
data class BudgetBreakdown(
    val transport: Double,
    val food: Double,
    val entry: Double,
    val misc: Double
) {
    val total: Double
        get() = transport + food + entry + misc
}