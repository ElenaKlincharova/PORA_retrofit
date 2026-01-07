package com.example.retrofit_demo.model

import com.google.gson.annotations.SerializedName


data class ProductsResponse(
    @SerializedName("products")
    val products: List<Product>,

    @SerializedName("total")
    val total: Int,

    @SerializedName("skip")
    val skip: Int,

    @SerializedName("limit")
    val limit: Int
)

data class Product(
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("price")
    val price: Double,

    @SerializedName("discountPercentage")
    val discountPercentage: Double? = null,

    @SerializedName("rating")
    val rating: Double? = null,

    @SerializedName("stock")
    val stock: Int,

    @SerializedName("brand")
    val brand: String? = null,

    @SerializedName("category")
    val category: String,

    @SerializedName("thumbnail")
    val thumbnail: String? = null
)

data class AddProductRequest(
    val title: String,
    val description: String,
    val price: Double,
    val stock: Int,
    val category: String
)

data class UpdateProductRequest(
    val title: String? = null,
    val price: Double? = null,
    val stock: Int? = null
)