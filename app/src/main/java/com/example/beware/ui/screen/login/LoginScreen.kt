package com.example.beware.ui.screen.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.beware.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = viewModel(),
    onLoginSuccess: ()->Unit
){
    var showPassword by rememberSaveable { mutableStateOf(false) }
    var email by rememberSaveable { mutableStateOf("amana@amherst.edu") }
    var password by rememberSaveable { mutableStateOf("123456") }
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier
        .fillMaxSize()
        .background(colorResource(id = R.color.dark_grey))) {
        Box(modifier = Modifier
            .align(Alignment.TopCenter)
            .padding(top = 100.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://firebasestorage.googleapis.com/v0/b/beware-f953e.appspot.com/o/files%2Fimages%2Fbewarelogo.jpg?alt=media&token=d1374742-fcd2-4bc3-8e97-5100d09cd992")
                    .crossfade(true)
                    .build(),
                contentDescription = "Beware Logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(400.dp).padding(50.dp)
            )
        }
        Box(modifier = Modifier
            .padding(top = 100.dp)
        ){
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    label = {
                        Text(text = "E-mail")
                    },
                    value = email,
                    onValueChange = {
                        email = it
                    },
                    singleLine = true,
                    leadingIcon = {
                        Icon(Icons.Default.Email, null)
                    },
                    colors = TextFieldDefaults.colors(
                        unfocusedLabelColor = Color.White,
                        unfocusedIndicatorColor = Color.White,
                        unfocusedTextColor = Color.White,
                        unfocusedContainerColor = Color.Transparent,
                        unfocusedLeadingIconColor = Color.White,
                        unfocusedTrailingIconColor = Color.White,
                        focusedLabelColor = Color.White,
                        focusedLeadingIconColor = Color.White,
                        focusedTextColor = Color.White,
                        focusedContainerColor = Color.DarkGray,
                        focusedTrailingIconColor = Color.White
                    )
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    label = {
                        Text(text = "Password")
                    },
                    value = password,
                    onValueChange = { password = it },
                    singleLine = true,
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    leadingIcon = {
                        Icon(Icons.Default.Password, null)
                    },
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            if (showPassword) {
                                Icon(Icons.Default.Visibility, null)
                            } else {
                                Icon(Icons.Default.VisibilityOff, null)
                            }
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        unfocusedLabelColor = Color.White,
                        unfocusedIndicatorColor = Color.White,
                        unfocusedTextColor = Color.White,
                        unfocusedContainerColor = Color.Transparent,
                        unfocusedLeadingIconColor = Color.White,
                        unfocusedTrailingIconColor = Color.White,
                        focusedLabelColor = Color.White,
                        focusedLeadingIconColor = Color.White,
                        focusedTextColor = Color.White,
                        focusedContainerColor = Color.DarkGray,
                        focusedTrailingIconColor = Color.White
                    )
                )
                Row(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                        OutlinedButton(
                            modifier = Modifier.padding(5.dp),
                            onClick = {
                        // do login...
                            coroutineScope.launch(Dispatchers.IO) {
                                val result = loginViewModel.loginUser(email,password)
                                if (result?.user != null) {
                                    withContext(Dispatchers.Main) {
                                        onLoginSuccess()
                                    }
                                }
                            }
                        },
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.White
                            )
                    ) {
                        Text(text = "Login")
                    }
                    OutlinedButton(
                        modifier = Modifier.padding(5.dp),
                        onClick = {
                        // do registration..
                    coroutineScope.launch {
                        loginViewModel.registerUser(email, password)
                    }
                    },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.White,
                        )
                    ) {
                        Text(text = "Register")
                    }
                }
            }
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(bottom = 50.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (loginViewModel.loginUiState) {
                    is LoginUiState.Loading -> CircularProgressIndicator()
                    is LoginUiState.RegisterSuccess -> Text(text = "Registration OK", color = Color.White)
                    is LoginUiState.Error -> Text(text = "Error: ${
                        (loginViewModel.loginUiState as LoginUiState.Error).error
                    }", color = Color.White)
                    is LoginUiState.LoginSuccess -> Text(text = "Login OK", color = Color.White)
                    LoginUiState.Init -> {}
                }
            }

        }

    }

}