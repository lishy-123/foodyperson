package com.example.foodyperson.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.LocalGroceryStore
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.foodyperson.R
import com.example.foodyperson.data.Product
import com.example.foodyperson.ui.theme.FoodyPersonTheme
import com.example.foodyperson.ui.theme.PrimaryGreen

@Composable
fun HomeScreen(
    onProductClick: (Product) -> Unit,
    onCategoryClick: (String) -> Unit,
    onCartClick: () -> Unit,
    onProfileClick: () -> Unit,
    onFavoritesClick: () -> Unit = {},
    isFavorite: (String) -> Boolean = { false },
    onToggleFavorite: (Product) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    
    val allProducts = listOf(
        Product("1", "Zinger Burger", "Fast Food", 550.0, "Delicious Zinger Burger", R.drawable.burger, "20 min"),
        Product("2", "Cheese Pizza", "Fast Food", 1200.0, "Melting Cheese Pizza", R.drawable.pizza, "30 min"),
        Product("3", "Beef Burger", "Meat", 700.0, "Juicy Beef Burger", R.drawable.burger, "25 min"),
        Product("4", "Coke", "Drinks", 150.0, "Ice Cold Coca-Cola", "https://img.freepik.com/free-photo/coca-cola-bottle_144627-23467.jpg", "10 min"),
        Product("5", "Sprite", "Drinks", 150.0, "Refreshing Sprite", "https://img.freepik.com/free-photo/sprite-logo_144627-23472.jpg", "10 min"),
        Product("6", "Fresh Juice", "Drinks", 200.0, "Refreshing Fresh Orange Juice", R.drawable.logo, "10 min"),
        Product("7", "Chicken Karahi", "Meat", 1500.0, "Traditional Spicy Chicken Karahi", R.drawable.logo, "40 min")
    )

    val filteredProducts = remember(searchQuery) {
        if (searchQuery.isEmpty()) {
            allProducts
        } else {
            allProducts.filter { it.productName.contains(searchQuery, ignoreCase = true) }
        }
    }

    Scaffold(
        bottomBar = { 
            BottomNavigationBar(
                onCartClick = onCartClick, 
                onProfileClick = onProfileClick,
                onFavoritesClick = onFavoritesClick
            ) 
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Header(
                searchQuery = searchQuery, 
                onSearchQueryChange = { searchQuery = it },
                onProfileClick = onProfileClick
            )
            Spacer(modifier = Modifier.height(16.dp))
            CategoriesSection(onCategoryClick)
            Spacer(modifier = Modifier.height(24.dp))
            PromoBanner()
            Spacer(modifier = Modifier.height(24.dp))
            BestSellersSection(
                products = filteredProducts,
                onProductClick = onProductClick, 
                isFavorite = isFavorite, 
                onToggleFavorite = onToggleFavorite,
                onSeeAllClick = { searchQuery = "" }
            )
        }
    }
}

@Composable
fun Header(
    searchQuery: String, 
    onSearchQueryChange: (String) -> Unit,
    onProfileClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = "Hello 👋", fontSize = 14.sp, color = Color.Gray)
                    Text(
                        text = "Foody User",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
            IconButton(
                onClick = onProfileClick,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF5F5F5))
            ) {
                Icon(Icons.Default.Person, contentDescription = "Profile", tint = PrimaryGreen)
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Search Bar
        androidx.compose.material3.OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            placeholder = { Text("Search your groceries", fontSize = 14.sp, color = Color.Gray) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
            shape = RoundedCornerShape(26.dp),
            colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryGreen,
                unfocusedBorderColor = Color(0xFFEEEEEE),
                focusedContainerColor = Color(0xFFF9F9F9),
                unfocusedContainerColor = Color(0xFFF9F9F9)
            ),
            singleLine = true
        )
    }
}

@Composable
fun CategoriesSection(onCategoryClick: (String) -> Unit) {
    val categories = listOf(
        CategoryItemData("Meat", Icons.Default.Restaurant),
        CategoryItemData("Fast Food", Icons.Default.Fastfood),
        CategoryItemData("Grocery", Icons.Default.LocalGroceryStore),
        CategoryItemData("Drinks", Icons.Default.LocalDrink)
    )

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(categories) { category ->
            CategoryItem(category) { onCategoryClick(category.name) }
        }
    }
}

data class CategoryItemData(val name: String, val icon: ImageVector)

@Composable
fun CategoryItem(category: CategoryItemData, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFF5F5F5)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = category.icon,
                contentDescription = category.name,
                tint = PrimaryGreen,
                modifier = Modifier.size(30.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = category.name, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun PromoBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1B5E20)) // Dark Green
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "New Year Offer", color = Color.White, fontSize = 14.sp)
                Text(
                    text = "30% OFF",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(text = "16 - 31 Dec", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Text("Get Now", fontSize = 12.sp)
                }
            }
            // Placeholder for promo image
            Icon(
                Icons.Default.Celebration,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(100.dp)
            )
        }
    }
}

@Composable
fun BestSellersSection(
    products: List<Product>,
    onProductClick: (Product) -> Unit,
    isFavorite: (String) -> Boolean,
    onToggleFavorite: (Product) -> Unit,
    onSeeAllClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Best Sellers", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(
            text = "See All", 
            fontSize = 14.sp, 
            color = PrimaryGreen, 
            modifier = Modifier.clickable { onSeeAllClick() }
        )
    }
    Spacer(modifier = Modifier.height(16.dp))

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxHeight()
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

@Composable
fun ProductCard(
    product: Product, 
    onClick: (Product) -> Unit,
    isFavorite: Boolean = false,
    onToggleFavorite: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(product) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF5F5F5)),
                contentAlignment = Alignment.Center
            ) {
                if (product.imageUrl.toString().isNotEmpty()) {
                    AsyncImage(
                        model = product.imageUrl,
                        contentDescription = product.productName,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop
                    )
                } else {
                    Icon(
                        Icons.Default.Fastfood,
                        contentDescription = null,
                        tint = Color.LightGray,
                        modifier = Modifier.size(48.dp)
                    )
                }
                
                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color.Red else Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = product.productName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(text = "PKR ${product.price}", color = Color.Gray, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Timer, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                    Text(text = product.deliveryTime, fontSize = 10.sp, color = Color.Gray)
                }
                IconButton(
                    onClick = { },
                    modifier = Modifier.size(24.dp).background(PrimaryGreen, RoundedCornerShape(4.dp))
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    onCartClick: () -> Unit, 
    onProfileClick: () -> Unit,
    onFavoritesClick: () -> Unit = {}
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = true,
            onClick = { },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = PrimaryGreen, selectedTextColor = PrimaryGreen)
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.FavoriteBorder, contentDescription = "Favorites") },
            label = { Text("Favorites") },
            selected = false,
            onClick = onFavoritesClick
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Cart") },
            label = { Text("Cart") },
            selected = false,
            onClick = onCartClick
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = false,
            onClick = onProfileClick
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    FoodyPersonTheme {
        HomeScreen(
            onProductClick = {},
            onCategoryClick = {},
            onCartClick = {},
            onProfileClick = {}
        )
    }
}
