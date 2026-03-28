package com.mzansi.microtrips.data.repository

import com.mzansi.microtrips.data.models.Category
import com.mzansi.microtrips.data.models.Gem
import com.mzansi.microtrips.data.models.Province
import com.mzansi.microtrips.data.source.JsonSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GemsRepository(private val dataSource: JsonSource) {

    private val _gems = MutableStateFlow<List<Gem>>(emptyList())
    val gems: StateFlow<List<Gem>> = _gems.asStateFlow()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _provinces = MutableStateFlow<List<Province>>(emptyList())
    val provinces: StateFlow<List<Province>> = _provinces.asStateFlow()

    private val _favorites = MutableStateFlow<Set<Int>>(emptySet())
    val favorites: StateFlow<Set<Int>> = _favorites.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        _gems.value = dataSource.loadGems()
        _categories.value = dataSource.loadCategories()
        _provinces.value = dataSource.loadProvinces()
    }

    fun getGemById(id: Int): Gem? {
        return _gems.value.find { it.id == id }
    }

    fun getCategoryName(categoryId: Int): String {
        return _categories.value.find { it.id == categoryId }?.name ?: "Unknown"
    }

    fun getProvinceName(provinceId: Int): String {
        return _provinces.value.find { it.id == provinceId }?.name ?: "Unknown"
    }

    fun toggleFavorite(gemId: Int) {
        val current = _favorites.value.toMutableSet()
        if (current.contains(gemId)) {
            current.remove(gemId)
        } else {
            current.add(gemId)
        }
        _favorites.value = current
    }

    fun isFavorite(gemId: Int): Boolean {
        return _favorites.value.contains(gemId)
    }

    fun getFavoriteGems(): List<Gem> {
        return _gems.value.filter { _favorites.value.contains(it.id) }
    }
}