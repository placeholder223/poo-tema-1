package main;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;

public class CardUsesAbility {
   /**
    * the function that decides which ability to use, and how to use it
    * the function is not coded into the card itself, but instead is decided at runtime
    */
   public boolean cardUsesAbility(final Board board, final ActionsInput currAction,
                                  final ObjectNode temp, final ThrowError errors,
                                  final CalculatingBasedOnPlayer calculating,
                                  final int currentPlayer) {
      int affectedX = currAction.getCardAttacked().getX();
      int affectedY = currAction.getCardAttacked().getY();
      int affecterX = currAction.getCardAttacker().getX();
      int affecterY = currAction.getCardAttacker().getY();
      if (board.getGridSquare(affecterX, affecterY).getFrozen()) {
         temp.put("error", "Attacker card is frozen.");
         return errors.attackError(temp, currAction);
      }
      if (board.getGridSquare(affecterX, affecterY).getAttackStatus()) {
         temp.put("error", "Attacker card has already attacked this turn.");
         return errors.attackError(temp, currAction);
      }
      if (board.getTankStatus(affectedX)
            && board.getGridSquare(
            affecterX, affecterY).getName().compareTo("Disciple") != 0) {
         if (!board.getGridSquare(affectedX, affectedY).getTank()) {
            temp.put("error", "Attacked card is not of type 'Tank'.");
            return errors.attackError(temp, currAction);
         }
      }
      if (board.getGridSquare(affecterX,
            affecterY).getName().compareTo("Disciple") != 0) {
         if (calculating.isEnemyRow(currentPlayer, affectedX)) {
            temp.put("error", "Attacked card does not belong to the enemy.");
            return errors.attackError(temp, currAction);
         }
      } else {
         if (calculating.isAllyRow(currentPlayer, affectedX)) {
            temp.put("error", "Attacked card does not belong to the current "
                  + "player.");
            return errors.attackError(temp, currAction);
         }
      }
      switch (board.getGridSquare(affecterX, affecterY).getName()) {
         case "The Ripper" -> {
            board.getGridSquare(affectedX, affectedY).setAttackDamage(
                  board.getGridSquare(affectedX, affectedY).getAttackDamage() - 2);
            if (board.getGridSquare(affectedX, affectedY).getAttackDamage() < 0) {
               board.getGridSquare(affectedX, affectedY).setAttackDamage(0);
            }
         }
         case "Miraj" -> {
            int aux = board.getGridSquare(affecterX, affecterY).getHealth();
            board.getGridSquare(affecterX, affecterY).setHealth(
                  board.getGridSquare(affectedX, affectedY).getHealth());
            board.getGridSquare(affectedX, affectedY).setHealth(aux);
         }
         case "The Cursed One" -> {
            int aux = board.getGridSquare(affectedX, affectedY).getHealth();
            board.getGridSquare(affectedX, affectedY).setHealth(
                  board.getGridSquare(affectedX, affectedY).getAttackDamage());
            board.getGridSquare(affectedX, affectedY).setAttackDamage(aux);
            if (board.getGridSquare(affectedX, affectedY).getHealth() == 0) {
               board.removeFromGrid(affectedX, affectedY);
            }
         }
         case "Disciple" -> board.getGridSquare(affectedX, affectedY).setHealth(
               board.getGridSquare(affectedX, affectedY).getHealth() + 2);
         default -> { //I MUST OBEY TO THE ALLMIGHTY CHECKER
         }
      }
      board.getGridSquare(affecterX, affecterY).setAttackStatus(true);
      return false;
   }
}
