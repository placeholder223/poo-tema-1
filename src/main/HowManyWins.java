package main;

public class HowManyWins {
   private int winsPlayerOne;
   private int winsPlayerTwo;

   public HowManyWins() {
      this.winsPlayerOne = 0;
      this.winsPlayerTwo = 0;
   }

   /**
    * see how many wins the player one has
    */
   public int getWinsPlayerOne() {
      return winsPlayerOne;
   }

   /**
    * see how many wins the player two has
    */
   public int getWinsPlayerTwo() {
      return winsPlayerTwo;
   }

   /**
    * used when the player one wins a game, thereby increasing the amount by one
    * useful to make the code just a bit prettier
    */
   public void increaseWinsPlayerOne() {
      this.winsPlayerOne = this.winsPlayerOne + 1;
   }

   /**
    * used when the player one wins a game, thereby increasing the amount by one
    * useful to make the code just a bit prettier
    */
   public void increaseWinsPlayerTwo() {
      this.winsPlayerTwo = this.winsPlayerTwo + 1;
   }

   /**
    * unused setter for code style
    */
   public void setWinsPlayerOne(final int winsPlayerOne) {
      this.winsPlayerOne = winsPlayerOne;
   }

   /**
    * BOW TO THE CODING STYLE
    */
   public void setWinsPlayerTwo(final int winsPlayerTwo) {
      this.winsPlayerTwo = winsPlayerTwo;
   }
}
