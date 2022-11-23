package main;

import fileio.ActionsInput;

public class CalculatingBasedOnPlayer {
   public CalculatingBasedOnPlayer() {

   }

   /**
    * checks if the affected row is the enemy's
    */
   public boolean isEnemyRow(final int currentPlayer, final ActionsInput currAction) {
      if (currentPlayer == 1) {
         return currAction.getAffectedRow() == Board.ROW_2
               || currAction.getAffectedRow() == Board.ROW_3;
      }
      return currAction.getAffectedRow() == Board.ROW_1
            || currAction.getAffectedRow() == Board.ROW_0;
   }

   /**
    * checks if the affected row is the ally's
    */
   public boolean isAllyRow(final int currentPlayer, final ActionsInput currAction) {
      if (currentPlayer == 2) {
         return currAction.getAffectedRow() == Board.ROW_2
               || currAction.getAffectedRow() == Board.ROW_3;
      }
      return currAction.getAffectedRow() == Board.ROW_1
            || currAction.getAffectedRow() == Board.ROW_0;
   }

   /**
    * checks if the affected card belongs to the enemy
    */
   public boolean isEnemyRow(final int currentPlayer, final int attackedX) {
      if (currentPlayer == 1) {
         return attackedX == Board.ROW_2
               || attackedX == Board.ROW_3;
      }
      return attackedX == Board.ROW_1
            || attackedX == Board.ROW_0;
   }

   /**
    * checks if the affected card belongs to the ally
    */
   public boolean isAllyRow(final int currentPlayer, final int attackedX) {
      if (currentPlayer == 2) {
         return attackedX == Board.ROW_2
               || attackedX == Board.ROW_3;
      }
      return attackedX == Board.ROW_1
            || attackedX == Board.ROW_0;
   }

   /**
    * calculates the position of the minion with the highest ATK
    */
   public int maxHealth(final Board board, final ActionsInput currAction) {
      int max = 0;
      int yMax = 0;
      for (int j = 0;
           j < board.getRow(currAction.getAffectedRow()).size(); j++) {
         if (max < board.getGridSquare(
               currAction.getAffectedRow(), j).getHealth()) {
            max = board.getGridSquare(
                  currAction.getAffectedRow(), j).getHealth();
            yMax = j;
         }
      }
      return yMax;
   }

   /**
    * calculates the position of the minion with the highest HP
    */
   public int maxAttack(final Board board, final ActionsInput currAction) {
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
      }
      return yMax;
   }
}
