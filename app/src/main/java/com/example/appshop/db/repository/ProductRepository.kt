package com.example.appshop.db.repository

import com.example.appshop.model.Product
import com.example.appshop.network.RetroFitInstance


class ProductRepository {
    suspend fun getProducts(): List<Product> {
        return RetroFitInstance.api.getProducts()
    }
}