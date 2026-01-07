package com.example.retrofit_demo.api

import com.example.retrofit_demo.model.AddProductRequest
import com.example.retrofit_demo.model.Product
import com.example.retrofit_demo.model.ProductsResponse
import com.example.retrofit_demo.model.UpdateProductRequest
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ProductApiService {

    @GET("products")
    suspend fun getAllProducts(
        @Query("limit") limit: Int = 10,
        @Query("skip") skip: Int = 0
    ): Response<ProductsResponse>

    @GET("products/{id}")
    suspend fun getProductById(
        @Path("id") productId: Int,
        @Header("User-Agent") userAgent: String = "RetrofitDemo/1.0"
    ): Response<Product>


    @GET("products/category/{category}")
    suspend fun getProductsByCategory(
        @Path("category") category: String
    ): Response<ProductsResponse>


    @GET("products/search")
    suspend fun searchProducts(
        @Query("q") query: String
    ): Response<ProductsResponse>


    @HEAD("products/{id}")
    suspend fun checkProductExists(
        @Path("id") productId: Int
    ): Response<Void>


    @POST("products/add")
    suspend fun addProduct(
        @Body product: AddProductRequest,
        @Header("Content-Type") contentType: String = "application/json"
    ): Response<Product>


    @FormUrlEncoded
    @POST("products/add")
    suspend fun addProductWithFields(
        @Field("title") title: String,
        @Field("price") price: Double,
        @Field("stock") stock: Int,
        @Field("category") category: String
    ): Response<Product>


    @PUT("products/{id}")
    suspend fun updateProduct(
        @Path("id") productId: Int,
        @Body product: AddProductRequest
    ): Response<Product>


    @PATCH("products/{id}")
    suspend fun updateProductPartial(
        @Path("id") productId: Int,
        @Body update: UpdateProductRequest
    ): Response<Product>


    @DELETE("products/{id}")
    suspend fun deleteProduct(
        @Path("id") productId: Int
    ): Response<Product>
}