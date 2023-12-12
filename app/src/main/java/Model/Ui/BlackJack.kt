package Model.Ui

import Model.Data.Card
import Model.Data.Deck
import Model.Data.Player
import Model.Data.PlayingCards
import Model.Data.Routes
import Model.Data.Suits
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.calculator.restructuredblackjack.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MainMenu(navController: NavController,viewModel: ViewModel){
    Box(
        modifier = Modifier.paint(
            painter = painterResource(id = R.drawable.darkgreen_background),
            contentScale = ContentScale.FillWidth
        )){
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 40.dp),horizontalArrangement = Arrangement.End){
                Image(painter = painterResource(id = R.drawable.settings),
                    contentDescription = "logo",
                    modifier = Modifier.width(90.dp))
            }
            Row (
                modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center){
                Image(painter = painterResource(id = R.drawable.blacktitle),
                    contentDescription = "logo",
                    modifier = Modifier.width(700.dp))
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp),
                horizontalArrangement = Arrangement.Center
            ){
                Image(painter = painterResource(id = R.drawable.logo),
                    contentDescription = "logo",
                    modifier = Modifier.width(250.dp))
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 100.dp),
                horizontalArrangement = Arrangement.Center
            ){
                Button(
                    modifier = Modifier
                        .padding(10.dp)
                        .height(60.dp)
                        .width(160.dp),
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(Color.Black),onClick = {
                        navController.navigate(Routes.MultiplayerScreen.route)
                        viewModel.startGame()
                    }) {
                    Text(text = "Multiplayer")
                }
                Button(
                    modifier = Modifier
                        .padding(10.dp)
                        .height(60.dp)
                        .width(160.dp),
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(Color.Black),onClick = { /*TODO*/ }) {
                    Text(text = "SinglePlayer")
                }
            }
        }
    }

}

@Composable
fun MultiplayerScreen(navController: NavController,viewModel: ViewModel) {
    var gameOver by rememberSaveable { mutableStateOf(false) }
    Box(
        modifier = Modifier.paint(
            painter = painterResource(id = R.drawable.darkgreen_background),
            contentScale = ContentScale.FillWidth
        )
    ) {
        if (gameOver){
            GameOverScreen(navController, viewModel.checkWinner(), viewModel)
        }else{
            InGameScreen(
                viewModel,
                gameOver = {
                    gameOver = it
                    Log.d("gameOver",it.toString())
                }
            )
        }
    }


}

@Composable
fun InGameScreen(viewModel: ViewModel,gameOver:(Boolean)->Unit){
    var points by rememberSaveable { mutableIntStateOf(viewModel.currentPLayer.value!!.points) }
    var playerNumber by rememberSaveable { mutableIntStateOf(viewModel.currentPLayer.value!!.playerNumber.toInt()) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxHeight()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
        ) {
            Text(
                text = "Player $playerNumber",
                fontSize = 26.sp,
                modifier = Modifier.padding(5.dp),
                textAlign = TextAlign.Center,
                color = Color(color = viewModel.color())
            )

            Text(
                text = "Points : $points",
                fontSize = 26.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                textAlign = TextAlign.End,
                color = Color.White
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            ImageCreator(Card(PlayingCards.ace, Suits.spades, 0, 0, "backside"), 155, 245, 0, 0)
        }
        Box (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp),
        ){
            DisplayCards(viewModel.playerCards())
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {

            Buttons(
                viewModel,
                playerPoints = { points = it },
                playerNumber = { playerNumber = it },
                gameOver = {gameOver(it)}
            )
        }
    }
}


@Composable
fun Buttons(viewModel: ViewModel,playerPoints:(Int)->Unit,playerNumber:(Int)->Unit,gameOver:(Boolean)->Unit) {
    Row (
        modifier = Modifier
            .padding(top = 80.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){
        Button(
            modifier = Modifier
                .padding(10.dp)
                .height(60.dp)
                .width(160.dp),
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(Color.Black),
            onClick = {
                viewModel.getCard()
                viewModel.changePLayer()
                playerPoints(viewModel.playerInfo()[1])
                playerNumber(viewModel.playerInfo()[0])
//                viewModel.disableButton()
//                viewModel.sleep(300)



            }, enabled = viewModel.enableButton) {
            Text(text = "Hit")
        }

        Button(
            modifier = Modifier
                .padding(10.dp)
                .height(60.dp)
                .width(160.dp),
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(Color.Black),
            onClick = {
                viewModel.changePLayer()
                playerPoints(viewModel.playerInfo()[1])
                playerNumber(viewModel.playerInfo()[0])
            }) {
            Text(text = "Stand")
        }
        gameOver(viewModel.checkGameOver())
    }
}


@Composable
fun GameOverScreen(navController:NavController,gameOverText:String,viewModel: ViewModel){
    Box() {
        Column(modifier = Modifier.fillMaxSize()) {
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ){
                Text(text = gameOverText,
                    fontSize = 26.sp,
                    modifier = Modifier.padding(5.dp),
                    color = Color.White)
            }
            GameOverCards(viewModel.player1.value!!)
            GameOverCards(viewModel.player2.value!!)
            GameOverButtons(navController,viewModel)
        }
    }
}

@Composable
fun GameOverCards(player: Player){
    Row {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Player ${player.playerNumber} cards",
                    fontSize = 15.sp,
                    modifier = Modifier
                        .weight(1f)
                        .padding(5.dp, top = 20.dp),
                    color = Color.White,
                    textAlign = TextAlign.Start
                )

                Text(
                    text = "Points: ${player.points}",
                    fontSize = 15.sp,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 10.dp, top = 20.dp),
                    color = Color.White,
                    textAlign = TextAlign.End
                )
            }
            LazyRow(){
                items(player.cardsInHand) {
                        item -> ImageCreator(card = item, width = 140, height = 180, offsetX = 0, offsetY = 0)
                }
            }

        }
    }
}


@Composable
fun GameOverButtons(navController: NavController,viewModel: ViewModel){
    Row(modifier = Modifier
        .fillMaxSize()
        .padding(top = 40.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Center) {
        Button(modifier = Modifier
            .padding(10.dp)
            .height(60.dp)
            .width(160.dp),
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(Color.Black),onClick = {
                navController.navigate(Routes.MultiplayerScreen.route)
                viewModel.startGame()
            }) {
            Text(text = "Play Again")

        }
        Button(modifier = Modifier
            .padding(10.dp)
            .height(60.dp)
            .width(160.dp),
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(Color.Black),onClick = {
                navController.navigate(Routes.MainMenu.route)

            } ) {
            Text(text = "Exit")
        }
    }
}


@Composable
fun ImageCreator(card: Card, width:Int, height:Int, offsetX:Int, offsetY:Int){
    Image(
        //uses the function getCardId to extract the id of the card
        painter = painterResource(id = getCardId(card)),
        contentDescription = "image",
        modifier = Modifier
            .width(width.dp)
            .height(height.dp)
            .offset(x = offsetX.dp, y = offsetY.dp))
}

@Composable
fun getCardId(card:Card): Int {
    val context = LocalContext.current
    val id = context.resources.getIdentifier(card.idDrawable, "drawable", context.packageName)
    return id
}

@Composable
fun DisplayCards(cards:MutableList<Card>){
    var counter = 5
    for (card in cards ){
        ImageCreator(card, 150 , 250 , counter, 0 )
        counter+=40
    }
}

