package com.plcoding.composegooglesignincleanarchitecture.presentation.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.plcoding.composegooglesignincleanarchitecture.presentation.sign_in.register.UserData

@Composable
fun ProfileTab(userData: UserData?, onSignOut: () -> Unit, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .size(48.dp)
            .clickable(onClick = onClick)
    ) {
        Box(modifier = Modifier.fillMaxSize()) { // Center the image
            when {
                userData?.profilePictureUrl != null -> { // Check for null URL
                    val painter = rememberImagePainter(
                        data = userData.profilePictureUrl,
                        //context = LocalContext.current // Provide context for Coil
                    )
                    Image(
                        painter = painter,
                        contentDescription = "Profile picture",
                        modifier = Modifier.size(48.dp),
                        contentScale = ContentScale.Crop
                    )
                }
                else -> {
//                    // Display placeholder
//                    Image(
//                        painter = painterResource(id = R.drawable.your_placeholder_drawable),
//                        contentDescription = "Placeholder profile picture",
//                        modifier = Modifier.size(48.dp),
//                        contentScale = ContentScale.Crop
//                    )
                }
            }
        }
    }
}
