package com.example.foodyperson

// zeeshan
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.foodyperson.ui.auth.LoginScreen
import com.example.foodyperson.ui.auth.OnboardingScreen
import com.example.foodyperson.ui.auth.SignUpScreen
import com.example.foodyperson.ui.home.HomeScreen
import com.example.foodyperson.ui.product.ProductDetailScreen
import com.example.foodyperson.ui.product.CategoryProductsScreen
import com.example.foodyperson.ui.cart.CartScreen
import com.example.foodyperson.ui.profile.*
import com.example.foodyperson.ui.favorite.FavoritesScreen
import com.example.foodyperson.ui.theme.FoodyPersonTheme
import com.example.foodyperson.ui.theme.PrimaryGreen
import com.example.foodyperson.data.Product
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val mainViewModel: MainViewModel = viewModel()
            FoodyPersonTheme(darkTheme = mainViewModel.isDarkMode) {
                val navController = rememberNavController()

                // Determine start destination
                val startDestination = if (com.google.firebase.auth.FirebaseAuth.getInstance().currentUser != null) "home" else "splash"

                Column(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier.weight(1f)) {
                        NavHost(navController = navController, startDestination = startDestination) {
                            composable("splash") {
                                SplashScreen(onTimeout = {
                                    navController.navigate("login") {
                                        popUpTo("splash") { inclusive = true }
                                    }
                                })
                            }
                            composable("login") {
                                LoginScreen(
                                    onLoginClick = { email, password, onComplete ->
                                        mainViewModel.login(email, password) { success, error ->
                                            if (success) {
                                                navController.navigate("home") {
                                                    popUpTo("login") { inclusive = true }
                                                }
                                            }
                                            onComplete(success, error)
                                        }
                                    },
                                    onSignUpClick = {
                                        navController.navigate("signup")
                                    },
                                    onGoogleLoginClick = { idToken ->
                                        mainViewModel.signInWithGoogle(idToken) { success, error ->
                                            if (success) {
                                                navController.navigate("home") {
                                                    popUpTo("login") { inclusive = true }
                                                }
                                            }
                                        }
                                    }
                                )
                            }
                            composable("signup") {
                                SignUpScreen(
                                    onSignUpClick = { name, email, password, onComplete ->
                                        mainViewModel.signUp(email, password, name) { success, error ->
                                            if (success) {
                                                navController.navigate("onboarding") {
                                                    popUpTo("login") { inclusive = true }
                                                }
                                            }
                                            onComplete(success, error)
                                        }
                                    },
                                    onLoginClick = {
                                        navController.popBackStack()
                                    },
                                    onGoogleSignUpClick = { idToken ->
                                        mainViewModel.signInWithGoogle(idToken) { success, error ->
                                            if (success) {
                                                navController.navigate("home") {
                                                    popUpTo("signup") { inclusive = true }
                                                }
                                            }
                                        }
                                    }
                                )
                            }
                            composable("onboarding") {
                                OnboardingScreen(
                                    onGetStartedClick = {
                                        navController.navigate("home") {
                                            popUpTo("onboarding") { inclusive = true }
                                        }
                                    }
                                )
                            }
                            composable("home") {
                                HomeScreen(
                                    onProductClick = { product ->
                                        MainActivity.currentSelectedProduct = product
                                        navController.navigate("product_detail")
                                    },
                                    onCategoryClick = { category ->
                                        MainActivity.currentSelectedCategory = category
                                        navController.navigate("category_products")
                                    },
                                    onCartClick = {
                                        navController.navigate("cart")
                                    },
                                    onProfileClick = {
                                        navController.navigate("profile")
                                    },
                                    onFavoritesClick = {
                                        navController.navigate("favorites")
                                    },
                                    isFavorite = { productId ->
                                        mainViewModel.isProductFavorite(productId)
                                    },
                                    onToggleFavorite = { product ->
                                        mainViewModel.toggleFavorite(product)
                                    }
                                )
                            }
                            composable("favorites") {
                                FavoritesScreen(
                                    favoriteProducts = mainViewModel.favoriteProducts,
                                    onBackClick = { navController.popBackStack() },
                                    onProductClick = { product ->
                                        MainActivity.currentSelectedProduct = product
                                        navController.navigate("product_detail")
                                    },
                                    onToggleFavorite = { product ->
                                        mainViewModel.toggleFavorite(product)
                                    }
                                )
                            }
                            composable("category_products") {
                                currentSelectedCategory?.let { category ->
                                    CategoryProductsScreen(
                                        categoryName = category,
                                        onBackClick = { navController.popBackStack() },
                                        onProductClick = { product ->
                                            MainActivity.currentSelectedProduct = product
                                            navController.navigate("product_detail")
                                        },
                                        isFavorite = { productId ->
                                            mainViewModel.isProductFavorite(productId)
                                        },
                                        onToggleFavorite = { product ->
                                            mainViewModel.toggleFavorite(product)
                                        }
                                    )
                                }
                            }
                            composable("profile") {
                                ProfileScreen(
                                    userName = mainViewModel.currentUser.name,
                                    userEmail = mainViewModel.currentUser.email,
                                    isDarkMode = mainViewModel.isDarkMode,
                                    onThemeToggle = { mainViewModel.toggleTheme() },
                                    onBackClick = { navController.popBackStack() },
                                    onLogoutClick = {
                                        mainViewModel.logout()
                                        navController.navigate("login") {
                                            popUpTo("home") { inclusive = true }
                                        }
                                    },
                                    onEditProfileClick = { navController.navigate("edit_profile") },
                                    onAddressManagementClick = { navController.navigate("address_management") },
                                    onChangePasswordClick = { navController.navigate("change_password") },
                                    onOrderHistoryClick = { navController.navigate("order_history") }
                                )
                            }
                            composable("address_management") {
                                AddressManagementScreen(
                                    addresses = mainViewModel.userAddresses,
                                    onBackClick = { navController.popBackStack() },
                                    onAddAddress = { mainViewModel.addAddress(it) },
                                    onUpdateAddress = { mainViewModel.updateAddress(it) },
                                    onDeleteAddress = { mainViewModel.deleteAddress(it) }
                                )
                            }
                            composable("edit_profile") {
                                EditProfileScreen(
                                    user = mainViewModel.currentUser,
                                    onBackClick = { navController.popBackStack() },
                                    onSaveClick = { name, email, username ->
                                        mainViewModel.updateProfile(name, email, username)
                                        navController.popBackStack()
                                    }
                                )
                            }
                            composable("change_password") {
                                ChangePasswordScreen(
                                    onBackClick = { navController.popBackStack() },
                                    onConfirmClick = { _ ->
                                        // Logic for password update
                                        navController.popBackStack()
                                    }
                                )
                            }
                            composable("order_history") {
                                OrderHistoryScreen(
                                    orders = mainViewModel.orderHistory,
                                    onBackClick = { navController.popBackStack() }
                                )
                            }
                            composable("product_detail") {
                                currentSelectedProduct?.let { product ->
                                    ProductDetailScreen(
                                        product = product,
                                        onBackClick = { navController.popBackStack() },
                                        onAddToCart = { quantity ->
                                            mainViewModel.addToCart(product, quantity)
                                            navController.navigate("cart")
                                        },
                                        isFavorite = mainViewModel.isProductFavorite(product.productId),
                                        onToggleFavorite = { mainViewModel.toggleFavorite(product) }
                                    )
                                }
                            }
                            composable("cart") {
                                CartScreen(
                                    cartItems = mainViewModel.cartItems,
                                    onBackClick = { navController.popBackStack() },
                                    onCheckoutClick = {
                                        mainViewModel.checkout()
                                        navController.navigate("home") {
                                            popUpTo("home") { inclusive = true }
                                        }
                                    },
                                    onRemoveItem = { mainViewModel.removeFromCart(it) },
                                    onUpdateQuantity = { item, qty -> mainViewModel.updateQuantity(item, qty) }
                                )
                            }
                        }
                    }
                    com.example.foodyperson.ui.theme.CopyrightFooter()
                }
            }
        }
    }

    companion object {
        // Simple way to share selected product/category for this demo
        var currentSelectedProduct: Product? = null
        var currentSelectedCategory: String? = null
    }
}

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(3000)
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier.size(120.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "FoodyPerson",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryGreen
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    FoodyPersonTheme {
        SplashScreen(onTimeout = {})
    }
}
