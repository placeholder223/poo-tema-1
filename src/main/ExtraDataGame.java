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
   public int manaPlayerOne;
   public int manaPlayerTwo;
   public int gameOver;
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


//   public int getWinsPlayer(final int which){
//      if(which == 1){
//         return this.winsPlayerOne;
//      }
//      return this.winsPlayerTwo;
//   }
//   public void setWinsPlayer(final int which, final int howMany){
//      if(which == 1){
//         this.winsPlayerOne = howMany;
//      }
//      this.winsPlayerTwo = howMany;
//   }
}
