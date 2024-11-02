package com.example.handbook.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.handbook.MainViewModel
import com.example.handbook.R
import com.example.handbook.ui.components.MainTopBar
import com.example.handbook.ui.theme.BgTransparent
import com.example.handbook.ui.theme.MainRed
import com.example.handbook.utils.DrawerEvents
import com.example.handbook.utils.ListItem
import com.example.handbook.ui.components.MainListItem
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    mainViewModel: MainViewModel = hiltViewModel(),
    onClick: (ListItem) -> Unit
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    val topBarTitle = remember {
        mutableStateOf("Грибы")
    }

//    val mainList = remember {
//        mutableStateOf(getListItemByIndex(0, context))
//    }

    val mainList = mainViewModel.mainList


    LaunchedEffect(Unit) {
        mainViewModel.getAllItemsByCategory(topBarTitle.value)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerHeader()
                Spacer(modifier = Modifier.height(5.dp))
                DrawerBody() { event ->
                    when (event) {
                        is DrawerEvents.OnItemClick -> {
                            topBarTitle.value = event.title
//                            mainList.value = getListItemByIndex(event.index, context)
                            mainViewModel.getAllItemsByCategory(event.title)
                        }
                    }

                    coroutineScope.launch {
                        drawerState.close()
                    }
                }
            }
        },
        content = {
            Scaffold(
                //snackbarHost = { SnackbarHost(snackbarHostState) },
                topBar = {
                    MainTopBar(
                        title = topBarTitle.value,
                        drawerState
                    ) {
                        topBarTitle.value = "Избранные"
                        mainViewModel.getFavorites()
                    }
                }
            ) { innerPadding ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    items(mainList.value) { item ->
                        MainListItem(item = item) { listItem ->
                            onClick(listItem)
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun DrawerHeader() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
            .padding(5.dp),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, MainRed),
        onClick = { /*TODO*/ }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = R.drawable.header_bg),
                contentDescription = "Header image",
                contentScale = ContentScale.Crop
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MainRed)
                    .padding(10.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                text = "-Справочник ботаника-"
            )
        }
    }
}

@Composable
fun DrawerBody(onEvent: (DrawerEvents) -> Unit) {
    val list = stringArrayResource(id = R.array.drawer_list)

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        itemsIndexed(list) { index, title ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(3.dp),
                colors = CardDefaults.cardColors(BgTransparent)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .wrapContentWidth()
                        .clickable {
                            onEvent(DrawerEvents.OnItemClick(title, index))
                        },
                    fontWeight = FontWeight.Bold,
                    text = title
                )
            }
        }

    }
}

//private fun getListItemByIndex(index: Int, context: Context): List<ListItem> {
//    val list = ArrayList<ListItem>()
//
//    val arrayList = context.resources.getStringArray(IdArrayList.listId[index])
//
//    arrayList.forEach { item ->
//        val itemArray = item.split("|")
//
//        list.add(
//            ListItem(
//                itemArray[0],
//                itemArray[1],
//                itemArray[2]
//            )
//        )
//    }
//
//    return list
//}