package com.splitwise.easy.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AddBill : Screen("add_bill")
    object Friends : Screen("friends")
    object Groups : Screen("groups")
    object History : Screen("history")
    object Settings : Screen("settings")
    object BillDetails : Screen("bill_details/{billId}") {
        fun createRoute(billId: String) = "bill_details/$billId"
    }
    object SplitCalculator : Screen("split_calculator")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SplittyEasyApp() {
    val navController = rememberNavController()
    SplittyEasyTheme {
        NavHost(navController = navController, startDestination = Screen.Home.route) {
            composable(Screen.Home.route) { HomeScreen(navController) }
            composable(Screen.AddBill.route) { AddBillScreen(navController) }
            composable(Screen.Friends.route) { FriendsScreen(navController) }
            composable(Screen.Groups.route) { GroupsScreen(navController) }
            composable(Screen.History.route) { HistoryScreen(navController) }
            composable(Screen.Settings.route) { SettingsScreen(navController) }
            composable(Screen.BillDetails.route) { backStackEntry ->
                val billId = backStackEntry.arguments?.getString("billId") ?: ""
                BillDetailsScreen(navController, billId)
            }
            composable(Screen.SplitCalculator.route) { SplitCalculatorScreen(navController) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupsScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Groups") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text("Groups Screen - Coming Soon!")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    // Observe transactions
    val transactions by com.splitwise.easy.data.TransactionRepository.transactions.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("SplittyEasy", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Home") },
                    selected = true,
                    onClick = { }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text("Friends") },
                    selected = false,
                    onClick = { navController.navigate(Screen.Friends.route) }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = null) },
                    label = { Text("History") },
                    selected = false,
                    onClick = { navController.navigate(Screen.History.route) }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.AddBill.route) },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Bill")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Balance Summary Cards
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    BalanceCard(
                        title = "You're owed",
                        amount = "$0.00",
                        color = Color(0xFF4CAF50),
                        modifier = Modifier.weight(1f)
                    )
                    BalanceCard(
                        title = "You owe",
                        amount = "$0.00",
                        color = Color(0xFFF44336),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                NetBalanceCard(amount = "$0.00", isPositive = true)
            }

            // Quick Actions
            item {
                Text(
                    "Quick Actions",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(listOf("Split Bill", "Add Friend", "Settle Up", "Groups")) { action ->
                        QuickActionCard(action) {
                            when (action) {
                                "Split Bill" -> navController.navigate(Screen.SplitCalculator.route)
                                "Add Friend" -> navController.navigate(Screen.Friends.route)
                                "Groups" -> navController.navigate(Screen.Groups.route)
                            }
                        }
                    }
                }
            }

            // Recent Transactions
            item {
                Text(
                    "Recent Transactions",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            if (transactions.isEmpty()) {
                item {
                    Card {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "No recent transactions yet",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Add a bill to see it here",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(transactions) { tx ->
                    RecentTransactionItem(
                        title = tx.title,
                        amount = formatCents(tx.amountCents),
                        participants = "${tx.participants} people",
                        date = tx.dateText,
                        isPaid = tx.isPaid
                    ) { navController.navigate(Screen.BillDetails.createRoute(tx.id)) }
                }
            }
        }
    }
}

private fun formatCents(amountCents: Long): String {
    val sign = if (amountCents < 0) "-" else ""
    val abs = kotlin.math.abs(amountCents)
    val dollars = abs / 100
    val cents = abs % 100
    return sign + "$" + "%d.%02d".format(dollars, cents)
}

@Composable
fun BalanceCard(
    title: String,
    amount: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = amount,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

@Composable
fun NetBalanceCard(amount: String, isPositive: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isPositive) Color(0xFFE8F5E8) else Color(0xFFFFEBEE)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Net Balance",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (isPositive) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = if (isPositive) Color(0xFF4CAF50) else Color(0xFFF44336)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = amount,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (isPositive) Color(0xFF4CAF50) else Color(0xFFF44336)
                )
            }
        }
    }
}

@Composable
fun QuickActionCard(action: String, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.width(100.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = when (action) {
                    "Split Bill" -> Icons.Default.Add
                    "Add Friend" -> Icons.Default.Add
                    "Settle Up" -> Icons.Default.Star
                    "Groups" -> Icons.Default.Person
                    else -> Icons.Default.Add
                },
                contentDescription = action,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = action,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun RecentTransactionItem(
    title: String,
    amount: String,
    participants: String,
    date: String,
    isPaid: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "$participants • $date",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = amount,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (isPaid) "Paid" else "Pending",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isPaid) Color(0xFF4CAF50) else Color(0xFFFFC107)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBillScreen(navController: NavHostController) {
    var currentStep by remember { mutableStateOf(0) }
    val steps = listOf("Bill Details", "Select Friends", "Split Method", "Enter Spent", "Final Amounts")

    // Hoisted bill state
    var billTitle by remember { mutableStateOf("") }
    var billAmountText by remember { mutableStateOf("") }
    var billCategory by remember { mutableStateOf("Food") }
    var billDescription by remember { mutableStateOf("") }

    // Selected participants
    var selectedFriends by remember { mutableStateOf(setOf<String>()) }

    // Split method
    var selectedMethod by remember { mutableStateOf("Equal") }

    // Spent amounts per participant (string inputs)
    var spentInputs by remember { mutableStateOf<Map<String, String>>(emptyMap()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Bill - ${steps[currentStep]}") },
                navigationIcon = {
                    IconButton(onClick = {
                        if (currentStep > 0) currentStep-- else navController.popBackStack()
                    }) {
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
                .padding(16.dp)
        ) {
            // Progress indicator
            LinearProgressIndicator(
                progress = { (currentStep + 1) / steps.size.toFloat() },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            when (currentStep) {
                0 -> BillDetailsStep(
                    title = billTitle,
                    amountText = billAmountText,
                    category = billCategory,
                    description = billDescription,
                    onTitleChange = { billTitle = it },
                    onAmountChange = { billAmountText = it },
                    onCategoryChange = { billCategory = it },
                    onDescriptionChange = { billDescription = it },
                    onNext = { if (billTitle.isNotEmpty() && billAmountText.isNotEmpty()) currentStep++ }
                )
                1 -> SelectFriendsStep(
                    selectedFriends = selectedFriends,
                    onToggle = { friend ->
                        selectedFriends = if (friend in selectedFriends) selectedFriends - friend else selectedFriends + friend
                        spentInputs = spentInputs.filterKeys { it in selectedFriends }
                    },
                    onNext = { if (selectedFriends.isNotEmpty()) currentStep++ }
                )
                2 -> SplitMethodStep(
                    selectedMethod = selectedMethod,
                    onSelected = { selectedMethod = it },
                    onNext = { currentStep++ }
                )
                3 -> EnterSpentStep(
                    participants = selectedFriends.toList(),
                    inputs = spentInputs,
                    onChange = { name, value -> spentInputs = spentInputs.toMutableMap().apply { put(name, value) } },
                    onNext = { currentStep++ }
                )
                4 -> FinalAmountsStep(
                    participants = selectedFriends.toList(),
                    totalAmountText = billAmountText,
                    spentInputs = spentInputs,
                    splitMethod = selectedMethod,
                    onDone = { navController.popBackStack() }
                )
            }
        }
    }
}

@Composable
fun BillDetailsStep(
    title: String,
    amountText: String,
    category: String,
    description: String,
    onTitleChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onNext: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            label = { Text("Bill Title") },
            placeholder = { Text("Dinner at restaurant") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = amountText,
            onValueChange = onAmountChange,
            label = { Text("Total Amount") },
            placeholder = { Text("0.00") },
            leadingIcon = { Text("$") },
            modifier = Modifier.fillMaxWidth()
        )

        var expanded by remember { mutableStateOf(false) }
        Box {
            OutlinedTextField(
                value = category,
                onValueChange = { },
                readOnly = true,
                label = { Text("Category") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                trailingIcon = {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Dropdown"
                    )
                }
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                listOf("Food", "Travel", "Entertainment", "Utilities", "Other").forEach { cat ->
                    DropdownMenuItem(
                        text = { Text(cat) },
                        onClick = {
                            onCategoryChange(cat)
                            expanded = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text("Description (Optional)") },
            maxLines = 3,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onNext,
            enabled = title.isNotEmpty() && amountText.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Next: Select Friends")
        }
    }
}

@Composable
fun SelectFriendsStep(
    selectedFriends: Set<String>,
    onToggle: (String) -> Unit,
    onNext: () -> Unit
) {
    val friends = remember {
        listOf(
            "Alice Johnson", "Bob Smith", "Charlie Brown",
            "Diana Prince", "Eve Wilson", "Frank Miller"
        )
    }

    Column {
        Text(
            "Select friends to split with",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(friends) { friend ->
                Card(
                    onClick = { onToggle(friend) },
                    colors = CardDefaults.cardColors(
                        containerColor = if (friend in selectedFriends)
                            MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    MaterialTheme.colorScheme.primary,
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                friend.first().toString(),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            friend,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1f)
                        )

                        if (friend in selectedFriends) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = "Selected",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onNext,
            enabled = selectedFriends.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Next: Choose Split Method")
        }
    }
}

@Composable
fun SplitMethodStep(
    selectedMethod: String,
    onSelected: (String) -> Unit,
    onNext: () -> Unit
) {
    Column {
        Text(
            "How do you want to split?",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            listOf(
                "Equal" to "Split equally among all participants",
                "Percentage" to "Split by custom percentages",
                "Custom" to "Enter exact amounts for each person",
                "Item-wise" to "Split by individual items"
            ).forEach { (method, description) ->
                Card(
                    onClick = { onSelected(method) },
                    colors = CardDefaults.cardColors(
                        containerColor = if (method == selectedMethod)
                            MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = method == selectedMethod,
                            onClick = { onSelected(method) }
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                method,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onNext,
            modifier = Modifier.fillMaxWidth()
        ) { Text("Next: Enter Spent") }
    }
}

@Composable
fun EnterSpentStep(
    participants: List<String>,
    inputs: Map<String, String>,
    onChange: (String, String) -> Unit,
    onNext: () -> Unit
) {
    Column {
        Text("Who already paid?", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(participants) { name ->
                OutlinedTextField(
                    value = inputs[name] ?: "",
                    onValueChange = { onChange(name, it.filter { ch -> ch.isDigit() || ch == '.' }) },
                    label = { Text("$name spent") },
                    leadingIcon = { Text("$") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = onNext, modifier = Modifier.fillMaxWidth()) { Text("Next: Final Amounts") }
    }
}

@Composable
fun FinalAmountsStep(
    participants: List<String>,
    totalAmountText: String,
    spentInputs: Map<String, String>,
    splitMethod: String,
    onDone: () -> Unit
) {
    val total = totalAmountText.toBigDecimalOrNull() ?: java.math.BigDecimal.ZERO
    val n = participants.size.coerceAtLeast(1)
    val share = if (n > 0) total.divide(java.math.BigDecimal(n), 2, java.math.RoundingMode.HALF_UP) else java.math.BigDecimal.ZERO

    val rows = participants.map { name ->
        val spent = spentInputs[name]?.toBigDecimalOrNull() ?: java.math.BigDecimal.ZERO
        val net = spent.subtract(share)
        Triple(name, spent, net)
    }

    Column {
        Text("Final amounts", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Total: $${total.setScale(2, java.math.RoundingMode.HALF_UP)}  •  Per person: $${share}")
        Spacer(modifier = Modifier.height(12.dp))
        Card {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                rows.forEach { (name, spent, net) ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(name)
                        val status = if (net.signum() >= 0) "to receive" else "to pay"
                        val amt = net.abs().setScale(2, java.math.RoundingMode.HALF_UP).toPlainString()
                        Text("Spent $${spent.setScale(2, java.math.RoundingMode.HALF_UP)} • $status $${amt}", fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = onDone, modifier = Modifier.fillMaxWidth()) { Text("Done") }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendsScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Friends & Groups") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text("Friends screen placeholder")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Transaction History") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text("History screen placeholder")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text("Settings screen placeholder")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillDetailsScreen(navController: NavHostController, billId: String) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bill Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Bill ID: $billId", fontWeight = FontWeight.Bold)
            Text("Details placeholder")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SplitCalculatorScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Split Calculator") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        // Simple equal-split calculator UI
        var totalText by remember { mutableStateOf("") }
        var peopleText by remember { mutableStateOf("2") }

        val total = totalText.toBigDecimalOrNull() ?: java.math.BigDecimal.ZERO
        val people = peopleText.toIntOrNull()?.coerceAtLeast(1) ?: 1
        val perPerson = if (people > 0) total.divide(java.math.BigDecimal(people), 2, java.math.RoundingMode.HALF_UP) else java.math.BigDecimal.ZERO

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Equal split", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

            OutlinedTextField(
                value = totalText,
                onValueChange = { input ->
                    totalText = input.filter { ch -> ch.isDigit() || ch == '.' }
                },
                label = { Text("Total Amount") },
                leadingIcon = { Text("$") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = peopleText,
                onValueChange = { input ->
                    peopleText = input.filter { ch -> ch.isDigit() }
                },
                label = { Text("Number of people") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Total: $${total.setScale(2, java.math.RoundingMode.HALF_UP)}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "People: $people",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    HorizontalDivider()
                    Text(
                        text = "Each pays: $${perPerson.setScale(2, java.math.RoundingMode.HALF_UP)}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = {
                        totalText = ""
                        peopleText = "2"
                    },
                    modifier = Modifier.weight(1f)
                ) { Text("Reset") }

                Button(
                    onClick = { navController.popBackStack() },
                    enabled = totalText.isNotEmpty(),
                    modifier = Modifier.weight(1f)
                ) { Text("Done") }
            }
        }
    }
}
