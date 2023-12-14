package Model.Ui

import Model.Data.Card
import Model.Data.Deck
import Model.Data.Player
import android.app.Application
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


class ViewModel (application: Application) : AndroidViewModel(application) {


    private val _player1 = MutableLiveData<Player>()
    var player1 : LiveData<Player> = _player1
    private val _player2 = MutableLiveData<Player>()
    var player2 : LiveData<Player> = _player2
    var currentPLayer = MutableLiveData<Player>()
    private var switchPlayer : Boolean = true
    private var standCounter : Int = 0
    private var _displayCard = MutableLiveData<Boolean>()
    var displayCard : LiveData<Boolean> = _displayCard
    private var _gameOver = MutableLiveData<Boolean>()
    var gameOver : LiveData<Boolean> = _gameOver
    private var _ai = MutableLiveData<Boolean>()
    var ai : LiveData<Boolean> = _ai



    private fun createPlayers(){
        _player1.value = Player(1)
        _player2.value = Player(2)
        startingHand(_player1.value!!)
        startingHand(_player2.value!!)
        switch()
    }

    private fun startingHand(player: Player){
        player.startingHand()
    }


    private fun newDeck(){
        Deck.resetDeck()
        Deck.createDeck()
    }

    fun startGame(ai:Boolean){
        switchPlayer = true
        _displayCard.value = false
        _ai.value = ai
        standCounter = 0
        _gameOver.value = false
        newDeck()
        createPlayers()
    }

    fun playerHasStood(){
        while (standCounter<2){
            aiTurn()
        }
    }


    fun switch(){
        if (switchPlayer){
            currentPLayer = _player1
        }else{
            currentPLayer = _player2
        }
    }

    fun changePLayer(){
        switchPlayer =! switchPlayer
        _displayCard.value = false
        switch()
    }

    fun aiTurn(): String {
        var text = ""
        if (_ai.value!!){
            changePLayer()
            if (currentPLayer.value!!.checkPoints() <=17){
                currentPLayer.value!!.hit()
                text = "Ai has hit !!"
            }else{
                stand()
                text = "Ai has stood !!"
            }
            currentPLayer.value!!.checkPoints()
            if (checkGameOver()){
                _gameOver.value = true
            }
            changePLayer()
        }
        return text
    }
    fun playerInfo(): MutableList<Int> {
        return mutableListOf(currentPLayer.value!!.playerNumber.toInt(),currentPLayer.value!!.points)
    }

    fun playerCards(): MutableList<Card> {
        return currentPLayer.value!!.cardsInHand
    }

    fun getCard(){
        _displayCard.value = true
        currentPLayer.value!!.hit()
        currentPLayer.value!!.checkPoints()

    }

    fun lastCard(): Card {
        return currentPLayer.value!!.cardsInHand.last()
    }


    fun color(): Long {
        val color_: Long
        if (switchPlayer){
            color_ = 0xFF0030DB
        }else{
            color_ = 0xFFDB0007
        }
        return color_
    }

    fun checkGameOver(): Boolean {
        return currentPLayer.value!!.winOrLoose() == 1 || currentPLayer.value!!.winOrLoose() == -1
    }

    fun checkWinner(): String {
        var winner = 0
        var draw = false
        if (_player2.value!!.winOrLoose() ==1 && _player1.value!!.winOrLoose() == 0 || _player1.value!!.winOrLoose() ==-1 && _player2.value!!.winOrLoose() == 0){
            winner = 2
        }else if (_player2.value!!.winOrLoose() ==-1 && _player1.value!!.winOrLoose() == 0 || _player1.value!!.winOrLoose() ==1 && _player2.value!!.winOrLoose() == 0){
            winner = 1
        }else if (_player2.value!!.winOrLoose() ==-1 && _player1.value!!.winOrLoose() ==-1){
            draw = true
        }else if (_player2.value!!.winOrLoose() ==1 && _player1.value!!.winOrLoose() ==1){
            draw = true
        }
        else{
            if (_player1.value!!.closesToWinning() < _player2.value!!.closesToWinning()){
                winner =1
            }else{
                winner = 2
            }
        }
        if (draw ){
            return "Draw !!"
        }else{
            return "Player $winner is the winner !!"
        }
    }

    fun gameOver(gameOver :Boolean){
        _gameOver.value = gameOver
    }



    fun stand(){
        standCounter += 1
        if (standCounter == 2){
            _gameOver.value = true
        }
    }


}