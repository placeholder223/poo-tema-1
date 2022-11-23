package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import fileio.GameInput;
import fileio.StartGameInput;

import java.util.ArrayList;

// name is from Yu-Gi-Oh
// it means "once per turn"
public class IciTarnIciDo {

   public IciTarnIciDo() {

   }
   // schimba temp put command si handidx cu o functie, si la fel cu put command si playeridx

   /**
    * this means "My Turn"
    */
   public void oreNoTurn(final GameInput currGame, final StartGameInput currSettings,
                         final int secondPlayer, final ObjectMapper objectMapper,
                         final ArrayNode output, final ExtraDataGame extraData,
                         final int whichTurn, final Board board, final HowManyWins numWins,
                         final int currentNumber) {
      int tarnEndo = 0; //Yu-Gi-Oh Reference
      int currentPlayer;
      if (whichTurn == 1) {
         currentPlayer = currSettings.getStartingPlayer();
      } else {
         currentPlayer = secondPlayer;
      }
      ThrowError errors = new ThrowError();
      do {
         boolean needsChange = false;
         ObjectNode temp = objectMapper.createObjectNode();
         ActionsInput currAction = currGame.getActions().get(extraData.actionIndex);
         switch (currAction.getCommand()) {
            case "endPlayerTurn" -> {
               tarnEndo = 1;
            }

            case "getPlayerDeck" -> {
               needsChange = errors.playerError(temp, currAction);
               if (currAction.getPlayerIdx() == 1) {
                  ArrayList<Object> tempDeck = new ArrayList<>(extraData.deckOne);
                  temp.putPOJO("output", tempDeck);
               } else {
                  ArrayList<Object> tempDeck = new ArrayList<>(extraData.deckTwo);
                  temp.putPOJO("output", tempDeck);
               }
            }

            case "getPlayerHero" -> {
               needsChange = errors.playerError(temp, currAction);
               if (currAction.getPlayerIdx() == 1) {
                  BetterHero tempHero = new BetterHero(extraData.heroPlayerOne);
                  temp.putPOJO("output", tempHero);
               } else {
                  BetterHero tempHero = new BetterHero(extraData.heroPlayerTwo);
                  temp.putPOJO("output", tempHero);
               }
            }

            case "getPlayerTurn" -> {
               temp.put("command", currAction.getCommand());
               temp.put("output", currentPlayer);
               needsChange = true;
               // pt jucatorul de dupa
            }

            case "getCardsInHand" -> {
               needsChange = errors.playerError(temp, currAction);
               if (currAction.getPlayerIdx() == 1) {
                  ArrayList<Object> copyForPOJO = new ArrayList<>();
                  for (int i = 0; i < extraData.handPlayerOne.size(); i++) {
                     if (extraData.handPlayerOne.get(i) instanceof BetterEnvironment) {
                        BetterEnvironment aux = new BetterEnvironment(
                              (BetterEnvironment) extraData.handPlayerOne.get(i));
                        copyForPOJO.add(aux);
                     } else {
                        BetterMinion aux = new BetterMinion((BetterMinion)
                              extraData.handPlayerOne.get(i));
                        copyForPOJO.add(aux);
                     }
                  }
                  temp.putPOJO("output", copyForPOJO);
               } else {
                  ArrayList<Object> copyForPOJO = new ArrayList<>();
                  for (int i = 0; i < extraData.handPlayerTwo.size(); i++) {
                     if (extraData.handPlayerTwo.get(i) instanceof BetterEnvironment) {
                        BetterEnvironment aux = new BetterEnvironment(
                              (BetterEnvironment) extraData.handPlayerTwo.get(i));
                        copyForPOJO.add(aux);
                     } else {
                        BetterMinion aux = new BetterMinion((BetterMinion)
                              extraData.handPlayerTwo.get(i));
                        copyForPOJO.add(aux);
                     }
                  }
                  temp.putPOJO("output", copyForPOJO);
               }
            }

            case "placeCard" -> {
               if (currentPlayer == 1) {
                  if (currAction.getHandIdx() >= extraData.handPlayerOne.size()) {
                     temp.put("output", currAction.getHandIdx());
                     needsChange = true;
                     break;
                  }
                  if (extraData.handPlayerOne.get(currAction.getHandIdx())
                        instanceof BetterEnvironment) {
                     temp.put("error", "Cannot place environment card on table.");
                     needsChange = errors.handError(temp, currAction);
                  } else {
                     if (((BetterMinion) extraData.handPlayerOne.get(
                           currAction.getHandIdx())).getMana()
                           > extraData.manaPlayerOne) {
                        needsChange = errors.handError(temp, currAction);
                        temp.put("error",
                              "Not enough mana to place card on table.");
                     } else {
                        if (board.setGrid((BetterMinion) extraData.handPlayerOne.get(
                              currAction.getHandIdx()), 1) == 0) {
                           extraData.manaPlayerOne
                                 -= ((BetterMinion) extraData.handPlayerOne.get(
                                 currAction.getHandIdx())).getMana();
                           extraData.handPlayerOne.remove(currAction.getHandIdx());
                        } else {
                           needsChange = errors.handError(temp, currAction);
                           temp.put("error", "Cannot place card "
                                 + "on table since row is full.");
                        }
                     }

                  }
               } else {
                  if (currAction.getHandIdx() >= extraData.handPlayerTwo.size()) {
                     temp.put("output", currAction.getHandIdx());
                     needsChange = true;
                     break;
                  }
                  if (extraData.handPlayerTwo.get(currAction.getHandIdx())
                        instanceof BetterEnvironment) {
                     temp.put("error", "Cannot place environment card on table.");
                     needsChange = errors.handError(temp, currAction);
                  } else {
                     if (((BetterMinion) extraData.handPlayerTwo.get(
                           currAction.getHandIdx())).getMana()
                           > extraData.manaPlayerTwo) {
                        needsChange = errors.handError(temp, currAction);
                        temp.put("error",
                              "Not enough mana to place card on table.");
                     } else {
                        if (board.setGrid((BetterMinion) extraData.handPlayerTwo.get(
                              currAction.getHandIdx()), 2) == 0) {
                           extraData.manaPlayerTwo
                                 -= ((BetterMinion) extraData.handPlayerTwo.get(
                                 currAction.getHandIdx())).getMana();
                           extraData.handPlayerTwo.remove(currAction.getHandIdx());
                        } else {
                           needsChange = errors.handError(temp, currAction);
                           temp.put("error", "Cannot place card "
                                 + "on table since row is full.");
                        }
                     }

                  }
               }
            }
            case "getPlayerMana" -> {
               temp.put("command", currAction.getCommand());
               needsChange = true;
               if (currAction.getPlayerIdx() == 1) {
                  temp.put("output", extraData.manaPlayerOne);
               } else {
                  temp.put("output", extraData.manaPlayerTwo);
               }
               temp.put("playerIdx", currAction.getPlayerIdx());
            }

            case "getCardsOnTable" -> {

               temp.put("command", currAction.getCommand());
               needsChange = true;
               Board proxy = new Board(board);
               ArrayList<ArrayList<BetterMinion>> tableCards = new ArrayList<>();
               for (int i = 0; i < Board.ROW_NUMBERS; i++) {
                  tableCards.add(proxy.getRow(i));
               }
               temp.putPOJO("output", tableCards);
            }

            case "getEnvironmentCardsInHand" -> {
               temp.put("command", currAction.getCommand());

               needsChange = true;
               if (currAction.getPlayerIdx() == 1) {
                  ArrayList<BetterEnvironment> copyForPOJO = new ArrayList<>();
                  for (int i = 0; i < extraData.handPlayerOne.size(); i++) {
                     if (extraData.handPlayerOne.get(i) instanceof BetterEnvironment) {
                        BetterEnvironment aux = new BetterEnvironment(
                              (BetterEnvironment) extraData.handPlayerOne.get(i));
                        copyForPOJO.add(aux);
                     }
                  }
                  temp.putPOJO("output", copyForPOJO);
               } else {
                  ArrayList<BetterEnvironment> copyForPOJO = new ArrayList<>();
                  for (int i = 0; i < extraData.handPlayerTwo.size(); i++) {
                     if (extraData.handPlayerTwo.get(i) instanceof BetterEnvironment) {
                        BetterEnvironment aux = new BetterEnvironment(
                              (BetterEnvironment) extraData.handPlayerTwo.get(i));
                        copyForPOJO.add(aux);
                     }
                  }
                  temp.putPOJO("output", copyForPOJO);
               }
               temp.put("playerIdx", currAction.getPlayerIdx());
            }

            case "getCardAtPosition" -> {
               temp.put("command", currAction.getCommand());
               temp.put("x", currAction.getX());
               temp.put("y", currAction.getY());
               needsChange = true;

               if (board.getGridSquare(currAction.getX(),
                     currAction.getY()) != null) {
                  BetterMinion tempMinion =
                        new BetterMinion(board.getGridSquare(currAction.getX(),
                              currAction.getY()));
                  temp.putPOJO("output", tempMinion);
               } else {
                  temp.putPOJO("output", "No card available at that position.");
               }
            }

            case "useEnvironmentCard" -> {

               if (currentPlayer == 1) {
                  if (!(extraData.handPlayerOne.get(currAction.getHandIdx())
                        instanceof BetterEnvironment)) {
                     needsChange = errors.handError(temp, currAction);
                     temp.put("affectedRow", currAction.getAffectedRow());
                     temp.put("error", "Chosen card is not of type environment.");
                     break;
                  }
                  if (((BetterEnvironment)
                        extraData.handPlayerOne.get(currAction.getHandIdx())).getMana()
                        > extraData.manaPlayerOne) {
                     needsChange = errors.handError(temp, currAction);
                     temp.put("affectedRow", currAction.getAffectedRow());
                     temp.put("error", "Not enough mana to use environment card.");
                     break;
                  }
                  if (currAction.getAffectedRow() == Board.NOT_MAGIC_ROW2
                        || currAction.getAffectedRow() == Board.NOT_MAGIC_ROW3) {
                     needsChange = errors.handError(temp, currAction);
                     temp.put("affectedRow", currAction.getAffectedRow());
                     temp.put("error", "Chosen row does not belong to the enemy.");
                     break;
                  }
               } else {

                  if (!(extraData.handPlayerTwo.get(currAction.getHandIdx())
                        instanceof BetterEnvironment)) {
                     needsChange = errors.handError(temp, currAction);
                     temp.put("affectedRow", currAction.getAffectedRow());
                     temp.put("error", "Chosen card is not of type environment.");

                     break;
                  }
                  if (((BetterEnvironment)
                        extraData.handPlayerTwo.get(currAction.getHandIdx())).getMana()
                        > extraData.manaPlayerTwo) {
                     //eroare
                     needsChange = errors.handError(temp, currAction);
                     temp.put("affectedRow", currAction.getAffectedRow());
                     temp.put("error", "Not enough mana to use environment card.");
                     break;
                  }
                  if (currAction.getAffectedRow() == Board.NOT_MAGIC_ROW1
                        || currAction.getAffectedRow() == Board.NOT_MAGIC_ROW0) {
                     needsChange = errors.handError(temp, currAction);
                     temp.put("affectedRow", currAction.getAffectedRow());
                     temp.put("error", "Chosen row does not belong to the enemy.");
                     break;
                  }
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

                        needsChange = errors.handError(temp, currAction);
                        temp.put("affectedRow", currAction.getAffectedRow());
                        temp.put("error",
                              "Cannot steal enemy card since the player's row is full.");
                        break;
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
               if (needsChange) {
                  break;
               }
               if (currentPlayer == 1) {
                  extraData.manaPlayerOne -= ((BetterEnvironment)
                        extraData.handPlayerOne.get(currAction.getHandIdx())).getMana();
                  extraData.handPlayerOne.remove(currAction.getHandIdx());
               } else {
                  extraData.manaPlayerTwo -= ((BetterEnvironment)
                        extraData.handPlayerTwo.get(currAction.getHandIdx())).getMana();
                  extraData.handPlayerTwo.remove(currAction.getHandIdx());
               }
            }

            case "getFrozenCardsOnTable" -> {
               temp.put("command", currAction.getCommand());
               ArrayList<BetterMinion> allFrozen = new ArrayList<>();
               needsChange = true;
               for (int i = 0; i < Board.ROW_NUMBERS; i++) {
                  for (int j = 0; j < board.getGrid().get(i).size(); j++) {
                     if (board.getGridSquare(i, j).getFrozen()) {
                        BetterMinion tempMinion =
                              new BetterMinion(board.getGridSquare(i, j));
                        allFrozen.add(tempMinion);
                     }
                  }
               }
               temp.putPOJO("output", allFrozen);
            }

            case "cardUsesAttack" -> {
               int attackedX = currAction.getCardAttacked().getX();
               int attackedY = currAction.getCardAttacked().getY();
               int attackerX = currAction.getCardAttacker().getX();
               int attackerY = currAction.getCardAttacker().getY();
               if (board.getGridSquare(attackerX, attackerY).getFrozen()) {
                  needsChange = errors.attackError(temp, currAction);
                  temp.put("command", currAction.getCommand());
                  temp.put("error", "Attacker card is frozen.");
                  break;
               }
               if (board.getGridSquare(attackerX, attackerY).getAttackStatus()) {
                  needsChange = errors.attackError(temp, currAction);
                  temp.put("command", currAction.getCommand());
                  temp.put("error", "Attacker card has already attacked this turn.");
                  break;
               }
               if (board.getTankStatus(attackedX)) {
                  if (!board.getGridSquare(attackedX, attackedY).getTank()) {
                     needsChange = errors.attackError(temp, currAction);
                     temp.put("command", currAction.getCommand());
                     temp.put("error", "Attacked card is not of type 'Tank'.");
                     break;
                  }

               }
               if (attackedX == Board.ROW_NUMBERS - 2 * currentPlayer
                     || attackedX == Board.ROW_NUMBERS
                     - 2 * currentPlayer + 1) {
                  needsChange = errors.attackError(temp, currAction);
                  temp.put("command", currAction.getCommand());
                  temp.put("error", "Attacked card does not belong to the enemy.");
                  break;
               }
               board.getGridSquare(attackedX, attackedY).setHealth(
                     board.getGridSquare(attackedX, attackedY).getHealth()
                           - board.getGridSquare(attackerX, attackerY).getAttackDamage());
               board.getGridSquare(attackerX, attackerY).setAttackStatus(true);
               if (board.getGridSquare(attackedX, attackedY).getHealth() <= 0) {
                  board.removeFromGrid(attackedX, attackedY);
               }
            }

            case "cardUsesAbility" -> {

               int affectedX = currAction.getCardAttacked().getX();
               int affectedY = currAction.getCardAttacked().getY();
               int affecterX = currAction.getCardAttacker().getX();
               int affecterY = currAction.getCardAttacker().getY();
               if (board.getGridSquare(affecterX, affecterY).getFrozen()) {
                  needsChange = errors.attackError(temp, currAction);
                  temp.put("command", currAction.getCommand());
                  temp.put("error", "Attacker card is frozen.");
                  break;
               }
               if (board.getGridSquare(affecterX, affecterY).getAttackStatus()) {
                  needsChange = errors.attackError(temp, currAction);
                  temp.put("command", currAction.getCommand());
                  temp.put("error", "Attacker card has already attacked this turn.");
                  break;
               }
               if (board.getTankStatus(affectedX)
                     && board.getGridSquare(
                     affecterX, affecterY).getName().compareTo("Disciple") != 0) {
                  if (!board.getGridSquare(affectedX, affectedY).getTank()) {
                     needsChange = errors.attackError(temp, currAction);
                     temp.put("command", currAction.getCommand());
                     temp.put("error", "Attacked card is not of type 'Tank'.");
                     break;
                  }
               }
               if (board.getGridSquare(affecterX,
                     affecterY).getName().compareTo("Disciple") != 0) {
                  if (affectedX == (Board.ROW_NUMBERS
                        - 2 * (currentPlayer))
                        || affectedX == Board.ROW_NUMBERS
                        - 2 * (currentPlayer) + 1) {
                     needsChange = errors.attackError(temp, currAction);
                     temp.put("command", currAction.getCommand());
                     temp.put("error", "Attacked card does not belong to the enemy.");
                     break;
                  }
               } else {
                  if (affectedX != (Board.ROW_NUMBERS
                        - 2 * (currentPlayer))
                        && affectedX != Board.ROW_NUMBERS
                        - 2 * (currentPlayer) + 1) {
                     needsChange = errors.attackError(temp, currAction);
                     temp.put("command", currAction.getCommand());
                     temp.put("error", "Attacked card does not belong to the current "
                           + "player.");
                     break;
                  }
               }
               switch (board.getGridSquare(affecterX, affecterY).getName()) {
                  case "The Ripper" -> {
                     board.getGridSquare(affectedX, affectedY).setAttackDamage(
                           board.getGridSquare(affectedX, affectedY).getAttackDamage() - 2
                     );
                     if (board.getGridSquare(affectedX, affectedY).getAttackDamage() < 0) {
                        board.getGridSquare(affectedX, affectedY).setAttackDamage(0);
                     }
                  }
                  case "Miraj" -> {
                     int aux = board.getGridSquare(affecterX, affecterY).getHealth();
                     board.getGridSquare(affecterX, affecterY).setHealth(
                           board.getGridSquare(affectedX, affectedY).getHealth()
                     );
                     board.getGridSquare(affectedX, affectedY).setHealth(aux);
                  }
                  case "The Cursed One" -> {
                     int aux = board.getGridSquare(affectedX, affectedY).getHealth();
                     board.getGridSquare(affectedX, affectedY).setHealth(
                           board.getGridSquare(affectedX, affectedY).getAttackDamage()
                     );
                     board.getGridSquare(affectedX, affectedY).setAttackDamage(aux);
                     if (board.getGridSquare(affectedX, affectedY).getHealth() == 0) {
                        board.removeFromGrid(affectedX, affectedY);
                     }
                  }
                  case "Disciple" -> board.getGridSquare(affectedX, affectedY).setHealth(
                        board.getGridSquare(affectedX, affectedY).getHealth() + 2
                  );
                  default -> {
                  }
                  //I MUST OBEY TO THE ALLMIGHTY CHECKER
               }

               board.getGridSquare(affecterX, affecterY).setAttackStatus(true);


            }

            case "useAttackHero" -> {
               int attackerX = currAction.getCardAttacker().getX();
               int attackerY = currAction.getCardAttacker().getY();
               if (board.getGridSquare(attackerX, attackerY).getFrozen()) {
                  needsChange = true;
                  temp.putPOJO("cardAttacker", currAction.getCardAttacker());
                  temp.put("command", currAction.getCommand());
                  temp.put("error", "Attacker card is frozen.");
                  break;
               }
               if (board.getGridSquare(attackerX, attackerY).getAttackStatus()) {
                  needsChange = true;
                  temp.putPOJO("cardAttacker", currAction.getCardAttacker());
                  temp.put("command", currAction.getCommand());
                  temp.put("error", "Attacker card has already attacked this turn.");
                  break;
               }
               if (board.getTankStatus(currentPlayer)) {
                  needsChange = true;
                  temp.putPOJO("cardAttacker", currAction.getCardAttacker());
                  temp.put("command", currAction.getCommand());
                  temp.put("error", "Attacked card is not of type 'Tank'.");
                  break;
               }
               board.getGridSquare(attackerX, attackerY).setAttackStatus(true);
               if (currentPlayer == 1) {
                  extraData.heroPlayerTwo.setHealth(extraData.heroPlayerTwo.getHealth()
                        - board.getGridSquare(attackerX, attackerY).getAttackDamage());
                  if (extraData.heroPlayerTwo.getHealth() <= 0) {
                     needsChange = true;
                     temp.put("gameEnded", "Player one killed the enemy hero.");
                     numWins.increaseWinsPlayerOne();
                     extraData.gameOver = 1;
                  }
               } else {
                  extraData.heroPlayerOne.setHealth(extraData.heroPlayerOne.getHealth()
                        - board.getGridSquare(attackerX, attackerY).getAttackDamage());
                  if (extraData.heroPlayerOne.getHealth() <= 0) {
                     extraData.gameOver = 1;
                     needsChange = true;
                     temp.put("gameEnded", "Player two killed the enemy hero.");
                     numWins.increaseWinsPlayerTwo();
                  }
               }

            }

            case "useHeroAbility" -> {
               String heroName;
               if (currentPlayer == 1) {
                  heroName = extraData.heroPlayerOne.getName();
               } else {
                  heroName = extraData.heroPlayerTwo.getName();
               }
               if (currentPlayer == 1) {
                  if (extraData.manaPlayerOne < extraData.heroPlayerOne.getMana()) {
                     needsChange = true;
                     temp.put("affectedRow", currAction.getAffectedRow());
                     temp.put("command", currAction.getCommand());
                     temp.put("error", "Not enough mana to use hero's ability.");
                     break;
                  }
                  if (extraData.heroPlayerOne.getHasAttacked()) {
                     needsChange = true;
                     temp.put("affectedRow", currAction.getAffectedRow());
                     temp.put("command", currAction.getCommand());
                     temp.put("error", "Hero has already attacked this turn.");
                     break;
                  }
               } else {
                  if (extraData.manaPlayerTwo < extraData.heroPlayerTwo.getMana()) {
                     needsChange = true;
                     temp.put("affectedRow", currAction.getAffectedRow());
                     temp.put("command", currAction.getCommand());
                     temp.put("error", "Not enough mana to use hero's ability.");
                     break;
                  }
                  if (extraData.heroPlayerTwo.getHasAttacked()) {
                     needsChange = true;
                     temp.put("affectedRow", currAction.getAffectedRow());
                     temp.put("command", currAction.getCommand());
                     temp.put("error", "Hero has already attacked this turn.");
                     break;
                  }
               }
               switch (heroName) {
                  case "Lord Royce" -> {
                     if (currAction.getAffectedRow() == (Board.ROW_NUMBERS
                           - 2 * (currentPlayer))
                           || currAction.getAffectedRow() == Board.ROW_NUMBERS
                           - 2 * (currentPlayer) + 1) {
                        needsChange = true;
                        temp.put("affectedRow", currAction.getAffectedRow());
                        temp.put("command", currAction.getCommand());
                        temp.put("error", "Selected row does not belong to the enemy.");
                        break;
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
                     }
                     board.getGridSquare(currAction.getAffectedRow(),
                           yMax).setFrozen(true);
                  }
                  case "Empress Thorina" -> {
                     if (currAction.getAffectedRow() == (Board.ROW_NUMBERS
                           - 2 * (currentPlayer))
                           || currAction.getAffectedRow() == Board.ROW_NUMBERS
                           - 2 * (currentPlayer) + 1) {
                        needsChange = true;
                        temp.put("affectedRow", currAction.getAffectedRow());
                        temp.put("command", currAction.getCommand());
                        temp.put("error", "Selected row does not belong to the enemy.");
                        break;
                     }
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
                     board.removeFromGrid((currAction.getAffectedRow()), yMax);
                  }
                  case "King Mudface" -> {
                     if (currAction.getAffectedRow() != (Board.ROW_NUMBERS
                           - 2 * (currentPlayer))
                           && currAction.getAffectedRow() != Board.ROW_NUMBERS
                           - 2 * (currentPlayer) + 1) {
                        needsChange = true;
                        temp.put("affectedRow", currAction.getAffectedRow());
                        temp.put("command", currAction.getCommand());
                        temp.put("error", "Selected row does not belong to the current "
                              + "player.");
                        break;
                     }
                     for (int j = 0;
                          j < board.getRow(currAction.getAffectedRow()).size(); j++) {
                        board.getGridSquare(currAction.getAffectedRow(), j).setHealth(
                              board.getGridSquare(
                                    currAction.getAffectedRow(), j).getHealth() + 1);
                     }
                  }
                  case "General Kocioraw" -> {
                     if (currAction.getAffectedRow() != (Board.ROW_NUMBERS
                           - 2 * (currentPlayer))
                           && currAction.getAffectedRow() != Board.ROW_NUMBERS
                           - 2 * (currentPlayer) + 1) {
                        needsChange = true;
                        temp.put("affectedRow", currAction.getAffectedRow());
                        temp.put("command", currAction.getCommand());
                        temp.put("error", "Selected row does not belong to the current "
                              + "player.");
                        break;
                     }
                     for (int j = 0;
                          j < board.getRow(currAction.getAffectedRow()).size(); j++) {
                        board.getGridSquare(currAction.getAffectedRow(), j).setAttackDamage(
                              board.getGridSquare(
                                    currAction.getAffectedRow(), j).getAttackDamage() + 1);
                     }
                  }
                  default -> {
                     //all hail the allmighty codestyle checker
                  }

               }
               if (needsChange) {
                  break;
               }
               if (currentPlayer == 1) {
                  extraData.manaPlayerOne -= extraData.heroPlayerOne.getMana();
                  extraData.heroPlayerOne.setHasAttacked(true);
               } else {
                  extraData.manaPlayerTwo -= extraData.heroPlayerTwo.getMana();
                  extraData.heroPlayerTwo.setHasAttacked(true);
               }
            }
            case "getPlayerOneWins" -> {
               needsChange = true;
               temp.put("command", currAction.getCommand());
               temp.put("output", numWins.getWinsPlayerOne());
            }

            case "getPlayerTwoWins" -> {
               needsChange = true;
               temp.put("command", currAction.getCommand());
               temp.put("output", numWins.getWinsPlayerTwo());
            }
            case "getTotalGamesPlayed" -> {
               needsChange = true;
               temp.put("command", currAction.getCommand());
               temp.put("output", currentNumber + 1);
            }

            default -> {
            }
         }
         extraData.actionIndex++;
         if (extraData.actionIndex >= currGame.getActions().size()) {
            tarnEndo = 1;
            extraData.gameOver = 1;
         }
         if (needsChange) {
            output.add(temp);
         }
      } while (tarnEndo == 0);

   }
}



