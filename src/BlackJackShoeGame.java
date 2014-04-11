import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
    public ArrayList playerCards;
    public ArrayList dealerCards;
    private int playerBet;
    private double playerChips = 100;



    public static void main(String[] args) {

        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("***************Casino Rules for BlackJack Table***********************************************************");
        System.out.println("         BLACKJACK pays 3 to 2");
        System.out.println("         Dealer must stand on all 17");
        System.out.println("         If the Player bets zero chips it implies they are opting out of the game and the game ends!!!");
        System.out.println("         If the Player enters invalid input(space/enter/non numeric characters) twice in a row, the game exits!!!");
        System.out.println("***********************************************************************************************************");

        BlackJackShoeGame blackJackShoeGame = new BlackJackShoeGame();


        int totalNumOfDecks = 1;
        int bet = 0;

        while(blackJackShoeGame.playerChips>1) {//Player can play as long as they have more than one chip in hand.

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
                bet = getUserInput(bufferRead, 1);

                if (bet % 2 == 0 && bet > 0){
                    break;
                }
                else if (bet == 0){
                    System.out.println("Game ends!!");
                    System.exit(0);
                }
                else{
                    System.out.println("Enter again");
                }
            } while(bet % 2 != 0 || bet < 0);

            blackJackShoeGame.placeBetsInCircle(bet);

            System.out.println("-----------------------------------");
            System.out.println("Dealing the cards !!!!!!!");
            System.out.println("-----------------------------------");


            blackJackShoeGame.dealCards(cardsShuffled);
            System.out.println("Players cards : " + blackJackShoeGame.playerCards.get(0).toString() + " " + blackJackShoeGame.playerCards.get(1).toString());
            System.out.println("-----------------------------------");
            System.out.println("Dealers cards : " + blackJackShoeGame.dealerCards.get(0).toString() + " " + " ** ");
            System.out.println("-----------------------------------");
            int playerOption;




            do {//Repeat till player chooses to stand or the player goes bust.
                System.out.println("Players Options: 1.Stand  2.Hit [Enter the corresponding number]:");
                playerOption = getUserInput(bufferRead, 1);

                switch (playerOption) {
                    case 1:
                        System.out.println("Player Stands");
                        blackJackShoeGame.standCards();
                        //blackJackShoeGame.dealersTurn();
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

    //Dealing the first two cards to both the player and the dealer .
    public void dealCards(ArrayList cardsInBox) {

        cardsInBox.remove(0);//burning the beginning card

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
                playerBet = playerBet + (playerBet*3)/2;
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
            System.out.println("Player goes bust!!");
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

        this.playerChips = this.playerChips - bet;
        this.playerBet = bet;
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
