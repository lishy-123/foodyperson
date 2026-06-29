@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.foodyperson.ui.cart

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.foodyperson.R
import com.example.foodyperson.data.CartItem
import com.example.foodyperson.ui.theme.FoodyPersonTheme

// Colors from the image
val DarkGreenBg = Color(0xFF01241D)
val CardBeige = Color(0xFFFEF3BB)
val LightText = Color(0xFFFEF3BB)
val DarkText = Color(0xFF01241D)
val GreenButton = Color(0xFF96B23C)
val OrangePayment = Color(0xFFF9A825)
val QuantityIconColor = Color(0xFF7A912D)

@Composable
fun CartScreen(
    cartItems: List<CartItem>,
    onBackClick: () -> Unit,
    onCheckoutClick: () -> Unit,
    onRemoveItem: (CartItem) -> Unit,
    onUpdateQuantity: (CartItem, Int) -> Unit
) {
    var selectedPaymentMethod by remember { mutableStateOf("COD") }
    var deliveryAddress by remember { mutableStateOf("123 Mango Street, South City") }
    var isEditingAddress by remember { mutableStateOf(false) }

    val subtotal = cartItems.sumOf { it.totalPrice }
    val discount = 5.0
    val deliveryCharges = 0.0
    val total = (subtotal - discount + deliveryCharges).coerceAtLeast(0.0)
    
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        containerColor = DarkGreenBg,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            "My Cart",
                            color = LightText,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = LightText
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Menu */ }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = LightText)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(cartItems) { item ->
                    CartItemCard(item, onUpdateQuantity)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Delivered to Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Delivered to", color = LightText, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                IconButton(onClick = { isEditingAddress = !isEditingAddress }) {
                    Icon(
                        if (isEditingAddress) Icons.Default.Check else Icons.Default.Edit,
                        contentDescription = null,
                        tint = GreenButton,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (isEditingAddress) {
                TextField(
                    value = deliveryAddress,
                    onValueChange = { deliveryAddress = it },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.1f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.05f),
                        focusedTextColor = LightText,
                        unfocusedTextColor = LightText,
                        cursorColor = GreenButton
                    ),
                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 14.sp)
                )
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = GreenButton, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(deliveryAddress, color = LightText, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Payment Options Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Payment Options", color = LightText, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Icon(Icons.Default.ArrowForward, contentDescription = null, tint = GreenButton, modifier = Modifier.size(16.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = { selectedPaymentMethod = "Card" },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedPaymentMethod == "Card") OrangePayment else Color.Transparent
                    ),
                    border = if (selectedPaymentMethod == "Card") null else BorderStroke(1.dp, LightText)
                ) {
                    Text(
                        "Credit/Debit", 
                        color = if (selectedPaymentMethod == "Card") DarkGreenBg else LightText, 
                        fontSize = 12.sp,
                        fontWeight = if (selectedPaymentMethod == "Card") FontWeight.Bold else FontWeight.Normal
                    )
                }
                Button(
                    onClick = { selectedPaymentMethod = "COD" },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedPaymentMethod == "COD") OrangePayment else Color.Transparent
                    ),
                    border = if (selectedPaymentMethod == "COD") null else BorderStroke(1.dp, LightText)
                ) {
                    Text(
                        "Cash on Delivery", 
                        color = if (selectedPaymentMethod == "COD") DarkGreenBg else LightText, 
                        fontSize = 12.sp, 
                        fontWeight = if (selectedPaymentMethod == "COD") FontWeight.Bold else FontWeight.Normal
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Summary Section
            SummaryRowDesign("Sub total", "Rs $subtotal")
            SummaryRowDesign("Discount applied", "-Rs $discount")
            SummaryRowDesign("Delivery charge", if (deliveryCharges == 0.0) "Free" else "Rs $deliveryCharges")

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total", color = LightText, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text("Rs $total", color = LightText, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Checkout Button
            Button(
                onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar("Order proceeded successfully!")
                    }
                    onCheckoutClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenButton)
            ) {
                Text("Proceed to Checkout", color = DarkGreenBg, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun CartItemCard(
    item: CartItem,
    onUpdateQuantity: (CartItem, Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(CardBeige)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = null,
                modifier = Modifier.size(60.dp),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = item.productName, color = DarkText, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(text = "Rs ${item.price}/kg", color = Color(0xFFB4A46C), fontSize = 14.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Rs ${item.totalPrice}", color = DarkText, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
        }

        // Quantity Selector
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                Icons.Default.RemoveCircle,
                contentDescription = "Decrease",
                tint = QuantityIconColor,
                modifier = Modifier.size(24.dp).clickable {
                    if (item.quantity > 1) onUpdateQuantity(item, item.quantity - 1)
                }
            )
            Text(
                text = item.quantity.toString(),
                color = DarkText,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Icon(
                Icons.Default.AddCircle,
                contentDescription = "Increase",
                tint = QuantityIconColor,
                modifier = Modifier.size(24.dp).clickable {
                    onUpdateQuantity(item, item.quantity + 1)
                }
            )
        }
    }
}

@Composable
fun SummaryRowDesign(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = LightText, fontSize = 14.sp)
        Text(text = value, color = LightText, fontSize = 14.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun CartScreenPreview() {
    val sampleCartItems = listOf(
        CartItem("1", "Kiwi", 7.50, 3, R.drawable.logo),
        CartItem("2", "Oranges", 5.99, 1, R.drawable.logo)
    )
    CartScreen(
        cartItems = sampleCartItems,
        onBackClick = {},
        onCheckoutClick = {},
        onRemoveItem = {},
        onUpdateQuantity = { _, _ -> }
    )
}
