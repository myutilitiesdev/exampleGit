package com.foryou.examplegit.presentation.ui.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.foryou.examplegit.R
import com.foryou.examplegit.presentation.component.CircularIndeterminateProgressBar
import com.foryou.examplegit.presentation.component.ErrorLayout
import com.foryou.examplegit.presentation.viewmodel.UserDetailViewModel

@Composable
fun UserDetailScreen(modifier: Modifier, navController: NavHostController, username: String) {
    val viewModel: UserDetailViewModel = hiltViewModel()

    val userDetail by viewModel.userDetail.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(username) {
        viewModel.fetchUserDetail(username)
    }

    CircularIndeterminateProgressBar(isDisplayed = isLoading, 0.4f)

    if (!isLoading) {
        userDetail?.let { user ->
            Column(
                modifier = modifier
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                UserDetail(user) { }

                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.Center) {

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray)
                                .padding(18.dp),
                            painter = painterResource(R.drawable.followers),
                            contentDescription = "Followers",
                            colorFilter = ColorFilter.tint(Color.Black)
                        )

                        Text(
                            modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
                            text = "${user.followers}",
                            fontSize = 16.sp
                        )

                        Text(
                            text = "Followers",
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.width(50.dp))

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray)
                                .padding(18.dp),
                            painter = painterResource(R.drawable.following),
                            contentDescription = "Following",
                            colorFilter = ColorFilter.tint(Color.Black)
                        )

                        Text(
                            modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
                            text = "${user.following}",
                            fontSize = 16.sp
                        )

                        Text(
                            text = "Following",
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                    }

                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Blog",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            textDecoration = TextDecoration.Underline
                        )
                    ) {
                        withLink(LinkAnnotation.Url(url = user.htmlUrl)) {
                            append(user.htmlUrl)
                        }
                    }
                }, modifier = Modifier.fillMaxWidth())
            }
        } ?: run {
            ErrorLayout {
                navController.popBackStack()
            }
        }
    }
}