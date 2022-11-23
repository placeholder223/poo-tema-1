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
      Outputs messages = new Outputs();
      ThrowError errors = new ThrowError();
      CalculatingBasedOnPlayer calculating = new CalculatingBasedOnPlayer();
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
               // not an error but uses the same message
               needsChange = errors.playerError(temp, currAction);
               ArrayList<Object> copyForPOJO = new ArrayList<>();
               ArrayList<Object> currentHand;
               if (currAction.getPlayerIdx() == 1) {
                  currentHand = new ArrayList<>(extraData.handPlayerOne);
               } else {
                  currentHand = new ArrayList<>(extraData.handPlayerTwo);
               }
               for (Object o : currentHand) {
                  if (o instanceof BetterEnvironment) {
                     BetterEnvironment aux = new BetterEnvironment(
                           (BetterEnvironment) o);
                     copyForPOJO.add(aux);
                  } else {
                     BetterMinion aux = new BetterMinion((BetterMinion)
                           o);
                     copyForPOJO.add(aux);
                  }
               }
               temp.putPOJO("output", copyForPOJO);
            }
            case "placeCard" -> {
               PlaceCardOnTable function = new PlaceCardOnTable();
               needsChange = function.placeCardOnTable(board, extraData,
                     errors, currAction, temp, currentPlayer);
            }
            case "getPlayerMana" -> {
               needsChange = errors.playerError(temp, currAction); //not an error, but useful
               if (currAction.getPlayerIdx() == 1) {
                  temp.put("output", extraData.getManaPlayerOne());
               } else {
                  temp.put("output", extraData.getManaPlayerTwo());
               }
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
               ArrayList<Object> currentHand;
               needsChange = errors.playerError(temp, currAction);
               if (currAction.getPlayerIdx() == 1) {
                  currentHand = new ArrayList<>(extraData.handPlayerOne);
               } else {
                  currentHand = new ArrayList<>(extraData.handPlayerTwo);
               }
               ArrayList<BetterEnvironment> copyForPOJO = new ArrayList<>();
               for (Object o : currentHand) {
                  if (o instanceof BetterEnvironment) {
                     BetterEnvironment aux = new BetterEnvironment(
                           (BetterEnvironment) o);
                     copyForPOJO.add(aux);
                  }
               }
               temp.putPOJO("output", copyForPOJO);
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
               UseEnvironmentCard function = new UseEnvironmentCard();
               needsChange = function.useEnvironmentCard(board, errors, extraData, temp, currAction,
                     currentPlayer, calculating);
            }

            case "getFrozenCardsOnTable" -> {
               temp.put("command", currAction.getCommand());
               ArrayList<BetterMinion> allFrozenCards = new ArrayList<>();
               needsChange = true;
               for (int i = 0; i < Board.ROW_NUMBERS; i++) {
                  for (int j = 0; j < board.getGrid().get(i).size(); j++) {
                     if (board.getGridSquare(i, j).getFrozen()) {
                        BetterMinion tempMinion =
                              new BetterMinion(board.getGridSquare(i, j));
                        allFrozenCards.add(tempMinion);
                     }
                  }
               }
               temp.putPOJO("output", allFrozenCards);
            }

            case "cardUsesAttack" -> {
               CardUsesAttack function = new CardUsesAttack();
               needsChange = function.cardUsesAttackOnCard(currAction, board, errors, temp,
                     currentPlayer);
            }

            case "cardUsesAbility" -> {
               CardUsesAbility function = new CardUsesAbility();
               needsChange = function.cardUsesAbility(board, currAction,
                     temp, errors, calculating, currentPlayer);
            }

            case "useAttackHero" -> {
               CardUsesAttack function = new CardUsesAttack();
               needsChange = function.cardUsesAttackOnHero(currAction, board, temp,
                     currentPlayer, extraData, numWins);
            }

            case "useHeroAbility" -> {
               HeroUseAbility function = new HeroUseAbility();
               needsChange = function.heroUseAbility(board, extraData, errors, temp, currAction,
                     currentPlayer, calculating);
            }
            case "getPlayerOneWins" -> {
               needsChange = messages.getPlayerWins(temp, currAction, numWins, 1);
            }
            case "getPlayerTwoWins" -> {
               needsChange = messages.getPlayerWins(temp, currAction, numWins, 2);
            }
            case "getTotalGamesPlayed" -> {
               needsChange = messages.getGamesPlayed(temp, currAction, currentNumber);
            }
            default -> {
            }
         }
         extraData.actionIndex++;
         if (extraData.actionIndex >= currGame.getActions().size()) {
            tarnEndo = 1;
            extraData.setGameOver(1);
         }
         if (needsChange) {
            output.add(temp);
         }
      } while (tarnEndo == 0);
   }
}



