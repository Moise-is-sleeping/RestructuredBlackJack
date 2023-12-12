package Model.Ui

import Model.Data.Card
import Model.Data.Deck
import Model.Data.Player
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ViewModel (application: Application) : AndroidViewModel(application) {


    private val _player1 = MutableLiveData<Player>()
    var player1 : LiveData<Player> = _player1
    private val _player2 = MutableLiveData<Player>()
    var player2 : LiveData<Player> = _player2
    var currentPLayer = MutableLiveData<Player>()
    private var switchPlayer : Boolean = false
    private var standCounter : Int = 0
    var enableButton = true



    fun disableButton(){
        enableButton=false

    }
    fun enableButton(){
        enableButton=true

    }
    private fun createPlayers(){
        _player1.value = Player(1)
        _player2.value = Player(2)
        startingHand(_player1.value!!)
        startingHand(_player2.value!!)
    }

    private fun startingHand(player: Player){
        player.startingHand()
    }


    private fun newDeck(){
        Deck.resetDeck()
        Deck.createDeck()
    }

    fun startGame(){
        newDeck()
        createPlayers()
        changePLayer()

    }

    fun sleep(time:Long){
        Thread.sleep(time)
    }

    fun changePLayer(){
//        enableButton()
        switchPlayer =! switchPlayer
        if (switchPlayer){
            currentPLayer = _player1
        }else{
            currentPLayer = _player2
        }
    }
    fun playerInfo(): MutableList<Int> {
        return mutableListOf(currentPLayer.value!!.playerNumber.toInt(),currentPLayer.value!!.points)
    }

    fun playerCards(): MutableList<Card> {
        return currentPLayer.value!!.cardsInHand
    }

    fun getCard(){
        currentPLayer.value!!.hit()
        currentPLayer.value!!.checkPoints()

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
        if (_player2.value!!.winOrLoose() ==1 || _player1.value!!.winOrLoose() ==-1){
            winner = 2
        }else if (_player2.value!!.winOrLoose() ==-1 || _player1.value!!.winOrLoose() ==1){
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



    fun stand(){
        standCounter += 1
        if (standCounter == 2){

        }
    }


}