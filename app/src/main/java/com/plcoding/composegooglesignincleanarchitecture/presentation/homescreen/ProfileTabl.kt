import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.plcoding.composegooglesignincleanarchitecture.presentation.sign_in.UserData

@Composable
fun ProfileTab(userData: UserData?, onSignOut: () -> Unit, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clickable(onClick = onClick)
    ) {
        userData?.profilePictureUrl?.let { url ->
            Image(
                painter = rememberImagePainter(url),
                contentDescription = "Profile picture",
                modifier = Modifier.size(48.dp),
                contentScale = ContentScale.Crop,
                // Remove the shape parameter as Image() does not support it
            )
        }
    }
}
