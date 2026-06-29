package com.example.foodyperson.ui.product

// zeeshan
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.foodyperson.R
import com.example.foodyperson.data.Product
import com.example.foodyperson.ui.home.ProductCard
import com.example.foodyperson.ui.theme.FoodyPersonTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryProductsScreen(
    categoryName: String,
    onBackClick: () -> Unit,
    onProductClick: (Product) -> Unit,
    isFavorite: (String) -> Boolean = { false },
    onToggleFavorite: (Product) -> Unit = {}
) {
    val products = when (categoryName) {
        "Fast Food" -> listOf(
            Product("ff1", "Zinger Burger", "Fast Food", 550.0, "Delicious Zinger Burger", R.drawable.burger, "20-40 min"),
            Product("ff2", "Chicken Burger", "Fast Food", 450.0, "Crispy Chicken Burger", R.drawable.burger, "20-40 min"),
            Product("ff3", "Pizza", "Fast Food", 1200.0, "Cheesy Italian Pizza", R.drawable.pizza, "20-40 min"),
            Product("ff4", "Fries", "Fast Food", 250.0, "Crispy French Fries", R.drawable.loaded_fries, "20-40 min"),
            Product("ff5", "Shawarma", "Fast Food", 300.0, "Spicy Chicken Shawarma", R.drawable.shawarma, "20-40 min"),
            Product("ff6", "Sandwich", "Fast Food", 350.0, "Club Sandwich", R.drawable.sandwitch, "20-40 min"),
            Product("ff7", "Cold Drink", "Fast Food", 150.0, "Refreshing Cold Drink", R.drawable.logo, "20-40 min")
        )
        "Grocery" -> listOf(
            Product("gr1", "Rice 1kg", "Grocery", 350.0, "Premium Basmati Rice", R.drawable.rice, "30-60 min"),
            Product("gr2", "Sugar 1kg", "Grocery", 180.0, "Refined White Sugar", R.drawable.sugar, "30-60 min"),
            Product("gr3", "Milk", "Grocery", 250.0, "Fresh Milk 1L", R.drawable.milk, "30-60 min"),
            Product("gr4", "Tea Pack", "Grocery", 650.0, "Strong Black Tea", R.drawable.logo, "30-60 min"),
            Product("gr5", "Cooking Oil", "Grocery", 850.0, "Pure Vegetable Oil", R.drawable.oil, "30-60 min"),
            Product("gr6", "Eggs (Dozen)", "Grocery", 320.0, "Farm Fresh Eggs", R.drawable.logo, "30-60 min"),
            Product("gr7", "Bread", "Grocery", 120.0, "Soft White Bread", R.drawable.bread, "30-60 min")
        )
        "Drinks" -> listOf(
            Product("dr1", "Coke", "Drinks", 150.0, "Ice Cold Coca-Cola", "https://img.freepik.com/free-photo/coca-cola-bottle_144627-23467.jpg", "10-20 min"),
            Product("dr2", "Sprite", "Drinks", 150.0, "Refreshing Sprite", "https://img.freepik.com/free-photo/sprite-logo_144627-23472.jpg", "10-20 min"),
            Product("dr3", "Fresh Juice", "Drinks", 200.0, "Freshly Squeezed Juice", R.drawable.logo, "10-20 min")
        )
        else -> emptyList()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(categoryName, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (products.isEmpty() && categoryName == "Office Products") {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text(text = "Office Products feature will be available in future updates.", textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(padding)
            ) {
                items(products) { product ->
                    ProductCard(
                        product = product, 
                        onClick = onProductClick,
                        isFavorite = isFavorite(product.productId),
                        onToggleFavorite = { onToggleFavorite(product) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryProductsScreenPreview() {
    FoodyPersonTheme {
        CategoryProductsScreen(
            categoryName = "Fast Food",
            onBackClick = {},
            onProductClick = {}
        )
    }
}
