package com.example.foodyperson.ui.profile

// zeeshan
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.foodyperson.R
import com.example.foodyperson.ui.theme.FoodyPersonTheme
import com.example.foodyperson.ui.theme.PrimaryGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onEditProfileClick: () -> Unit = {},
    onAddressManagementClick: () -> Unit = {},
    onChangePasswordClick: () -> Unit = {},
    onOrderHistoryClick: () -> Unit = {},
    isDarkMode: Boolean = false,
    onThemeToggle: (Boolean) -> Unit = {},
    userName: String = "Foody User",
    userEmail: String = "foody.user@example.com"
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Profile", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            
            // Profile Image Placeholder
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(text = userName, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text(text = userEmail, fontSize = 14.sp, color = Color.Gray)
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Profile Options
            ProfileOption(
                icon = Icons.Default.PersonOutline, 
                label = "Edit Profile", 
                onClick = onEditProfileClick
            )
            ProfileOption(
                icon = Icons.Default.LocationOn, 
                label = "Address Management", 
                onClick = onAddressManagementClick
            )
            ProfileOption(
                icon = Icons.Default.LockOpen, 
                label = "Change Password", 
                onClick = onChangePasswordClick
            )
            ProfileOption(
                icon = Icons.Default.History, 
                label = "Order History", 
                onClick = onOrderHistoryClick
            )

            // Dark Mode Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFF5F5F5)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isDarkMode) Icons.Default.DarkMode else Icons.Default.LightMode,
                        contentDescription = null,
                        tint = PrimaryGreen,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "Dark Mode", modifier = Modifier.weight(1f), fontSize = 16.sp, fontWeight = FontWeight.Medium)
                Switch(
                    checked = isDarkMode,
                    onCheckedChange = onThemeToggle,
                    colors = SwitchDefaults.colors(checkedThumbColor = PrimaryGreen)
                )
            }

            ProfileOption(icon = Icons.Default.NotificationsNone, label = "Notifications")
            
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Support", 
                fontSize = 14.sp, 
                fontWeight = FontWeight.Bold, 
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )
            
            ProfileOption(icon = Icons.AutoMirrored.Filled.HelpOutline, label = "Help & Support")
            ProfileOption(icon = Icons.Default.ContactSupport, label = "Contact us")
            ProfileOption(icon = Icons.Default.Quiz, label = "FAQs")
            ProfileOption(icon = Icons.Default.Chat, label = "Live chat")
            
            Spacer(modifier = Modifier.height(24.dp))
            
            ProfileOption(
                icon = Icons.AutoMirrored.Filled.Logout, 
                label = "Logout", 
                onClick = onLogoutClick
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = onLogoutClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFEBEE))
            ) {
                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, tint = Color.Red)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Logout", color = Color.Red, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ProfileOption(icon: ImageVector, label: String, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFF5F5F5)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = PrimaryGreen, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = label, modifier = Modifier.weight(1f), fontSize = 16.sp, fontWeight = FontWeight.Medium)
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.LightGray)
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    FoodyPersonTheme {
        ProfileScreen(
            onBackClick = {},
            onLogoutClick = {}
        )
    }
}
