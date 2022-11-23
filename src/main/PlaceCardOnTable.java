package main;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;

import java.util.ArrayList;

public class PlaceCardOnTable {
   /**
    * function that places the specified card (or returns error)
    */
   public boolean placeCardOnTable(final Board board, final ExtraDataGame extraData,
                                   final ThrowError errors, final ActionsInput currAction,
                                   final ObjectNode temp, final int currentPlayer) {
      ArrayList<Object> currentHand;
      int currentMana;
      if (currentPlayer == 1) {
         currentHand = new ArrayList<>(extraData.handPlayerOne);
         currentMana = extraData.getManaPlayerOne();
      } else {
         currentHand = new ArrayList<>(extraData.handPlayerTwo);
         currentMana = extraData.getManaPlayerTwo();
      }
      if (currentHand.get(currAction.getHandIdx())
            instanceof BetterEnvironment) {
         temp.put("error", "Cannot place environment card on table.");
         return errors.handError(temp, currAction);
      }
      if (((BetterMinion) currentHand.get(
            currAction.getHandIdx())).getMana()
            > currentMana) {
         temp.put("error",
               "Not enough mana to place card on table.");
         return errors.handError(temp, currAction);
      }
      if (board.setGrid((BetterMinion) currentHand.get(
            currAction.getHandIdx()), currentPlayer) == 0) {
         if (currentPlayer == 1) {
            extraData.setManaPlayerOne(currentMana
                  - ((BetterMinion) currentHand.get(
                  currAction.getHandIdx())).getMana());
            extraData.handPlayerOne.remove(currAction.getHandIdx());
         } else {
            extraData.setManaPlayerTwo(currentMana
                  - ((BetterMinion) currentHand.get(
                  currAction.getHandIdx())).getMana());
            extraData.handPlayerTwo.remove(currAction.getHandIdx());
         }
      } else {
         temp.put("error", "Cannot place card "
               + "on table since row is full.");
         return errors.handError(temp, currAction);
      }
      return false;
   }
}
