package main;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;

public class Outputs {
   public Outputs() {

   }

   /**
    * used to return how many wins a player has
    */
   public boolean getPlayerWins(final ObjectNode temp, final ActionsInput currAction,
                                final HowManyWins numWins, final int player) {
      if (player == 1) {
         temp.put("command", currAction.getCommand());
         temp.put("output", numWins.getWinsPlayerOne());
         return true;
      }
      temp.put("command", currAction.getCommand());
      temp.put("output", numWins.getWinsPlayerTwo());
      return true;
   }

   /**
    * how many games have been played
    */
   public boolean getGamesPlayed(final ObjectNode temp, final ActionsInput currAction,
                                 final int currentNumber) {
      temp.put("command", currAction.getCommand());
      temp.put("output", currentNumber + 1);
      return true;
   }
}
