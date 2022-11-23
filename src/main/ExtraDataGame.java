package main;

import java.util.ArrayList;

public class ExtraDataGame {
   static final int HERO_STARTING_HEALTH = 30;
   static final int MAX_MANA = 10;
   /**
    * we know the first player, but we don't directly know the second one
    * but with a bit of thinkig I figured out that
    * 3 - starting_player is always the second player
    */
   static final int FUNKY_TURN_MATH = 3;
   private int manaPlayerOne;
   private int manaPlayerTwo;
   private int gameOver;
   /**
    * the following fields are not private because
    * there is no point in making the code harder to read
    * for the sake of "OOP"  (not really with this homework, I go more in depth in the README)
    * unfortunately I didn't make public the rest of the fields that have the same problem
    */
   public int givenMana;
   public int actionIndex;
   public ArrayList<Object> handPlayerOne;
   public ArrayList<Object> handPlayerTwo;
   public BetterHero heroPlayerOne;
   public BetterHero heroPlayerTwo;
   public ArrayList<Object> deckOne;
   public ArrayList<Object> deckTwo;

   public ExtraDataGame() {
      this.manaPlayerOne = 0;
      this.manaPlayerTwo = 0;
      this.gameOver = 0;
      this.givenMana = 0;
      this.actionIndex = 0;
      this.handPlayerOne = new ArrayList<>();
      this.handPlayerTwo = new ArrayList<>();
   }

   /**
    * sees how much mana player one has
    */
   public int getManaPlayerOne() {
      return manaPlayerOne;
   }

   /**
    * the mana of a player is modified at the beginning of the turn
    * and after the player uses an Environment or a card/hero ability
    */
   public void setManaPlayerOne(final int manaPlayerOne) {
      this.manaPlayerOne = manaPlayerOne;
   }

   /**
    * sees how much mana player one has
    */
   public int getManaPlayerTwo() {
      return manaPlayerTwo;
   }

   /**
    * the mana of a player is modified at the beginning of the turn
    * and after the player uses an Environment or a card/hero ability
    */
   public void setManaPlayerTwo(final int manaPlayerTwo) {
      this.manaPlayerTwo = manaPlayerTwo;
   }

   /**
    * returns the state of the current game
    */
   public int getGameOver() {
      return gameOver;
   }

   /**
    * the game is over when there are no more commands
    * or one of the player's hero reached zero health
    */
   public void setGameOver(final int gameOver) {
      this.gameOver = gameOver;
   }
}
