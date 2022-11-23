package main;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;

import java.util.ArrayList;

public class UseEnvironmentCard {
   /**
    * the function that decides which environment effect to use, and how to use it
    * the function is not coded into the card itself, but instead is decided at runtime
    */
   public boolean useEnvironmentCard(final Board board, final ThrowError errors,
                                     final ExtraDataGame extraData, final ObjectNode temp,
                                     final ActionsInput currAction, final int currentPlayer,
                                     final CalculatingBasedOnPlayer calculating) {
      ArrayList<Object> currentHand;
      int currentMana;
      if (currentPlayer == 1) {
         currentHand = new ArrayList<>(extraData.handPlayerOne);
         currentMana = extraData.getManaPlayerOne();
      } else {
         currentHand = new ArrayList<>(extraData.handPlayerTwo);
         currentMana = extraData.getManaPlayerTwo();
      }
      if (!(currentHand.get(currAction.getHandIdx())
            instanceof BetterEnvironment)) {
         temp.put("affectedRow", currAction.getAffectedRow());
         temp.put("error", "Chosen card is not of type environment.");
         return errors.handError(temp, currAction);
      }
      if (((BetterEnvironment)
            currentHand.get(currAction.getHandIdx())).getMana()
            > currentMana) {
         temp.put("affectedRow", currAction.getAffectedRow());
         temp.put("error", "Not enough mana to use environment card.");
         return errors.handError(temp, currAction);
      }
      if (calculating.isEnemyRow(currentPlayer, currAction)) {
         temp.put("affectedRow", currAction.getAffectedRow());
         temp.put("error", "Chosen row does not belong to the enemy.");
         return errors.handError(temp, currAction);
      }
      String nameOfCard;
      if (currentPlayer == 1) {
         nameOfCard = ((BetterEnvironment)
               extraData.handPlayerOne.get(currAction.getHandIdx())).getName();
      } else {
         nameOfCard = ((BetterEnvironment)
               extraData.handPlayerTwo.get(currAction.getHandIdx())).getName();
      }
      switch (nameOfCard) {
         case "Firestorm":
            for (int j = 0; j < board.getRow(currAction.getAffectedRow()).size();
                 j++) {
               BetterMinion tempMinion =
                     board.getGridSquare(currAction.getAffectedRow(), j);
               tempMinion.setHealth(tempMinion.getHealth() - 1);
               if (tempMinion.getHealth() <= 0) {
                  board.removeFromGrid(currAction.getAffectedRow(), j);
                  j--;
               }
            }
            break;
         case "Winterfell":
            for (int j = 0; j < board.getRow(currAction.getAffectedRow()).size();
                 j++) {
               board.getGridSquare(currAction.getAffectedRow(), j).setFrozen(true);
            }
            break;
         case "Heart Hound":
            if (board.getRow(Board.ROW_NUMBERS
                  - currAction.getAffectedRow() - 1).size()
                  == Board.COLUMN_NUMBERS) {
               temp.put("affectedRow", currAction.getAffectedRow());
               temp.put("error",
                     "Cannot steal enemy card since the player's row is full.");
               return errors.handError(temp, currAction);
            }
            int max = 0;
            int yMax = 0;
            for (int j = 0;
                 j < board.getRow(currAction.getAffectedRow()).size(); j++) {
               if (max < board.getGridSquare(
                     currAction.getAffectedRow(), j).getAttackDamage()) {
                  max = board.getGridSquare(
                        currAction.getAffectedRow(), j).getAttackDamage();
                  yMax = j;
               }
               BetterMinion tempMinion = board.getGridSquare(
                     currAction.getAffectedRow(), yMax);
               board.removeFromGrid((currAction.getAffectedRow()), yMax);
               board.setGrid(tempMinion, currentPlayer);
            }
            break;
         default:
            //System.out.println("n-are cum");
            break;
      }
      if (currentPlayer == 1) {
         extraData.setManaPlayerOne(extraData.getManaPlayerOne() - ((BetterEnvironment)
               extraData.handPlayerOne.get(currAction.getHandIdx())).getMana());
         extraData.handPlayerOne.remove(currAction.getHandIdx());
      } else {
         extraData.setManaPlayerTwo(extraData.getManaPlayerTwo() - ((BetterEnvironment)
               extraData.handPlayerTwo.get(currAction.getHandIdx())).getMana());
         extraData.handPlayerTwo.remove(currAction.getHandIdx());
      }
      return false;
   }
}
