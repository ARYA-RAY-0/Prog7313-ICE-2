package com.mzansi.microtrips.data.source

import android.content.Context
import android.util.Log
import com.mzansi.microtrips.data.models.Category
import com.mzansi.microtrips.data.models.Gem
import com.mzansi.microtrips.data.models.Province
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import java.io.IOException

class JsonSource(private val context: Context) {

    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    fun loadGems(): List<Gem> {
        return try {
            val jsonString = loadJsonFromAsset("data/gems.json")
            Log.d("JsonSource", "Gems JSON: $jsonString") // ← ADD THIS
            val gems = json.decodeFromString<List<Gem>>(jsonString)
            Log.d("JsonSource", "Loaded ${gems.size} gems") // ← ADD THIS
            gems
        } catch (e: Exception) {
            Log.e("JsonSource", "Error loading gems", e) // ← ADD THIS
            e.printStackTrace()
            emptyList()
        }
    }

    fun loadCategories(): List<Category> {
        return try {
            val jsonString = loadJsonFromAsset("data/categories.json")
            json.decodeFromString<List<Category>>(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    fun loadProvinces(): List<Province> {
        return try {
            val jsonString = loadJsonFromAsset("data/provinces.json")
            json.decodeFromString<List<Province>>(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    private fun loadJsonFromAsset(fileName: String): String {
        try {
            // Test reading the test file first
            val test = context.assets.open("test.txt").bufferedReader().use { it.readText() }
            Log.d("TEST", "Test file says: $test")

            // Then read the actual file
            return context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (e: Exception) {
            Log.e("TEST", "Error: ${e.message}")
            return ""
        }
    }  // ← THIS closing brace for the function
}  // ← THIS closing brace for the class

