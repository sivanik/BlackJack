import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by sivani on 4/5/14.
 */
public class BlackJackShoeGameAdvanced {


    private ArrayList totalCards;
    private int playerTotal;
    private int dealerTotal;
    private boolean playerBusted;
    private int numOfAce = 0;

    private ArrayList cardsToBeDealt;
    public ArrayList playerCards;
    public ArrayList dealerCards;
    public int playerBet;
    private double playerChips = 100;
    //private boolean doubleDown;


    public static void main(String[] args){

        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("***************Casino Rules for BlackJack Table***********************************************************");
        System.out.println("         BLACKJACK pays 3 to 2");
        System.out.println("         Dealer must stand on all 17");
        System.out.println("         If the Player bets zero chips it implies they are opting out of the game and the game ends!!!");
        System.out.println("         If the Player enters invalid input(space/enter/non numeric characters) twice in a row, the game exits!!!");
        System.out.println("         Player can only DOUBLE and SURRENDER when they have two cards.");
        System.out.println("**********************************************************************************************************");

        BlackJackShoeGameAdvanced blackJackShoeGame = new BlackJackShoeGameAdvanced();


        int totalNumOfDecks = 1;

        while(blackJackShoeGame.playerChips>0) {//Player can play as long as they have more than one chip in hand.

            ArrayList cardsInHand = blackJackShoeGame.populateCards(totalNumOfDecks);
            System.out.println("Decks in hand!! ");
            System.out.println("-----------------------------------");
            System.out.println("Shuffling the cards.............");
            System.out.println("-----------------------------------");

            ArrayList cardsShuffled;
            cardsShuffled = blackJackShoeGame.shuffleCards(cardsInHand);


            do { //Repeat until player places a valid bet (positive integer in multiples of 2)
                System.out.println("Available chips for the player : " + blackJackShoeGame.playerChips);
                System.out.println("Please place your bets (multiples of 2 only):");
                blackJackShoeGame.playerBet = getUserInput(bufferRead,1);

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
            System.out.println("Player's cards : " + blackJackShoeGame.playerCards.get(0).toString() + " " + blackJackShoeGame.playerCards.get(1).toString());
            System.out.println("-----------------------------------");
            System.out.println("Dealer's cards : " + blackJackShoeGame.dealerCards.get(0).toString() + " " + " ** ");
            System.out.println("-----------------------------------");
            int playerOption=0;



            do {//Repeat till player chooses to stand or the player goes bust.
                if (blackJackShoeGame.playerCards.size() == 2) {
                    System.out.println("Players Options: 1.Stand  2.Hit  3.Surrender 4.DoubleDown [Enter the corresponding number]:");
                }
                else {
                    System.out.println("Players Options: 1.Stand  2.Hit [Enter the corresponding number]:");
                }
                playerOption = getUserInput(bufferRead,1);


                switch (playerOption) {
                    case 1:
                        System.out.println("Player Stands");
                        blackJackShoeGame.standCards();
                        break;
                    case 2:
                        System.out.println("Player Hits");
                        blackJackShoeGame.hitCards();
                        if (!blackJackShoeGame.playerBusted) {
                            playerOption = -1;
                            blackJackShoeGame.playerBusted = false;
                        }// blackJackShoeGame.dealersTurn();
                        break;
                    case 3:
                        if(blackJackShoeGame.playerCards.size()==2) {
                            System.out.println("Player opts for Surrender");
                            blackJackShoeGame.playerSurrender();
                            break;
                        }else{
                            System.out.println("Invaild Option! Cannot surrender after taking 3rd card.");
                            playerOption = -1;
                            break;
                        }
                    case 4:
                        if(blackJackShoeGame.playerCards.size()==2) {
                            //blackJackShoeGame.doubleDown = true;

                            System.out.println("Player opts for DoubleDown");
                            int doubleDownBet = 0;
                            do {
                                System.out.println("Please place your bet for Double Down (Enter number of chips between 0 to " + blackJackShoeGame.playerBet + " ): ");
                                doubleDownBet = getUserInput(bufferRead,1);
                                if(doubleDownBet<=blackJackShoeGame.playerBet) {
                                    blackJackShoeGame.playerDoubleDown(doubleDownBet);
                                }else{
                                    System.out.println("Cannot place bet higher than original bet.");
                                }
                            }while(doubleDownBet<= blackJackShoeGame.playerBet);
                            break;
                        }else{
                            System.out.println("Invaild Option! Cannot Double Down after taking 3rd card.");
                            playerOption = -1;
                            break;
                        }
                    default:
                        System.out.println("Please enter Again ");
                        break;
                }

            } while (playerOption != 1 && playerOption != 2 && playerOption != 3 && playerOption != 4 );

            if(blackJackShoeGame.playerChips == 0){
                System.out.println("Player has run out of chips");
                break;
            }

        }


    }

    //Populating 52 cards of a card
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

    //shuffling the the deck
    public ArrayList shuffleCards(ArrayList cardsRandom) {

        Collections.shuffle(cardsRandom);

        return cardsRandom;
    }

    //Dealing the first two cards to both the player and the dealer
    public void dealCards(ArrayList cardsInBox) {

        cardsInBox.remove(0);//burning the beginning card.

        dealerCards = new ArrayList();
        playerCards = new ArrayList();

        playerCards.add(0, cardsInBox.remove(0));
        dealerCards.add(0, cardsInBox.remove(0));
        playerCards.add(1, cardsInBox.remove(0));
        dealerCards.add(1, cardsInBox.remove(0));

        cardsToBeDealt = cardsInBox;

        return;
    }

    //To calculate the final total of the players cards and start the dealers turn
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

    //Cards to be dealt to dealer till the total of the cards reach 17 or above
    public void dealersTurn(){

        dealerTotal = 0;
        boolean cardIsAce = false;
        boolean dealAgain = true;
        int numOfAces = 0;

        do {

            for(Object cardObj: dealerCards) {
                Card dealerCard = (Card) cardObj;
                int tempValue = dealerCard.cardValuation();
                if (tempValue == 1) {
                    numOfAces++;
                    tempValue = 11;
                }
                dealerTotal = dealerTotal + tempValue;
                if (numOfAces > 0) {
                    numOfAces--;
                    if (dealerTotal >= 17 && dealerTotal <= 21) {
                        dealAgain = false;
                        break;
                    } else if (dealerTotal > 21) {
                        dealerTotal = dealerTotal - 10;
                        if (dealerTotal >= 17 && dealerTotal <= 21) {
                            dealAgain = false;
                            break;
                        }
                    }

                } else {
                    if (((dealerTotal >= 17 && dealerTotal <= 21) || dealerTotal > 21)) {
                        dealAgain = false;
                        break;
                    }

                }
            }

            if (dealAgain) {
                dealerCards.add(cardsToBeDealt.remove(0));
                dealerTotal = 0;
            }

        } while(dealAgain);

        System.out.println("Dealer's cards!!");
        System.out.println("-----------------------------");
        for (Object cardObj : dealerCards){
            System.out.print(" "+cardObj.toString());
        }
        System.out.println("");System.out.println("--------------------------");

        endResult();
    }

    //To calculate player bet and compare the totals of the player and dealer for different scenarios.
    private void endResult(){
        if(playerTotal == 21 && dealerTotal == 21){
            if((playerCards.size()==2 && dealerCards.size()==2) || (playerCards.size()>2 && dealerCards.size()>2)){
                System.out.println("Push!!");
            }
            else if(playerCards.size()==2 && dealerCards.size() >2){
                System.out.println("BlackJack for Player!! Player wins!");
                playerBet = playerBet + (playerBet*3)/2;
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
            System.out.println("Push!!");
        }
        else if(playerTotal == 21 && dealerTotal != 21 ){
            if(playerCards.size()==2){
                System.out.println("BlackJack for Player!! Player wins!");
                playerBet = playerBet+(playerBet*3)/2;
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
            playerBet = 0;
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

    //dealing a card to the player when they opt to hit and checking if the player goes bust.If busted automatically its the dealer turn.
    public void hitCards() {
        playerCards.add(cardsToBeDealt.remove(0));

        System.out.println("Player's cards");
        System.out.println("------------------------------------- ");

        for (Object cardObj : playerCards){
            System.out.print(" "+cardObj.toString());
        }
        System.out.println("");System.out.println("--------------------------------------");

        playerBusted = checkPlayerBusted();
        if(playerBusted){
            System.out.println("Player busted!! Dealer's turn!");
            dealersTurn();
        }
    }

    //checking if the player has busted.
    private boolean checkPlayerBusted(){

        boolean playerOver21 = false;
        playerTotal=0;
        numOfAce= 0;

        for (Object cardObj : playerCards){
            Card playerCard = (Card) cardObj;
            int tempValue = playerCard.cardValuation();
            if(tempValue == 1){
                tempValue = 11;
                numOfAce++;
            }
            playerTotal = playerTotal + tempValue;

            if(numOfAce>0){
                if(playerTotal>21){
                    playerTotal = playerTotal -10;
                    numOfAce--;
                    if(playerTotal>21){
                        playerOver21 = true;
                    }
                }
            }
            else{
                if(playerTotal>21){
                    playerOver21 = true;
                }
            }
        }
        return playerOver21;
    }

    //subtracting the placed bet from the total number of chips.
    public void placeBetsInCircle(int bet){

        playerChips = playerChips - bet;

    }

    //When the player surrenders the player is no longer dealt any hands, but loses only half of his bet.
    public void playerSurrender(){

        playerBet = playerBet/2;
        playerChips = playerChips+playerBet;
        System.out.println("Player receives half of original bet");
    }

    //when the player opts for double down then the player is dealt just one more hand and then dealer's cards are revealed.
    public void playerDoubleDown(int doubleDownBet){

        playerCards.add(cardsToBeDealt.remove(0));
        placeBetsInCircle(doubleDownBet);
        playerBet = playerBet+doubleDownBet;
        //System.out.println(playerBet+ " " + playerChips);
        int numOfAces = 0;

        playerTotal = 0;
        System.out.println("Player's cards");
        System.out.println("------------------------------------- ");
        for(Object cardObject : playerCards){

            Card playerCard = (Card)cardObject;

            System.out.print(" " + playerCard.toString());

            int tempValue = playerCard.cardValuation();

            if(tempValue == 1){
                tempValue = 11;
                numOfAces++;
            }

            playerTotal = playerTotal + tempValue;
            if(numOfAces>0){
                numOfAces--;
                if(playerTotal >21){
                    playerTotal = playerTotal -10;
                }
            }
        }


        System.out.println("");System.out.println("------------------------------------- ");

        dealersTurn();

    }

    //Get user Input from console.
    public static int getUserInput(BufferedReader bufferRead, int numTries){
        int intInput = -1;
        if (numTries <= 2) {
            try{
                String strInput = bufferRead.readLine();
                try {
                    intInput = Integer.parseInt(strInput);
                }
                catch (NumberFormatException nfe){
                    //Give user 2 tries
                    System.out.println("Invalid input "+strInput+". Please enter numbers only");
                    numTries++;
                    intInput = getUserInput(bufferRead,numTries);
                }
            }
            catch (IOException e){
                System.out.println("Error parsing player input. Game will exit now");
                e.printStackTrace();
                System.exit(1);
            }
        }
        else{
            System.out.println("Invalid input received. Game will exit now");
            System.exit(2);
        }
        return intInput;
    }
}


