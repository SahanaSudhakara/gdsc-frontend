package com.plcoding.composegooglesignincleanarchitecture.presentation.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.plcoding.composegooglesignincleanarchitecture.presentation.sign_in.UserData

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
