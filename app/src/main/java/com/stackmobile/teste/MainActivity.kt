package com.stackmobile.teste

import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material.icons.filled.Delete
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

// Data class para representar um pedido
data class Pedido(
    val sabor: String,
    var quantidade: Int,
    var precoTotal: Double
)

// Definindo o valor base das pizzas
val precosSabores = listOf(35.0, 40.0, 45.0, 50.0) // Preços para os 4 sabores

// Carrinho global para armazenar pedidos
val carrinho = mutableStateListOf<Pedido>()

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
    // Adicione mais validações de senha aqui, se necessário
    return true
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val contas = remember { mutableStateListOf<Conta>() }

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
                composable("telaPrincipal") {
                    TelaPrincipal(navController, carrinho) // Passa o carrinho global
                }
                composable("telaDetalhesPizza/{pizzaId}") { backStackEntry ->
                    val pizzaId = backStackEntry.arguments?.getString("pizzaId")?.toInt() ?: 0
                    TelaDetalhesPizza(navController, pizzaId)
                }
                composable("telaCarrinho") {
                    TelaCarrinho(navController, carrinho) // Passa o carrinho global
                }

                composable("telaCarrinho") { TelaCarrinho(navController, carrinho) }
                composable("telaPagamento") { TelaPagamento(navController) }
                composable("telaSucesso") { TelaSucesso(navController) } // Tela de sucesso que você vai implementar


            }
        }
    }
}




// Barra superior com lupa, Cardápio e Carrinho
// Barra superior com ícone de carrinho que mostra a quantidade de pizzas no carrinho
@Composable
fun BarraSuperior(
    onSearchClick: () -> Unit,
    onCartClick: () -> Unit,
    quantidadeNoCarrinho: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp)
            .background(Color.Red),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onSearchClick) {
            Icon(Icons.Filled.Search, contentDescription = "Pesquisar", tint = Color.White)
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .wrapContentWidth(Alignment.CenterHorizontally)
        ) {
            Text(text = "Cardápio", fontSize = 20.sp, color = Color.White)
        }

        // Exibe o número de pizzas no carrinho
        IconButton(onClick = onCartClick) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.ShoppingCart, contentDescription = "Carrinho", tint = Color.White)
                Spacer(modifier = Modifier.width(4.dp))
                if (quantidadeNoCarrinho > 0) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(Color.White, shape = CircleShape)
                            .align(Alignment.CenterVertically)
                    ) {
                        Text(
                            text = quantidadeNoCarrinho.toString(),
                            modifier = Modifier.align(Alignment.Center),
                            color = Color.Red,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}




// Tela Inicial
@Composable
fun TelaInicial(
    onCriarContaClick: () -> Unit,
    onLoginClick: () -> Unit,
    onSairClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // Define o fundo branco para a tela
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
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
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
}


@Preview(showBackground = true)
@Composable
fun PreviewTelaInicial() {
    TelaInicial(
        onCriarContaClick = {},
        onLoginClick = {},
        onSairClick = {}
    )
}

// Tela Criar Conta
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

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
fun PreviewTelaCriarConta() {
    TelaCriarConta(navController = rememberNavController(), contas = mutableStateListOf())
}

// Tela Login
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

@Preview(showBackground = true)
@Composable
fun PreviewTelaLogin() {
    TelaLogin(navController = rememberNavController(), contas = listOf())
}

// Tela Principal com as alterações solicitadas
@Composable
fun TelaPrincipal(navController: NavController, carrinho: MutableList<Pedido>) {

    val backgroundImage = painterResource(id = R.drawable.madeira)

    // Calcula a quantidade total de pizzas no carrinho
    val quantidadeNoCarrinho = carrinho.sumOf { it.quantidade }


    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = backgroundImage,
            contentDescription = "Imagem de fundo",
            modifier = Modifier
                .fillMaxSize()
                .offset(y = 33.dp)
                .graphicsLayer(
                    scaleX = 1.3f,
                    scaleY = 1.1f
                )
                .align(Alignment.Center)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp)
        ) {
            BarraSuperior(
                onSearchClick = {
                    // Lógica de busca
                },
                onCartClick = {
                    navController.navigate("telaCarrinho")
                },
                quantidadeNoCarrinho = quantidadeNoCarrinho // Passa a quantidade
            )


            Spacer(modifier = Modifier.height(16.dp))

            // Exibe a lista de sabores
            val sabores = listOf(
                R.drawable.sabor1,
                R.drawable.sabor2,
                R.drawable.sabor3,
                R.drawable.sabor4
            )

            Column {
                sabores.forEachIndexed { index, sabor ->
                    Image(
                        painter = painterResource(id = sabor),
                        contentDescription = "Sabor de Pizza",
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .clickable {
                                // Navega para a tela de detalhes da pizza
                                navController.navigate("telaDetalhesPizza/$index")
                            }
                    )
                }
            }
        }
    }
}




@Preview(showBackground = true)
@Composable
fun PreviewTelaPrincipal() {
    TelaPrincipal(navController = rememberNavController(), carrinho = mutableListOf())
}



// Tela de detalhes do sabor de pizza com contador de pizzas e atualização de preço
@Composable
fun TelaDetalhesPizza(navController: NavController, pizzaId: Int) {
    val sabores = listOf("Pizza de Calabresa com Cebola", "Pizza de Frango com Catupiry", "Pizza Quatro Queijos", "Pizza Romana")
    val ingredientes = listOf(
        "Pizza de Calabresa com cebolas é feita com massa fresca, molho, muçarela e calabresa, orégano e azeitonas dão o toque final.",
        "Nossa Pizza de Frango Catupiry, feita com massa fininha, molho de tomate caseiro, muçarela, frango feito artesanalmente.",
        "Nossa maravilhosa Pizza 4 Queijos é feita com massa fresca levinha, molho artesanal de tomates frescos, muçarela, parmesão, provolone e Catupiry original.",
        "Sabor levemente picante dos Salame do tipo Italiano, nossa Pizza Romana é a escolha certa. Feita em forno á lenha, com massa fresca fininha, molho caseiro de tomates frescos, muçarela, Salame Italiano de primeira qualidade, Catupiry, azeitonas e orégano."
    )

    var quantidade by remember { mutableStateOf(0) }
    var precoTotal by remember { mutableStateOf(0.0) }

    // Atualiza o preço total com base na quantidade
    LaunchedEffect(quantidade) {
        precoTotal = quantidade * precosSabores[pizzaId]
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Detalhes da ${sabores[pizzaId]}",
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(id = R.drawable.sabor1 + pizzaId), // Imagem correspondente ao sabor
            contentDescription = "Imagem do ${sabores[pizzaId]}",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = ingredientes[pizzaId],
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Exibe o preço
        Text(
            text = "Preço: R$ ${"%.2f".format(precoTotal)}",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Seção "+ x -" para ajustar a quantidade
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = { if (quantidade > 0) quantidade-- }) {
                Text(text = "-")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = quantidade.toString(), fontSize = 18.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { quantidade++ }) {
                Text(text = "+")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botão para adicionar ao carrinho
        Button(onClick = {
            if (quantidade > 0) {
                val pedidoExistente = carrinho.find { it.sabor == sabores[pizzaId] }
                if (pedidoExistente != null) {
                    pedidoExistente.quantidade += quantidade
                    pedidoExistente.precoTotal += precoTotal
                } else {
                    carrinho.add(Pedido(sabores[pizzaId], quantidade, precoTotal))
                }
                navController.popBackStack() // Volta à tela anterior
            }
        }) {
            Text("Adicionar ao Carrinho")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.popBackStack() }) {
            Text("Voltar")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewTelaDetalhesPizza() {
    TelaDetalhesPizza(navController = rememberNavController(), pizzaId = 1) // Passe um valor de exemplo para pizzaId
}

// Carrinho: Mostra todos os itens adicionados
@Composable
fun TelaCarrinho(navController: NavController, carrinho: MutableList<Pedido>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Seu Carrinho",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Exibe cada pedido no carrinho
        carrinho.forEachIndexed { index, pedido ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = pedido.sabor, fontSize = 20.sp)
                    Text(text = "Quantidade: ${pedido.quantidade}", fontSize = 16.sp)
                    Text(text = "Preço: R$ ${"%.2f".format(pedido.precoTotal)}", fontSize = 16.sp)
                }
                IconButton(onClick = {
                    carrinho.removeAt(index)
                }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Remover", tint = Color.Red)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Calcular o total do carrinho
        val total = carrinho.sumOf { it.precoTotal }

        // Exibir o total
        Text(
            text = "Total: R$ ${"%.2f".format(total)}",
            style = MaterialTheme.typography.bodyLarge, // Use bodyLarge ou outro estilo válido
            modifier = Modifier.align(Alignment.End)
        )


        Spacer(modifier = Modifier.height(16.dp))

        // Botão para efetuar pagamento
        Button(onClick = { navController.navigate("telaPagamento") }) {
            Text("Efetuar Pagamento")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.popBackStack() }) {
            Text("Voltar")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewTelaCarrinho() {
    TelaCarrinho(navController = rememberNavController(), carrinho = mutableListOf()) // Passa uma lista vazia como exemplo
}

@Composable
fun TelaPagamento(navController: NavController) {
    var metodoPagamento by remember { mutableStateOf("") }
    var endereco by remember { mutableStateOf("") }
    var emailCpfCelular by remember { mutableStateOf("") }
    var numeroCartao by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }

    val context = LocalContext.current // Obtenha o contexto aqui

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Escolha o Método de Pagamento",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Botões para selecionar o método de pagamento
        Button(onClick = { metodoPagamento = "pix" }) {
            Text("Pix")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { metodoPagamento = "cartao" }) {
            Text("Cartão de Débito/Credito")
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Campos para os detalhes de pagamento
        TextField(
            value = endereco,
            onValueChange = { endereco = it },
            label = { Text("Endereço") }
        )

        if (metodoPagamento == "pix") {
            TextField(
                value = emailCpfCelular,
                onValueChange = { emailCpfCelular = it },
                label = { Text("Email/CPF/Celular") }
            )
        } else if (metodoPagamento == "cartao") {
            TextField(
                value = numeroCartao,
                onValueChange = { numeroCartao = it },
                label = { Text("Número do Cartão") }
            )
            TextField(
                value = cvv,
                onValueChange = { cvv = it },
                label = { Text("CVV") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            // Implementar lógica para validar os dados e processar o pagamento
            if (endereco.isNotEmpty()) {
                // Aqui você pode adicionar validações adicionais se necessário
                navController.navigate("telaSucesso") // Navegar para a tela de sucesso
            } else {
                Toast.makeText(context, "Endereço não pode ser vazio!", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Confirmar Pagamento")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.popBackStack() }) {
            Text("Voltar")
        }
    }
}




@Preview(showBackground = true)
@Composable
fun PreviewTelaPagamento() {
    TelaPagamento(navController = rememberNavController())
}

@Composable
fun TelaSucesso(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Pagamento realizado com sucesso!", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            // Voltar à tela principal
            navController.navigate("telaPrincipal") {
                // Limpa a pilha de navegação para que o usuário não possa voltar à tela de sucesso
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
        }) {
            Text("Voltar ao Início")
        }

    }
}


@Preview(showBackground = true)
@Composable
fun PreviewTelaSucesso() {
    TelaPagamento(navController = rememberNavController())
}






