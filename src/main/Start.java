package main;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import fileio.CardInput;
import fileio.GameInput;
import fileio.Input;
import fileio.StartGameInput;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Start {


   /**
    * The implementation of the game
    */
   public void stepItUp(final Input inputData, final ObjectMapper objectMapper,
                        final ArrayNode output) {
      objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); // ignores null fields
      int numOfGames = inputData.getGames().size();
      HowManyWins numWins = new HowManyWins();
      for (int currentNumber = 0; currentNumber < numOfGames; currentNumber++) {
         GameInput currGame = inputData.getGames().get(currentNumber); // the actions of the game
         // mostly
         ExtraDataGame extraData = new ExtraDataGame();
         StartGameInput currSettings = currGame.getStartGame(); // the starting data of the game
         // such as the first player and the decks
         int secondPlayer = ExtraDataGame.FUNKY_TURN_MATH - currSettings.getStartingPlayer();
         ArrayList<CardInput> proxyDeck = inputData.getPlayerOneDecks().getDecks().get(
               currSettings.getPlayerOneDeckIdx());
         extraData.deckOne = new ArrayList<>();
         for (CardInput cardInput : proxyDeck) {
            if (cardInput.getName().compareTo("Firestorm") == 0
                  || cardInput.getName().compareTo("Winterfell") == 0
                  || cardInput.getName().compareTo("Heart Hound") == 0) {
               BetterEnvironment tempCard = new BetterEnvironment(cardInput);
               extraData.deckOne.add(tempCard);
            } else {
               BetterMinion tempCard = new BetterMinion(cardInput);
               tempCard.setInFront(tempCard.getName().compareTo("The Ripper") == 0
                     || tempCard.getName().compareTo("Goliath") == 0
                     || tempCard.getName().compareTo("Warden") == 0
                     || tempCard.getName().compareTo("Miraj") == 0);
               tempCard.setTank(tempCard.getName().compareTo("Goliath") == 0
                     || tempCard.getName().compareTo("Warden") == 0);
               extraData.deckOne.add(tempCard);
            }
         }
         proxyDeck = inputData.getPlayerTwoDecks().getDecks().get(
               currSettings.getPlayerTwoDeckIdx());
         extraData.deckTwo = new ArrayList<>();
         for (CardInput cardInput : proxyDeck) {
            if (cardInput.getName().compareTo("Firestorm") == 0
                  || cardInput.getName().compareTo("Winterfell") == 0
                  || cardInput.getName().compareTo("Heart Hound") == 0) {
               BetterEnvironment tempCard = new BetterEnvironment(cardInput);
               extraData.deckTwo.add(tempCard);
            } else {
               BetterMinion tempCard = new BetterMinion(cardInput);
               tempCard.setInFront(tempCard.getName().compareTo("The Ripper") == 0
                     || tempCard.getName().compareTo("Goliath") == 0
                     || tempCard.getName().compareTo("Warden") == 0
                     || tempCard.getName().compareTo("Miraj") == 0);
               tempCard.setTank(tempCard.getName().compareTo("Goliath") == 0
                     || tempCard.getName().compareTo("Warden") == 0);
               extraData.deckTwo.add(tempCard);
            }
         }
         Collections.shuffle(extraData.deckOne, new Random(currSettings.getShuffleSeed()));
         Collections.shuffle(extraData.deckTwo, new Random(currSettings.getShuffleSeed()));
         //converts the heroes to my class and also sets their health
         extraData.heroPlayerOne = new BetterHero(currSettings.getPlayerOneHero());
         extraData.heroPlayerTwo = new BetterHero(currSettings.getPlayerTwoHero());
         extraData.heroPlayerOne.setHealth(ExtraDataGame.HERO_STARTING_HEALTH);
         extraData.heroPlayerTwo.setHealth(ExtraDataGame.HERO_STARTING_HEALTH);
         Board board = new Board();
         do {
            if (extraData.givenMana < ExtraDataGame.MAX_MANA) {
               extraData.givenMana++;
            }
            extraData.setManaPlayerOne(extraData.getManaPlayerOne() + extraData.givenMana);
            extraData.setManaPlayerTwo(extraData.getManaPlayerTwo() + extraData.givenMana);
            if (extraData.deckOne.size() > 0) {

               extraData.handPlayerOne.add(extraData.deckOne.get(0));
               extraData.deckOne.remove(0);
            }
            if (extraData.deckTwo.size() > 0) {
               extraData.handPlayerTwo.add(extraData.deckTwo.get(0));
               extraData.deckTwo.remove(0);
            }
            IciTarnIciDo turnFunction = new IciTarnIciDo();
            turnFunction.oreNoTurn(currGame, currSettings, secondPlayer, objectMapper, output,
                  extraData, 1, board, numWins, currentNumber);
            if (extraData.getGameOver() == 1) {
               break;
            }
            board.unfreezeAll(currSettings.getStartingPlayer());
            board.resetAttackAndAbilities(currSettings.getStartingPlayer());

            if (currSettings.getStartingPlayer() == 1) {
               extraData.heroPlayerOne.setHasAttacked(false);
            } else {
               extraData.heroPlayerTwo.setHasAttacked((false));
            }
            turnFunction.oreNoTurn(currGame, currSettings, secondPlayer, objectMapper, output,
                  extraData, 2, board, numWins, currentNumber);
            board.unfreezeAll(secondPlayer);
            board.resetAttackAndAbilities(secondPlayer);

            if (currSettings.getStartingPlayer() == 2) {
               extraData.heroPlayerOne.setHasAttacked(false);
            } else {
               extraData.heroPlayerTwo.setHasAttacked((false));
            }
            if (extraData.actionIndex >= currGame.getActions().size()) {
               extraData.setGameOver(1);
            }
         } while (extraData.getGameOver() == 0);
      }
   }
}
