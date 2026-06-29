package com.example.foodyperson.data

data class User(
    val userId: String = "",
    val name: String = "",
    val fatherName: String = "",
    val username: String = "",
    val age: Int = 0,
    val email: String = ""
)

data class UserAddress(
    val id: String = java.util.UUID.randomUUID().toString(),
    val label: String = "Home",
    val fullAddress: String = "",
    val phoneNumber: String = "",
    val isDefault: Boolean = false
)

data class Product(
    val productId: String = "",
    val productName: String = "",
    val category: String = "",
    val price: Double = 0.0,
    val description: String = "",
    val imageUrl: Any = "",
    val deliveryTime: String = "",
    val isFavorite: Boolean = false
)

data class CartItem(
    val productId: String = "",
    val productName: String = "",
    val price: Double = 0.0,
    val quantity: Int = 1,
    val imageUrl: Any = ""
) {
    val totalPrice: Double get() = price * quantity
}

data class Order(
    val orderId: String = "",
    val items: List<CartItem> = emptyList(),
    val totalAmount: Double = 0.0,
    val orderDate: Long = System.currentTimeMillis(),
    val status: String = "Delivered"
)
