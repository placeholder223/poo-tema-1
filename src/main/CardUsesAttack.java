package main;


import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;

public class CardUsesAttack {
   /**
    * the function that handles how a card attacks another
    */
   public boolean cardUsesAttackOnCard(final ActionsInput currAction, final Board board,
                                       final ThrowError errors, final ObjectNode temp,
                                       final int currentPlayer) {
      int attackedX = currAction.getCardAttacked().getX();
      int attackedY = currAction.getCardAttacked().getY();
      int attackerX = currAction.getCardAttacker().getX();
      int attackerY = currAction.getCardAttacker().getY();
      if (board.getGridSquare(attackerX, attackerY).getFrozen()) {
         temp.put("command", currAction.getCommand());
         temp.put("error", "Attacker card is frozen.");
         return errors.attackError(temp, currAction);
      }
      if (board.getGridSquare(attackerX, attackerY).getAttackStatus()) {
         temp.put("command", currAction.getCommand());
         temp.put("error", "Attacker card has already attacked this turn.");
         return errors.attackError(temp, currAction);
      }
      if (board.getTankStatus(attackedX)) {
         if (!board.getGridSquare(attackedX, attackedY).getTank()) {
            temp.put("command", currAction.getCommand());
            temp.put("error", "Attacked card is not of type 'Tank'.");
            return errors.attackError(temp, currAction);
         }

      }
      if (attackedX == Board.ROW_NUMBERS - 2 * currentPlayer
            || attackedX == Board.ROW_NUMBERS
            - 2 * currentPlayer + 1) {
         temp.put("command", currAction.getCommand());
         temp.put("error", "Attacked card does not belong to the enemy.");
         return errors.attackError(temp, currAction);
      }
      board.getGridSquare(attackedX, attackedY).setHealth(
            board.getGridSquare(attackedX, attackedY).getHealth()
                  - board.getGridSquare(attackerX, attackerY).getAttackDamage());
      board.getGridSquare(attackerX, attackerY).setAttackStatus(true);
      if (board.getGridSquare(attackedX, attackedY).getHealth() <= 0) {
         board.removeFromGrid(attackedX, attackedY);
      }
      return false;
   }

   /**
    * the function that handles how a card attacks the hero
    */
   public boolean cardUsesAttackOnHero(final ActionsInput currAction, final Board board,
                                       final ObjectNode temp, final int currentPlayer,
                                       final ExtraDataGame extraData, final HowManyWins numWins) {
      int attackerX = currAction.getCardAttacker().getX();
      int attackerY = currAction.getCardAttacker().getY();
      if (board.getGridSquare(attackerX, attackerY).getFrozen()) {
         temp.putPOJO("cardAttacker", currAction.getCardAttacker());
         temp.put("command", currAction.getCommand());
         temp.put("error", "Attacker card is frozen.");
         return true;
      }
      if (board.getGridSquare(attackerX, attackerY).getAttackStatus()) {
         temp.putPOJO("cardAttacker", currAction.getCardAttacker());
         temp.put("command", currAction.getCommand());
         temp.put("error", "Attacker card has already attacked this turn.");
         return true;
      }
      if (board.getTankStatus(currentPlayer)) {
         temp.putPOJO("cardAttacker", currAction.getCardAttacker());
         temp.put("command", currAction.getCommand());
         temp.put("error", "Attacked card is not of type 'Tank'.");
         return true;
      }
      board.getGridSquare(attackerX, attackerY).setAttackStatus(true);
      if (currentPlayer == 1) {
         extraData.heroPlayerTwo.setHealth(extraData.heroPlayerTwo.getHealth()
               - board.getGridSquare(attackerX, attackerY).getAttackDamage());
         if (extraData.heroPlayerTwo.getHealth() <= 0) {
            temp.put("gameEnded", "Player one killed the enemy hero.");
            numWins.increaseWinsPlayerOne();
            extraData.setGameOver(1);
            return true;
         }
      } else {
         extraData.heroPlayerOne.setHealth(extraData.heroPlayerOne.getHealth()
               - board.getGridSquare(attackerX, attackerY).getAttackDamage());
         if (extraData.heroPlayerOne.getHealth() <= 0) {
            extraData.setGameOver(1);
            temp.put("gameEnded", "Player two killed the enemy hero.");
            numWins.increaseWinsPlayerTwo();
            return true;
         }
      }
      return false;
   }
}
