package main;

import checker.Checker;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import checker.CheckerConstants;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CardInput;
import fileio.GameInput;
import fileio.Input;
import fileio.StartGameInput;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;

public class Start {

    public void StepItUp (Input inputData, ObjectMapper object_Mapper, ArrayNode output)
    {
        int NumOfGames = inputData.getGames ().size ();
        for (int CurrentNumber = 0; CurrentNumber < NumOfGames; CurrentNumber++) {
            GameInput CurrGame = inputData.getGames ().get (CurrentNumber);
            StartGameInput CurrStart = CurrGame.getStartGame ();
            CurrStart.getPlayerOneHero ().setHealth (30);
            CurrStart.getPlayerTwoHero ().setHealth (30);
            ArrayList<CardInput> DeckOne = inputData.getPlayerOneDecks ().getDecks ().get (CurrStart.getPlayerOneDeckIdx ());
            ArrayList<CardInput> DeckTwo = inputData.getPlayerTwoDecks ().getDecks ().get (CurrStart.getPlayerTwoDeckIdx ());
            Collections.shuffle (DeckOne, new Random (CurrStart.getShuffleSeed ()));
            Collections.shuffle (DeckTwo, new Random (CurrStart.getShuffleSeed ()));
            int ManaPlayerOne = 0;
            int ManaPlayerTwo = 0;
            int GameOver = 1;
            int GivenMana = 0;
            do{
                if(GivenMana < 10)
                    GivenMana++;
                ManaPlayerOne+=GivenMana;
                ManaPlayerTwo+=GivenMana;
                ObjectNode temp = object_Mapper.createObjectNode();
                temp.put("hatz","gionuleee");
                //output.add ();
                output.add (temp);
                temp.put("bai","coi");
                //output.add ();
                //output.add (temp);

                //((ObjectNode) output).put("hatz","gionuleee");
                GameOver = 1;

            }while(GameOver == 0 );
        }
    }
}
