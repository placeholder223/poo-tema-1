package main;

import fileio.CardInput;

import java.util.ArrayList;

public final class BetterEnvironment {
   private int mana;
   private String description;
   private ArrayList<String> colors;
   private String name;

   public BetterEnvironment(final CardInput card) {
      this.mana = card.getMana();
      this.description = card.getDescription();
      this.colors = card.getColors();
      this.name = card.getName();
   }

   public BetterEnvironment(final BetterEnvironment card) {
      this.mana = card.getMana();
      this.description = card.getDescription();
      this.colors = card.getColors();
      this.name = card.getName();
   }
   /**
    * the mana cost of playing this environment
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
    * Donna takara yori kagayaku MONO mitsuketa
    * @return
    */
   public ArrayList<String> getColors() {
      return colors;
   }
   /**
    * Furikaereba hora
    * BAKA mitai ni warau kao ga dokomademo susumaseru
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
    * we wanna rename it because
    */
   public void setName(final String name) {
      this.name = name;
   }
   /**
    * showing the environment for debugging porpuses
    */
   @Override
   public String toString() {
      return "CardInput{"
            + "mana="
            + mana
            + ", description='"
            + description
            + '\''
            + ", colors="
            + colors
            + ", name='"
            + ""
            + name
            + '\''
            + '}';
   }
}
