package app.waste2wealth.com.challenges

import android.Manifest
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomDrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.material.rememberBottomDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import app.waste2wealth.com.R
import app.waste2wealth.com.bottombar.BottomBar
import app.waste2wealth.com.components.permissions.PermissionDrawer
import app.waste2wealth.com.navigation.Screens
import app.waste2wealth.com.profile.ProfileImage
import app.waste2wealth.com.ui.theme.appBackground
import app.waste2wealth.com.ui.theme.monteBold
import app.waste2wealth.com.ui.theme.textColor
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@OptIn(
    ExperimentalMaterialApi::class, ExperimentalFoundationApi::class,
    ExperimentalPermissionsApi::class, ExperimentalComposeUiApi::class
)
@Composable
fun Community(navController: NavHostController) {
    val user by remember { mutableStateOf(Firebase.auth.currentUser) }
    val cList = listOf("Challenges", "Clubs", "Posts")
    var tabIndex by remember { mutableStateOf(0) }
    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )
    val permissionDrawerState = rememberBottomDrawerState(
        if (permissionState.allPermissionsGranted) BottomDrawerValue.Closed else BottomDrawerValue.Open
    )
    PermissionDrawer(
        drawerState = permissionDrawerState,
        permissionState = permissionState,
        rationaleText = "To continue, allow Report Waste2Wealth to access your device's location" +
                ". Tap Settings > Permission, and turn \"Access Location On\" on.",
        withoutRationaleText = "Location permission required for functionality of this app." +
                " Please grant the permission.",
    ) {
        Scaffold(bottomBar = {
            BottomBar(navController = navController)
        }) {
            println(it)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(appBackground)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 35.dp,
                            bottom = 35.dp,
                            start = 20.dp,
                            end = 20.dp
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Community",
                        color = textColor,
                        fontSize = 35.sp,
                        fontFamily = monteBold,
                    )
                    ProfileImage(
                        imageUrl = user?.photoUrl,
                        modifier = Modifier
                            .size(60.dp)
                            .border(
                                width = 1.dp,
                                color = appBackground,
                                shape = CircleShape
                            )
                            .padding(3.dp)
                            .clip(CircleShape)
                            .clickable {
                                navController.navigate(Screens.Profile.route)
                            },
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 35.dp, end = 35.dp)
                ) {
                    TabRow(
                        selectedTabIndex = tabIndex,
                        backgroundColor = appBackground,
                        contentColor = textColor,
                        divider = {
                            TabRowDefaults.Divider(
                                color = Color(0xFFF37952),
                                thickness = 1.dp
                            )
                        },
                    ) {
                        cList.forEachIndexed { index, title ->
                            Tab(text = {
                                Text(
                                    title,
                                    softWrap = false,
                                    fontSize = 13.sp,
                                )
                            },
                                selected = tabIndex == index,
                                onClick = { tabIndex = index }
                            )
                        }

                    }
                }
                Row(
                    modifier = Modifier
                        .offset(y = (-50).dp)
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(id = R.drawable.kright),
                            contentDescription = "",
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .rotate(180f)
                                .size(15.dp)
                                .padding(end = 0.dp)
                        )

                        Text(
                            modifier = Modifier
                                .vertical()
                                .rotate(-90f)
                                .background(appBackground)
                                .padding(end = 20.dp),
                            text = "Comment",
                        )

                        Spacer(modifier = Modifier.height(50.dp))
                        Text(
                            modifier = Modifier
                                .vertical()
                                .rotate(-90f)
                                .background(appBackground)
                                .padding(end = 20.dp),
                            text = "      Kudos",
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.kright),
                            contentDescription = "",
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .size(15.dp)
                                .padding(end = 0.dp)
                        )

                    }
                    when (tabIndex) {
                        0 -> Challenges()
                        1 -> Clubs()
                        2 -> Posts()
                    }
                }


            }
        }
    }
}


fun Modifier.vertical() =
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        layout(placeable.height, placeable.width) {
            placeable.place(
                x = -(placeable.width / 2 - placeable.height / 2),
                y = -(placeable.height / 2 - placeable.width / 2)
            )
        }
    }

