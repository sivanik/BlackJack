/**
 * Created by sivani on 4/8/14.
 */
public class Card {

    private int handValue;
    private char handSuite;

    public int getHandValue() {
        return handValue;
    }

    public void setHandValue(int handValue) {
        this.handValue = handValue;
    }

    public char getHandSuite() {
        return handSuite;
    }

    public void setHandSuite(char handSuite) {
        this.handSuite = handSuite;
    }

    public int cardValuation(){

        int valuation;

        if(this.handValue == 11 || this.handValue == 12 || this.handValue == 13){
            valuation = 10;
        }
        else
        {
            valuation = this.handValue;
        }
        return valuation;
    }

    public String cardNotation() {

        String cardType;

        switch(this.handValue){
            case 11:
                cardType = "J" + this.handSuite;
                break;
            case 12:
                cardType = "Q" + this.handSuite;
                break;
            case 13:
                cardType = "K" + this.handSuite;
                break;
            case 1:
                cardType = "A" + this.handSuite;
                break;
            default:
                cardType = Integer.toString(this.handValue) + this.handSuite;
        }
        return cardType;
    }

    public String toString(){
        return this.cardNotation();
    }

    public Card(int cardValue,char cardHand){
        this.handSuite = cardHand;
        this.handValue = cardValue;

        //this.hand = cardNotation(cardValue,cardHand);
        //this.handValue = cardValuation(cardValue);
    }

}
