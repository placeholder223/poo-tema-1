package main;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;


public class Board {
   @JsonInclude(JsonInclude.Include.NON_NULL)
   private final ArrayList<ArrayList<BetterMinion>> grid = new ArrayList<>();
   static final int ROW_NUMBERS = 4;
   static final int COLUMN_NUMBERS = 5;
   static final int ROW_0 = 0;
   static final int ROW_1 = 1;
   static final int ROW_2 = 2;
   static final int ROW_3 = 3;

   public Board() {
      for (int i = 0; i < ROW_NUMBERS; i++) {
         this.grid.add(new ArrayList<>());
      }
   }
   public Board(final Board board) {
      for (int i = 0; i < ROW_NUMBERS; i++) {
         this.grid.add(new ArrayList<>());
      }
      for (int i = 0; i < ROW_NUMBERS; i++) {
         for (int j = 0; j < board.getGrid().get(i).size(); j++) {
            this.grid.get(i).add(board.getGrid().get(i).get(j));
         }
      }
   }
   private int addToGrid(final BetterMinion card, final int row) {
      if (this.grid.get(row).size() == COLUMN_NUMBERS) {
         //mesaj eroare
         return 1;
      }
      grid.get(row).add(card);
      return 0;

   }
   /**
    * @param card   - The card that you want to put on the board
    * @param player - the player that puts the card
    */
   public int setGrid(final BetterMinion card, final int player) {
      if (player == 1) {
         if (card.getinFront()) {
            return addToGrid(card, ROW_2);
         } else {
            return addToGrid(card, ROW_3);
         }
      }
      if (card.getinFront()) {
         return addToGrid(card, ROW_1);
      } else {
         return addToGrid(card, ROW_0);
      }

   }

   /**
    * returns the row of the board that the program wants to show/check/whatever
    */
   public ArrayList<BetterMinion> getRow(final int row) {
      return grid.get(row);
   }

   /**
    * removes a minion that dies from the board
    */
   public void removeFromGrid(final int row, final int column) {
      grid.get(row).remove(column);
   }

   /**
    * returns the minion at the specified place
    */
   @JsonIgnore
   public BetterMinion getGridSquare(final int x, final int y) {
      if (y >= grid.get(x).size()) {
         return null;
      }
      return grid.get(x).get(y);
   }

   /**
    * returns the whole board when needed for getCardsOnTable
    */
   @JsonInclude(JsonInclude.Include.NON_NULL)
   public ArrayList<ArrayList<BetterMinion>> getGrid() {
      return this.grid;
   }

   /**
    * at the end of the turn, all cards on the player's side lose the Frozen status
    */
   public void unfreezeAll(final int player) {
      for (int i = ROW_NUMBERS - 2 * player;
           i <= ROW_NUMBERS - 2 * player + 1; i++) {
         for (int j = 0; j < this.grid.get(i).size(); j++) {
            grid.get(i).get(j).setFrozen(false);
         }
      }
   }


   /**
    * at the end of the turn, all cards on the board can
    * attack and use their abilities again
    */
   @JsonIgnore
   public void resetAttackAndAbilities(final int player) {
      for (int i = ROW_NUMBERS - 2 * player;
           i <= ROW_NUMBERS - 2 * player + 1; i++) {
         for (int j = 0; j < this.grid.get(i).size(); j++) {
            grid.get(i).get(j).setAttackStatus(false);
         }
      }
   }

   /**
    * checks if there is a tank on the enemy field
    * because tanks must be attacked first
    */
   @JsonIgnore
   public boolean getTankStatus(final int attackedX) {
      if (attackedX == ROW_0 || attackedX == ROW_1) {
         for (int j = 0; j < this.grid.get(ROW_1).size(); j++) {
            if (this.getGridSquare(ROW_1, j).getTank()) {
               return true;
            }
         }
         return false;
      }
      for (int j = 0; j < this.grid.get(ROW_2).size(); j++) {
         if (this.getGridSquare(ROW_2, j).getTank()) {
            return true;
         }
      }
      return false;
   }
}
