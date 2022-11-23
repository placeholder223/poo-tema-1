package main;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fileio.CardInput;

import java.util.ArrayList;

public class BetterMinion {
   private int mana;
   private int attackDamage;
   private int health;
   private String description;
   private ArrayList<String> colors;
   private String name;

   @JsonIgnore
   private boolean inFront;
   @JsonIgnore
   private boolean tank = false;
   @JsonIgnore
   private boolean frozen;
   @JsonIgnore
   private boolean hasAttacked = false;

   public BetterMinion(final CardInput card) {
      this.attackDamage = card.getAttackDamage();
      this.health = card.getHealth();
      this.mana = card.getMana();
      this.description = card.getDescription();
      this.colors = card.getColors();
      this.name = card.getName();
   }

   public BetterMinion(final BetterMinion card) {
      this.attackDamage = card.getAttackDamage();
      this.health = card.getHealth();
      this.mana = card.getMana();
      this.description = card.getDescription();
      this.colors = card.getColors();
      this.name = card.getName();
      this.inFront = card.getinFront();
      this.frozen = card.getFrozen();
      this.tank = card.getTank();
   }

   /**
    * the attack points of the card
    */
   public int getAttackDamage() {
      return attackDamage;
   }

   /**
    * the attack of a card can change only when abilities are used on it
    */
   public void setAttackDamage(final int attackDamage) {
      this.attackDamage = attackDamage;
   }

   /**
    * the health that the card has left
    */
   public int getHealth() {
      return this.health;
   }

   /**
    * the health of a card changes when it gets attacked or when an ability is used on it
    */
   public void setHealth(final int health) {
      this.health = health;
   }

   /**
    * if the card must be attacked first
    */
   @JsonIgnore
   public boolean getTank() {
      return this.tank;
   }

   /**
    * only used when the card is initialized
    */
   public void setTank() {
      this.tank = true;
   }

   /**
    * when an attack is declared, we must see if the card is frozen first
    */
   @JsonIgnore
   public boolean getFrozen() {
      return this.frozen;
   }

   /**
    * a card can only be freezed by certain abilities
    */
   public void setFrozen(final boolean frozen) {
      this.frozen = frozen;
   }

   /**
    * the mana cost of playing this card
    */
   public int getMana() {
      return mana;
   }

   /**
    * setter for the mana
    */
   public void setMana(final int mana) {
      this.mana = mana;
   }

   /**
    * the little bits of lore about the card
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
    * SHINYYYY
    */
   public ArrayList<String> getColors() {
      return colors;
   }

   /**
    * PRECIOUSS
    */
   public void setColors(final ArrayList<String> colors) {
      this.colors = colors;
   }

   /**
    * used when initializing the cards to see what type they are
    */
   public String getName() {
      return name;
   }

   /**
    * this costs 50 gems
    */
   public void setName(final String name) {
      this.name = name;
   }

   /**
    * see if the card must be placed in the front row or not
    */
   @JsonIgnore
   public boolean getinFront() {
      return this.inFront;
   }

   /**
    * it wants to see the show Better
    */
   public void setInFront(final boolean value) {
      this.inFront = value;
   }


   /**
    * see if the minion has attacked or used its ability already
    */
   @JsonIgnore
   public boolean getAttackStatus() {
      return this.hasAttacked;
   }

   /**
    * changes when the minion has attacked or used its ability
    */
   @JsonIgnore
   public void setAttackStatus(final boolean value) {
      this.hasAttacked = value;
   }

   /**
    * showing the full card for debugging porpuses
    *
    * @return
    */
   @Override
   public String toString() {
      return "CardInput{"
            + "mana="
            + mana
            + ", attackDamage="
            + attackDamage
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
