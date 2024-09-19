package com.stackmobile.teste

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// Definição da data class Conta
data class Conta(
    val nome: String,
    val email: String,
    val senha: String
)

// Função para validar os campos de cadastro
fun validarCampos(nome: String, email: String, senha: String, confirmarSenha: String): Boolean {
    if (nome.isBlank() || email.isBlank() || senha.isBlank() || confirmarSenha.isBlank()) {
        return false // Verifica se todos os campos foram preenchidos
    }
    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        return false // Verifica se o e-mail é válido
    }
    if (senha != confirmarSenha) {
        return false // Verifica se as senhas coincidem
    }
    // Adicione mais validações de senha aqui, se necessário (tamanho mínimo, caracteres especiais, etc.)
    return true
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val contas = remember { mutableStateListOf<Conta>() } // Inicializa a lista de contas

            NavHost(navController = navController, startDestination = "telaInicial") {
                composable("telaInicial") {
                    TelaInicial(
                        onCriarContaClick = { navController.navigate("telaCriarConta") },
                        onLoginClick = { navController.navigate("telaLogin") },
                        onSairClick = { finish() }
                    )
                }
                composable("telaCriarConta") { TelaCriarConta(navController, contas) }
                composable("telaLogin") { TelaLogin(navController, contas) }
                composable("telaPrincipal") { TelaPrincipal(navController) }
            }
        }
    }
}

@Composable
fun TelaInicial(
    onCriarContaClick: () -> Unit,
    onLoginClick: () -> Unit,
    onSairClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.pizza), // Substitua pelo nome da sua imagem
            contentDescription = "Logo da Pizzaria",
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onCriarContaClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text("Criar Conta", color = Color.White)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onLoginClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
            Text("Login", color = Color.White)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onSairClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text("Sair", color = Color.White)
        }
    }
}

@Composable
fun TelaCriarConta(navController: NavController, contas: MutableList<Conta>) {
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var confirmarSenha by remember { mutableStateOf("") }
    var mostrarErro by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-mail") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = senha,
            onValueChange = { senha = it },
            label = { Text("Senha") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = confirmarSenha,
            onValueChange = { confirmarSenha = it },
            label = { Text("Confirmar Senha") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (mostrarErro) {
            Text("Erro ao validar os campos. Verifique as informações.", color = Color.Red)
        }

        Button(onClick = {
            if (validarCampos(nome, email, senha, confirmarSenha)) {
                val novaConta = Conta(nome, email, senha)
                contas.add(novaConta) // Adiciona a nova conta à lista
                navController.navigate("telaInicial")
            } else {
                mostrarErro = true
            }
        }) {
            Text("Finalizar Cadastro")
        }
    }
}

@Composable
fun TelaLogin(navController: NavController, contas: List<Conta>) {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var mostrarErro by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-mail") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = senha,
            onValueChange = { senha = it },
            label = { Text("Senha") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (mostrarErro) {
            Text("E-mail ou senha inválidos!", color = Color.Red)
        }
        Button(onClick = {
            val conta = contas.find { it.email == email && it.senha == senha }
            if (conta != null) {
                navController.navigate("telaPrincipal")
            } else {
                mostrarErro = true
            }
        }) {
            Text("Entrar")
        }
    }
}

@Composable
fun TelaPrincipal(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Bem-vindo à tela principal!")
    }
}
