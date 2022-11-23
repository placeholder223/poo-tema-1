package main;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fileio.CardInput;

import java.util.ArrayList;

public class BetterHero {
   private int mana;
   private int health;
   private String description;
   private ArrayList<String> colors;
   private String name;
   @JsonIgnore
   private boolean frozen;

   @JsonIgnore
   private boolean hasAttacked = false;

   public BetterHero(final CardInput card) {
      this.health = card.getHealth();
      this.mana = card.getMana();
      this.description = card.getDescription();
      this.colors = card.getColors();
      this.name = card.getName();
   }

   public BetterHero(final BetterHero card) {
      this.health = card.getHealth();
      this.mana = card.getMana();
      this.description = card.getDescription();
      this.colors = card.getColors();
      this.name = card.getName();
      this.hasAttacked = card.hasAttacked;
   }

   /**
    * how much hp the hero has left
    */

   public int getHealth() {
      return health;
   }

   /**
    * the hero's hp changes when it is attacked
    */
   public void setHealth(final int health) {
      this.health = health;
   }

   /**
    * see if the hero is frozen
    */
   @JsonIgnore
   public boolean getFrozen() {
      return this.frozen;
   }

   /**
    * the hero can only be frozen by abilities
    */
   public void setFrozen(final boolean frozen) {
      this.frozen = frozen;
   }

   /**
    * how much mana the hero's ability costs
    */
   public int getMana() {
      return mana;
   }

   /**
    * for initialization
    */
   public void setMana(final int mana) {
      this.mana = mana;
   }

   /**
    * was the hero a farmer? a demonspawn?
    * now we can know
    */
   public String getDescription() {
      return description;
   }

   /**
    * unused setter for the description of the card
    */
   public void setDescription(final String description) {
      this.description = description;
   }

   /**
    * shapes and colors the likes of which I've never seen
    */
   public ArrayList<String> getColors() {
      return colors;
   }

   /**
    * unused setter
    */
   public void setColors(final ArrayList<String> colors) {
      this.colors = colors;
   }

   /**
    * is this the krusty krab?
    */
   public String getName() {
      return name;
   }

   /**
    * NO, THIS IS PATRICK!
    */
   public void setName(final String name) {
      this.name = name;
   }

   /**
    * see if the hero has used its ability already
    * it's named 'Attacked' for consistency with the minion
    */
   public boolean getHasAttacked() {
      return this.hasAttacked;
   }

   /**
    * changes when the hero has used its ability
    */
   public void setHasAttacked(final boolean value) {
      this.hasAttacked = value;
   }

   /**
    * showing the hero for debugging porpuses
    */
   @Override
   public String toString() {
      return "CardInput{"
            + "mana="
            + mana
            + ", health="
            + health
            + ", description='"
            + description
            + '\''
            + ", colors="
            + colors
            + ", name='"
            + ""
            + name
            + '\''
            + '}'
            + ", frozen='"
            + ""
            + frozen
            + '\''
            + '}';
   }
}
