package com.example.retrofit_demo

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retrofit_demo.adapter.ProductAdapter
import com.example.retrofit_demo.api.RetrofitClient
import com.example.retrofit_demo.databinding.ActivityMainBinding
import com.example.retrofit_demo.model.AddProductRequest
import com.example.retrofit_demo.model.Product
import com.example.retrofit_demo.model.UpdateProductRequest
import kotlinx.coroutines.launch
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ProductAdapter
    private val apiService = RetrofitClient.apiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupButtons()


        loadAllProducts()
    }

    private fun setupRecyclerView() {
        adapter = ProductAdapter(
            onItemClick = { product -> showProductDetails(product) },
            onDelete = { product -> deleteProductItem(product) }
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun setupButtons() {
        binding.btnGetAll.setOnClickListener {
            loadAllProducts()
        }


        binding.btnGetCompleted.setOnClickListener {
            searchProducts()
        }


        binding.btnGetById.setOnClickListener {
            loadProductById(1)
        }


        binding.btnCheckExists.setOnClickListener {
            checkProductExists(1)
        }


        binding.btnCreate.setOnClickListener {
            createNewProduct()
        }


        binding.btnCreateFields.setOnClickListener {
            createProductWithFields()
        }


        binding.btnUpdate.setOnClickListener {
            updateEntireProduct()
        }


        binding.btnPatch.setOnClickListener {
            patchProductPrice()
        }


        binding.btnError.setOnClickListener {
            demonstrateErrorHandling()
        }
    }

    private fun loadAllProducts() {
        showLoading(true)
        lifecycleScope.launch {
            try {
                val response = apiService.getAllProducts(limit = 10)

                if (response.isSuccessful) {
                    response.body()?.let { productsResponse ->
                        adapter.submitList(productsResponse.products)
                        showMessage("✓ Loaded ${productsResponse.products.size} products (Total: ${productsResponse.total})")
                        logResponse("GET /products", response)
                    }
                } else {
                    handleErrorResponse("GET", response.code(), response.message())
                }
            } catch (e: Exception) {
                handleException("GET /products", e)
            } finally {
                showLoading(false)
            }
        }
    }

    private fun searchProducts() {
        showLoading(true)
        lifecycleScope.launch {
            try {
                val response = apiService.searchProducts("phone")

                if (response.isSuccessful) {
                    response.body()?.let { productsResponse ->
                        adapter.submitList(productsResponse.products)
                        showMessage("✓ Found ${productsResponse.products.size} products (@Query demo: search='phone')")
                        logResponse("GET /products/search?q=phone", response)
                    }
                }
            } catch (e: Exception) {
                handleException("GET with @Query", e)
            } finally {
                showLoading(false)
            }
        }
    }

    private fun loadProductById(id: Int) {
        showLoading(true)
        lifecycleScope.launch {
            try {
                val response = apiService.getProductById(id)

                if (response.isSuccessful) {
                    response.body()?.let { product ->
                        adapter.submitList(listOf(product))
                        showMessage("✓ Loaded product #$id")
                        logResponse("GET /products/{id}", response)
                    }
                } else if (response.code() == 404) {
                    showMessage("✗ Product #$id not found (404 error)")
                }
            } catch (e: Exception) {
                handleException("GET /products/$id", e)
            } finally {
                showLoading(false)
            }
        }
    }


    private fun checkProductExists(id: Int) {
        lifecycleScope.launch {
            try {
                val response = apiService.checkProductExists(id)

                if (response.isSuccessful) {
                    showMessage("✓ Product #$id exists (@HEAD: ${response.code()})")
                } else {
                    showMessage("✗ Product #$id not found (@HEAD: ${response.code()})")
                }
            } catch (e: Exception) {
                handleException("HEAD /products/$id", e)
            }
        }
    }


    private fun createNewProduct() {
        showLoading(true)
        lifecycleScope.launch {
            try {
                val newProduct = AddProductRequest(
                    title = "Demo Product ${System.currentTimeMillis()}",
                    description = "Created via Retrofit @Body demo",
                    price = 99.99,
                    stock = 50,
                    category = "electronics"
                )

                val response = apiService.addProduct(newProduct)

                if (response.isSuccessful) {
                    response.body()?.let { product ->
                        showMessage("✓ Created product #${product.id} (@Body demo)")
                        logResponse("POST /products/add", response)

                        val currentList = adapter.currentList.toMutableList()
                        currentList.add(0, product)
                        adapter.submitList(currentList)
                    }
                }
            } catch (e: Exception) {
                handleException("POST /products/add", e)
            } finally {
                showLoading(false)
            }
        }
    }

    private fun createProductWithFields() {
        showLoading(true)
        lifecycleScope.launch {
            try {
                val response = apiService.addProductWithFields(
                    title = "Product with @Field",
                    price = 49.99,
                    stock = 25,
                    category = "accessories"
                )

                if (response.isSuccessful) {
                    response.body()?.let { product ->
                        showMessage("✓ Created with @Field (form-encoded)")
                        logResponse("POST with @Field", response)

                        val currentList = adapter.currentList.toMutableList()
                        currentList.add(0, product)
                        adapter.submitList(currentList)
                    }
                }
            } catch (e: Exception) {
                handleException("POST with @Field", e)
            } finally {
                showLoading(false)
            }
        }
    }


    private fun updateEntireProduct() {
        showLoading(true)
        lifecycleScope.launch {
            try {
                val updatedProduct = AddProductRequest(
                    title = "Updated Product (PUT)",
                    description = "Fully updated via PUT",
                    price = 149.99,
                    stock = 100,
                    category = "electronics"
                )

                val response = apiService.updateProduct(1, updatedProduct)

                if (response.isSuccessful) {
                    response.body()?.let { product ->
                        showMessage("✓ Updated entire product #1 (@PUT demo)")
                        logResponse("PUT /products/1", response)

                        val currentList = adapter.currentList.toMutableList()
                        val index = currentList.indexOfFirst { it.id == 1 }
                        if (index != -1) {
                            currentList[index] = product
                            adapter.submitList(currentList)
                        }
                    }
                }
            } catch (e: Exception) {
                handleException("PUT /products/1", e)
            } finally {
                showLoading(false)
            }
        }
    }

    private fun patchProductPrice() {
        lifecycleScope.launch {
            try {
                val priceUpdate = UpdateProductRequest(price = 199.99)
                val response = apiService.updateProductPartial(1, priceUpdate)

                if (response.isSuccessful) {
                    response.body()?.let { product ->
                        showMessage("✓ Patched product #1 price to $${product.price} (@PATCH demo)")
                        logResponse("PATCH /products/1", response)

                        val currentList = adapter.currentList.toMutableList()
                        val index = currentList.indexOfFirst { it.id == 1 }
                        if (index != -1) {
                            currentList[index] = product
                            adapter.submitList(currentList)
                        }
                    }
                }
            } catch (e: Exception) {
                handleException("PATCH /products/1", e)
            }
        }
    }


    private fun deleteProductItem(product: Product) {
        lifecycleScope.launch {
            try {
                val response = apiService.deleteProduct(product.id)

                if (response.isSuccessful) {
                    showMessage("✓ Deleted product #${product.id} (@DELETE demo)")

                    val currentList = adapter.currentList.toMutableList()
                    currentList.remove(product)
                    adapter.submitList(currentList)
                }
            } catch (e: Exception) {
                handleException("DELETE /products/${product.id}", e)
            }
        }
    }


    private fun demonstrateErrorHandling() {
        lifecycleScope.launch {
            try {
                val response = apiService.getProductById(99999)

                if (!response.isSuccessful) {
                    showMessage("✗ Error ${response.code()}: ${response.message()}")
                }
            } catch (e: Exception) {
                handleException("Error demo", e)
            }
        }
    }


    private fun showProductDetails(product: Product) {
        val details = """
            ID: ${product.id}
            Title: ${product.title}
            Price: $${product.price}
            Stock: ${product.stock}
            Category: ${product.category}
            ${product.description ?: ""}
        """.trimIndent()

        Toast.makeText(this, details, Toast.LENGTH_LONG).show()
    }

    private fun handleErrorResponse(method: String, code: Int, message: String) {
        val errorMsg = when (code) {
            404 -> "Not Found (404)"
            500 -> "Server Error (500)"
            401 -> "Unauthorized (401)"
            403 -> "Forbidden (403)"
            else -> "Error $code: $message"
        }
        showMessage("✗ $method failed: $errorMsg")
    }

    private fun handleException(operation: String, exception: Throwable) {
        val errorMsg = when (exception) {
            is UnknownHostException -> "No internet connection"
            is SocketTimeoutException -> "Request timeout"
            else -> exception.message ?: "Unknown error"
        }
        showMessage("✗ $operation: $errorMsg")
    }

    private fun logResponse(endpoint: String, response: Response<*>) {
        println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
        println("Endpoint: $endpoint")
        println("Status: ${response.code()} ${response.message()}")
        println("Headers: ${response.headers()}")
        println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
    }

    private fun showMessage(message: String) {
        runOnUiThread {
            binding.tvStatus.text = message
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        runOnUiThread {
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.recyclerView.alpha = if (isLoading) 0.5f else 1.0f
        }
    }
}