package com.example.foodyperson

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.foodyperson.data.CartItem
import com.example.foodyperson.data.Order
import com.example.foodyperson.data.Product
import com.example.foodyperson.data.User
import com.example.foodyperson.data.UserAddress
import java.util.UUID

class MainViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    var currentUser by mutableStateOf(User(userId = "1", name = "Foody User", email = "foody.user@example.com", username = "foodyuser"))
        private set

    var isDarkMode by mutableStateOf(false)
        private set

    init {
        auth.addAuthStateListener { firebaseAuth ->
            val userId = firebaseAuth.currentUser?.uid
            if (userId != null) {
                loadUserData(userId)
            } else {
                // Clear data if logged out
                currentUser = User()
                _cartItems.clear()
                _favoriteProducts.clear()
                _orderHistory.clear()
                _userAddresses.clear()
            }
        }
    }

    private fun loadUserData(userId: String) {
        db.collection("users").document(userId).addSnapshotListener { snapshot, _ ->
            if (snapshot != null && snapshot.exists()) {
                snapshot.toObject(User::class.java)?.let { currentUser = it }
            }
        }
        
        loadCart(userId)
        loadFavorites(userId)
        loadOrders(userId)
        loadAddresses(userId)
    }

    private fun loadCart(userId: String) {
        db.collection("users").document(userId).collection("cart").addSnapshotListener { snapshot, _ ->
            _cartItems.clear()
            snapshot?.documents?.forEach { doc ->
                doc.toObject(CartItem::class.java)?.let { _cartItems.add(it) }
            }
        }
    }

    private fun loadFavorites(userId: String) {
        db.collection("users").document(userId).collection("favorites").addSnapshotListener { snapshot, _ ->
            _favoriteProducts.clear()
            snapshot?.documents?.forEach { doc ->
                doc.toObject(Product::class.java)?.let { _favoriteProducts.add(it) }
            }
        }
    }

    private fun loadOrders(userId: String) {
        db.collection("users").document(userId).collection("orders").addSnapshotListener { snapshot, _ ->
            _orderHistory.clear()
            snapshot?.documents?.forEach { doc ->
                doc.toObject(Order::class.java)?.let { _orderHistory.add(it) }
            }
        }
    }

    private fun loadAddresses(userId: String) {
        db.collection("users").document(userId).collection("addresses").addSnapshotListener { snapshot, _ ->
            _userAddresses.clear()
            snapshot?.documents?.forEach { doc ->
                doc.toObject(UserAddress::class.java)?.let { _userAddresses.add(it) }
            }
            if (_userAddresses.isEmpty()) {
                // Initial data if Firestore is empty
                _userAddresses.addAll(listOf(
                    UserAddress(label = "Home", fullAddress = "123 Mango Street, South City", isDefault = true),
                    UserAddress(label = "Office", fullAddress = "456 Tech Park, North Zone")
                ))
            }
        }
    }

    fun toggleTheme() {
        isDarkMode = !isDarkMode
    }

    private val _cartItems = mutableStateListOf<CartItem>()
    val cartItems: List<CartItem> get() = _cartItems

    private val _favoriteProducts = mutableStateListOf<Product>()
    val favoriteProducts: List<Product> get() = _favoriteProducts

    private val _orderHistory = mutableStateListOf<Order>()
    val orderHistory: List<Order> get() = _orderHistory

    private val _userAddresses = mutableStateListOf<UserAddress>()
    val userAddresses: List<UserAddress> get() = _userAddresses

    fun updateProfile(name: String, email: String, username: String) {
        val userId = auth.currentUser?.uid ?: "1"
        val updatedUser = currentUser.copy(name = name, email = email, username = username)
        db.collection("users").document(userId).set(updatedUser)
    }

    fun checkout() {
        if (_cartItems.isNotEmpty()) {
            val userId = auth.currentUser?.uid ?: "1"
            val subtotal = _cartItems.sumOf { it.totalPrice }
            val orderId = UUID.randomUUID().toString().take(8).uppercase()
            val newOrder = Order(
                orderId = orderId,
                items = _cartItems.toList(),
                totalAmount = subtotal + 150.0 // Including delivery
            )
            
            db.collection("users").document(userId).collection("orders").document(orderId).set(newOrder)
            
            // Clear cart in Firestore
            _cartItems.forEach { 
                db.collection("users").document(userId).collection("cart").document(it.productId).delete()
            }
        }
    }

    fun toggleFavorite(product: Product) {
        val userId = auth.currentUser?.uid ?: "1"
        val existing = _favoriteProducts.find { it.productId == product.productId }
        if (existing != null) {
            db.collection("users").document(userId).collection("favorites").document(product.productId).delete()
        } else {
            db.collection("users").document(userId).collection("favorites").document(product.productId).set(product.copy(isFavorite = true))
        }
    }

    fun isProductFavorite(productId: String): Boolean {
        return _favoriteProducts.any { it.productId == productId }
    }

    fun addToCart(product: Product, quantity: Int) {
        val userId = auth.currentUser?.uid ?: "1"
        val existingItem = _cartItems.find { it.productId == product.productId }
        val cartItem = if (existingItem != null) {
            existingItem.copy(quantity = existingItem.quantity + quantity)
        } else {
            CartItem(
                productId = product.productId,
                productName = product.productName,
                price = product.price,
                quantity = quantity,
                imageUrl = product.imageUrl
            )
        }
        db.collection("users").document(userId).collection("cart").document(product.productId).set(cartItem)
    }

    fun removeFromCart(item: CartItem) {
        val userId = auth.currentUser?.uid ?: "1"
        db.collection("users").document(userId).collection("cart").document(item.productId).delete()
    }

    fun updateQuantity(item: CartItem, newQuantity: Int) {
        if (newQuantity > 0) {
            val userId = auth.currentUser?.uid ?: "1"
            db.collection("users").document(userId).collection("cart").document(item.productId).update("quantity", newQuantity)
        }
    }

    fun addAddress(address: UserAddress) {
        val userId = auth.currentUser?.uid ?: "1"
        if (address.isDefault) {
            _userAddresses.forEach { 
                if (it.isDefault) db.collection("users").document(userId).collection("addresses").document(it.id).update("isDefault", false)
            }
        }
        db.collection("users").document(userId).collection("addresses").document(address.id).set(address)
    }

    fun updateAddress(updatedAddress: UserAddress) {
        val userId = auth.currentUser?.uid ?: "1"
        if (updatedAddress.isDefault) {
            _userAddresses.forEach { 
                if (it.isDefault && it.id != updatedAddress.id) {
                    db.collection("users").document(userId).collection("addresses").document(it.id).update("isDefault", false)
                }
            }
        }
        db.collection("users").document(userId).collection("addresses").document(updatedAddress.id).set(updatedAddress)
    }

    fun deleteAddress(addressId: String) {
        val userId = auth.currentUser?.uid ?: "1"
        db.collection("users").document(userId).collection("addresses").document(addressId).delete()
    }

    // Auth functions
    fun signUp(email: String, password: String, name: String, onResult: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = task.result.user?.uid ?: ""
                    val newUser = User(userId = userId, name = name, email = email, username = email.split("@")[0])
                    db.collection("users").document(userId).set(newUser)
                        .addOnCompleteListener { 
                            // Success or failure of Firestore doesn't block the result
                            onResult(true, null)
                        }
                } else {
                    val errorMessage = when (task.exception) {
                        is com.google.firebase.auth.FirebaseAuthException -> {
                            when ((task.exception as com.google.firebase.auth.FirebaseAuthException).errorCode) {
                                "ERROR_INVALID_EMAIL" -> "Invalid email address."
                                "ERROR_EMAIL_ALREADY_IN_USE" -> "Email already in use."
                                "ERROR_WEAK_PASSWORD" -> "Password is too weak."
                                "CONFIGURATION_NOT_FOUND" -> "Firebase Configuration Error: Please enable 'Android Device Verification' in Google Cloud Console and add SHA-1 to Firebase."
                                else -> task.exception?.localizedMessage
                            }
                        }
                        else -> task.exception?.localizedMessage
                    }
                    onResult(false, errorMessage)
                }
            }
    }

    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, null)
                } else {
                    val errorMessage = when (task.exception) {
                        is com.google.firebase.auth.FirebaseAuthException -> {
                            when ((task.exception as com.google.firebase.auth.FirebaseAuthException).errorCode) {
                                "ERROR_INVALID_EMAIL" -> "Invalid email address."
                                "ERROR_WRONG_PASSWORD" -> "Incorrect password."
                                "ERROR_USER_NOT_FOUND" -> "No account found with this email."
                                "CONFIGURATION_NOT_FOUND" -> "Firebase Configuration Error: Please add SHA-1 fingerprint to Firebase Console."
                                else -> task.exception?.localizedMessage
                            }
                        }
                        else -> task.exception?.localizedMessage
                    }
                    onResult(false, errorMessage)
                }
            }
    }

    fun logout() {
        auth.signOut()
        currentUser = User()
        _cartItems.clear()
        _favoriteProducts.clear()
        _orderHistory.clear()
        _userAddresses.clear()
    }

    fun signInWithGoogle(idToken: String, onResult: (Boolean, String?) -> Unit) {
        val credential = com.google.firebase.auth.GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = task.result.user
                    if (user != null) {
                        val userId = user.uid
                        // Check if user exists in Firestore, if not create
                        db.collection("users").document(userId).get().addOnSuccessListener { document ->
                            if (!document.exists()) {
                                val newUser = User(
                                    userId = userId,
                                    name = user.displayName ?: "Google User",
                                    email = user.email ?: "",
                                    username = user.email?.split("@")?.get(0) ?: "user"
                                )
                                db.collection("users").document(userId).set(newUser)
                            }
                            onResult(true, null)
                        }
                    }
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }
}
