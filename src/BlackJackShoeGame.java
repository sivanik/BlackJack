//import sun.org.mozilla.javascript.ast.IfStatement;

import java.io.BufferedReader;
//import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
//import java.lang.reflect.Array;
import java.util.ArrayList;
//import java.util.Collection;
import java.util.Collections;

/**
 * Created by sivani on 4/5/14.
 */
public class BlackJackShoeGame {


    private ArrayList totalCards;
    private int playerTotal;
    private int dealerTotal;
    private boolean playerBusted;
    private boolean playerHasAce = false;
    private int numOfAce = 0;

    private ArrayList cardsToBeDealt;
    private ArrayList playerCards;
    private ArrayList dealerCards;
    private int playerBet;
    private double playerChips = 100;



    public static void main(String[] args) throws IOException {

        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("***************Casino Rules for BlackJack Table************");
        System.out.println("         BLACKJACK pays 3 to 2");
        System.out.println("         Dealer must stand on all 17");
        System.out.println("         Insurance pays 2 to 1");
        System.out.println("         If the Player bets zero chips it implies they are opting out of the game and the game ends!!!");
        System.out.println("******************************************************");

        BlackJackShoeGame blackJackShoeGame = new BlackJackShoeGame();


        int totalNumOfDecks = 1;



        //System.out.println(cardsInHand);

        while(blackJackShoeGame.playerChips>0) {

            ArrayList cardsInHand = blackJackShoeGame.populateCards(totalNumOfDecks);
            System.out.println("Decks in hand!! ");
            System.out.println("-----------------------------------");
            System.out.println("Shuffling the cards.............");
            System.out.println("-----------------------------------");

            ArrayList cardsShuffled;
            cardsShuffled = blackJackShoeGame.shuffleCards(cardsInHand);
            //System.out.println(cardsShuffled);


            do { //Repeat until player places a valid bet (positive integer in multiples of 2)
                System.out.println("Available chips for the player : " + blackJackShoeGame.playerChips);
                System.out.println("Please place your bets (multiples of 2 only):");
                blackJackShoeGame.playerBet = Integer.parseInt(bufferRead.readLine());

                if (blackJackShoeGame.playerBet % 2 == 0 && blackJackShoeGame.playerBet > 0){
                    break;
                }
                else if (blackJackShoeGame.playerBet == 0){
                    System.out.println("Game ends!!");
                    System.exit(0);
                }
                else{
                    System.out.println("Enter again");
                }
            } while(blackJackShoeGame.playerBet % 2 != 0 || blackJackShoeGame.playerBet < 0);

            blackJackShoeGame.placeBetsInCircle(blackJackShoeGame.playerBet);

            System.out.println("-----------------------------------");
            System.out.println("Dealing the cards !!!!!!!");
            System.out.println("-----------------------------------");


            blackJackShoeGame.dealCards(cardsShuffled);
            System.out.println("Players cards : " + blackJackShoeGame.playerCards.get(0).toString() + " " + blackJackShoeGame.playerCards.get(1).toString());
            System.out.println("-----------------------------------");
            System.out.println("Dealers cards : " + blackJackShoeGame.dealerCards.get(0).toString() + " " + " ** ");
            System.out.println("-----------------------------------");
            int playerOption;

            //blackJackShoeGame.playerOptions();


            do {
                System.out.println("Players Options: 1.Stand  2.Hit [Enter the corresponding number]:");
                playerOption = Integer.parseInt(bufferRead.readLine());

                switch (playerOption) {
                    case 1:
                        System.out.println("Player Stands");
                        blackJackShoeGame.standCards();
                        blackJackShoeGame.dealersTurn();
                        break;
                    case 2:
                        System.out.println("Player Hits");
                        blackJackShoeGame.hitCards();
                        if (!blackJackShoeGame.playerBusted) {
                            playerOption = -1;
                            blackJackShoeGame.playerBusted = false;
                        }// blackJackShoeGame.dealersTurn();
                        break;

                    default:
                        System.out.println("Enter either 1 or 2. ");
                        break;
                }

            } while (playerOption != 1 && playerOption != 2);

           if(blackJackShoeGame.playerChips == 0){
               System.out.println("Player has run out of chips");
               break;
           }

        }


    }

    public ArrayList populateCards(int totalNumOfDecks) {

        int numOfCards = 0;
        totalCards = new ArrayList();
        for (int decks = 0; decks < totalNumOfDecks; decks++) {

            for (int suite = 0; suite < 4; suite++) {

                for (int cardValue = 1; cardValue <= 13; cardValue++) {

                    char[] cardType = {'H', 'C', 'D', 'S'};
                    totalCards.add(numOfCards, new Card(cardValue,cardType[suite]));
                    numOfCards++;
                }

            }

        }
        return totalCards;


    }

    public ArrayList shuffleCards(ArrayList cardsRandom) {

        Collections.shuffle(cardsRandom);

        return cardsRandom;
    }

    public void dealCards(ArrayList cardsInBox) {

        cardsInBox.remove(0);//burning the beginning card.can be placed in main?

        dealerCards = new ArrayList();
        playerCards = new ArrayList();

        playerCards.add(0, cardsInBox.remove(0));
        dealerCards.add(0, cardsInBox.remove(0));
        playerCards.add(1, cardsInBox.remove(0));
        dealerCards.add(1, cardsInBox.remove(0));

        cardsToBeDealt = cardsInBox;

        return;
    }

    public void standCards(){
        if(this.playerTotal == 0) {
            for (Object cardObj : playerCards) {
                Card playerCard = (Card) cardObj;
                int tempValue = playerCard.cardValuation();
                if(tempValue == 1){
                    tempValue =11;
                    numOfAce++;
                }
                this.playerTotal = this.playerTotal + tempValue;
                if(numOfAce>0){
                    numOfAce--;
                    if(playerTotal>21){
                        playerTotal = playerTotal - 10;
                    }
                }
            }
        }
        dealersTurn();
    }

    public void dealersTurn(){

        dealerTotal = 0;
        boolean cardIsAce = false;
        boolean dealAgain = true;
        int numOfAces = 0;

        do {

            for(Object cardObj: dealerCards){
                Card dealerCard = (Card) cardObj;
                int tempValue = dealerCard.cardValuation();
                //System.out.println(" "+ tempValue);
                if (tempValue == 1) {
                    cardIsAce = true;
                    numOfAces++;
                    tempValue = 11;
                }


                dealerTotal = dealerTotal + tempValue;

                if(numOfAces > 0){
                    numOfAces--;
                    if(dealerTotal>=17 && dealerTotal<=21 ){
                        dealAgain=false;
                        cardIsAce = false;
                        break;
                    }
                    else if (dealerTotal > 21) {
                        cardIsAce = false;
                        dealerTotal = dealerTotal - 10;
                        if (dealerTotal >= 17 && dealerTotal <= 21) {
                            dealAgain = false;
                            break;
                        }
                    }

                }else{
                    if(((dealerTotal >= 17 && dealerTotal <= 21) || dealerTotal>21)){
                        dealAgain = false;
                        break;
                    }

                }
               /* dealerTotal = dealerTotal + tempValue;
                //System.out.println(dealerTotal + " " + cardIsAce);
                if(dealerTotal>=17 && dealerTotal<=21 && cardIsAce){
                    dealAgain=false;
                    cardIsAce = false;
                    break;
                }
                else if (dealerTotal > 21 && cardIsAce) {
                    cardIsAce = false;
                    dealerTotal = dealerTotal - 10;
                    if (dealerTotal >= 17 && dealerTotal <= 21) {
                        dealAgain = false;
                        break;
                    }
                } else if(((dealerTotal >= 17 && dealerTotal <= 21)
                        || dealerTotal>21)
                        && !cardIsAce)            {
                    dealAgain = false;
                    break;
                }*/
            }

            if(dealAgain) {
                dealerCards.add(cardsToBeDealt.remove(0));
                dealerTotal = 0;
            }

        } while(dealAgain);

        System.out.println("Dealers cards!!");
        System.out.println("-----------------------------");
        for (Object cardObj : dealerCards){
            System.out.print(" "+cardObj.toString());
        }
        System.out.println("");System.out.println("--------------------------");

        endResult();
    }

    private void endResult(){if(playerTotal == 21 && dealerTotal == 21){
        if((playerCards.size()==2 && dealerCards.size()==2) || (playerCards.size()>2 && dealerCards.size()>2)){
            System.out.println("Push!!");
        }
        else if(playerCards.size()==2 && dealerCards.size() >2){
            System.out.println("BlackJack for Player!! Player wins!");
            playerBet = (playerBet*3)/2;
        }
        else if(playerCards.size()>2 && dealerCards.size()==2){
            System.out.println("BlackJack for Dealer!! Player looses");
            playerBet=0;
        }
    }
    else if(playerTotal<21 && dealerTotal<21 && playerTotal>dealerTotal){
        System.out.println("Player wins!!");
        playerBet = playerBet*2;
    }
    else if(playerTotal<21 && dealerTotal<21 && playerTotal<dealerTotal){
        System.out.println("Dealer wins!! Player loses!");
        playerBet = 0;
    }
    else if(playerTotal<21 && dealerTotal<21 && dealerTotal == playerTotal) {
        System.out.println("PUSH!!");
    }
    else if(playerTotal == 21 && dealerTotal != 21 ){
        if(playerCards.size()==2){
            System.out.println("BlackJack for Player!! Player wins!");
            playerBet = (playerBet*3)/2;
        }
        else {
            System.out.println("Player wins!!");
            playerBet = playerBet*2;
        }
    }
    else if(playerTotal !=21 && dealerTotal == 21){
        if(dealerCards.size()==2){
            System.out.println("BlackJack for Dealer!! Player loses");
        }
        else{
            System.out.println("Dealer wins!! Player loses!");
        }
        playerBet = 0;
    }
    else if(playerTotal>21 && dealerTotal>21){
        System.out.println(playerTotal +" " + dealerTotal);
        System.out.println("Both Dealer and Player go bust");
        playerBet = 0;//doubtful
    }
    else if(playerTotal>21 && dealerTotal<21){
        System.out.println("Dealer Wins!!");
        playerBet = 0;
    }else if(playerTotal<21 && dealerTotal>21) {
        System.out.println("Player Wins!!");
        playerBet = playerBet*2;

    }
        playerChips = playerChips + playerBet;
        playerTotal = 0;
        dealerTotal = 0;

    }

    public void hitCards() {
        playerCards.add(cardsToBeDealt.remove(0));

        System.out.println("Players cards");
        System.out.println("-------------------------------------: ");

        for (Object cardObj : playerCards){
            System.out.print(" "+cardObj.toString());
        }
        System.out.println("");System.out.println("--------------------------------------");

        playerBusted = checkPlayerBusted();
        if(playerBusted){
            System.out.println("Player busted!! Dealer's turn!");
            dealersTurn();
            //playerBusted = false;
        }
    }

    private boolean checkPlayerBusted(){

        boolean playerOver21 = false;
        playerTotal=0;
        boolean cardIsAce = false;
        numOfAce= 0;

        for (Object cardObj : playerCards){
            Card playerCard = (Card) cardObj;
            int tempValue = playerCard.cardValuation();
            //System.out.println(tempValue);
            if(tempValue == 1){
                tempValue = 11;
                cardIsAce = true;
                numOfAce++;
            }
            playerTotal = playerTotal + tempValue;
            System.out.println(playerTotal + " " +cardIsAce + numOfAce);
            if(playerTotal>21 && cardIsAce){
                if(numOfAce>1) {
                    playerTotal = playerTotal - (10 * numOfAce);
                    numOfAce=0;
                }else {
                    playerTotal = playerTotal - 10;
                }
                cardIsAce=false;
                if(playerTotal>21){
                    playerOver21 = true;
                }
            }
            if(playerTotal>21 && !cardIsAce){
                playerOver21 = true;
            }

            System.out.println(playerTotal + " " +cardIsAce);
            //isAce = false;
        }
        //System.out.println("" + playerTotal);
        return playerOver21;
    }

    public void placeBetsInCircle(int playerBet){

        playerChips = playerChips - playerBet;

    }
}
