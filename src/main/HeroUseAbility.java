package main;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;

public class HeroUseAbility {
   /**
    * the function that decides which ability to use
    * the function is not coded into the card itself, but instead is decided at runtime
    */
   public boolean heroUseAbility(final Board board, final ExtraDataGame extraData,
                                 final ThrowError errors, final ObjectNode temp,
                                 final ActionsInput currAction, final int currentPlayer,
                                 final CalculatingBasedOnPlayer calculating) {
      String heroName;
      int currentMana;
      int neededMana;
      boolean hasAttacked;
      if (currentPlayer == 1) {
         heroName = extraData.heroPlayerOne.getName();
         currentMana = extraData.getManaPlayerOne();
         neededMana = extraData.heroPlayerOne.getMana();
         hasAttacked = extraData.heroPlayerOne.getHasAttacked();
      } else {
         heroName = extraData.heroPlayerTwo.getName();
         currentMana = extraData.getManaPlayerTwo();
         neededMana = extraData.heroPlayerTwo.getMana();
         hasAttacked = extraData.heroPlayerTwo.getHasAttacked();
      }
      if (currentMana < neededMana) {
         temp.put("error", "Not enough mana to use hero's ability.");
         return errors.rowError(temp, currAction);
      }
      if (hasAttacked) {
         temp.put("error", "Hero has already attacked this turn.");
         return errors.rowError(temp, currAction);
      }

      switch (heroName) {
         case "Lord Royce" -> {
            if (calculating.isEnemyRow(currentPlayer, currAction)) {
               temp.put("error", "Selected row does not belong to the enemy.");
               return errors.rowError(temp, currAction);
            }
            board.getGridSquare(currAction.getAffectedRow(),
                  calculating.maxAttack(board, currAction)).setFrozen(true);
         }
         case "Empress Thorina" -> {
            if (calculating.isEnemyRow(currentPlayer, currAction)) {
               temp.put("error", "Selected row does not belong to the enemy.");
               return errors.rowError(temp, currAction);
            }
            board.removeFromGrid((currAction.getAffectedRow()),
                  calculating.maxHealth(board, currAction));
         }
         case "King Mudface" -> {
            if (calculating.isAllyRow(currentPlayer, currAction)) {
               temp.put("error", "Selected row does not belong to the current "
                     + "player.");
               return errors.rowError(temp, currAction);
            }
            for (int j = 0;
                 j < board.getRow(currAction.getAffectedRow()).size(); j++) {
               board.getGridSquare(currAction.getAffectedRow(), j).setHealth(
                     board.getGridSquare(
                           currAction.getAffectedRow(), j).getHealth() + 1);
            }
         }
         case "General Kocioraw" -> {
            if (calculating.isAllyRow(currentPlayer, currAction)) {
               temp.put("error", "Selected row does not belong to the current "
                     + "player.");
               return errors.rowError(temp, currAction);
            }
            for (int j = 0;
                 j < board.getRow(currAction.getAffectedRow()).size(); j++) {
               board.getGridSquare(currAction.getAffectedRow(), j).setAttackDamage(
                     board.getGridSquare(
                           currAction.getAffectedRow(), j).getAttackDamage() + 1);
            }
         }
         default -> { //all hail the allmighty codestyle checker
         }
      }
      if (currentPlayer == 1) {
         extraData.setManaPlayerOne(extraData.getManaPlayerOne()
               - extraData.heroPlayerOne.getMana());
         extraData.heroPlayerOne.setHasAttacked(true);
      } else {
         extraData.setManaPlayerTwo(extraData.getManaPlayerTwo()
               - extraData.heroPlayerTwo.getMana());
         extraData.heroPlayerTwo.setHasAttacked(true);
      }
      return false;
   }
}
