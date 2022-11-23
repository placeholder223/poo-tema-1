package main;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;

public class ThrowError {
   /**
    * used to replace those 2 lines in the code with only one
    * a bit of tidying
    */
   public boolean handError(final ObjectNode temp, final ActionsInput currAction) {
      temp.put("command", currAction.getCommand());
      temp.put("handIdx", currAction.getHandIdx());
      return true;
   }
   /**
    * ditto
    */
   public boolean playerError(final ObjectNode temp, final ActionsInput currAction) {
      temp.put("command", currAction.getCommand());
      temp.put("playerIdx", currAction.getPlayerIdx());
      return true;
   }
   /**
    * used to replace those * 3 * lines in the code with only one
    * a bit of tidying
    */
   public boolean attackError(final ObjectNode temp, final ActionsInput currAction) {
      temp.putPOJO("cardAttacked", currAction.getCardAttacked());
      temp.putPOJO("cardAttacker", currAction.getCardAttacker());
      temp.put("command", currAction.getCommand());
      return true;
   }
}
