package main;

import checker.Checker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import checker.CheckerConstants;
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



public class Board{
    private CardInput[][] grid = new CardInput [4][5];
    private int[] rowSizes = {0,0,0,0};
    private void addToGrid( CardInput Card, int row){
        if( this.getRowSize (row) == 5) {
            //mesaj eroare
            return;
        }
        grid[row][rowSizes[row]] = Card;
        rowSizes[row]++;


    }
    public void setGrid(CardInput Card, int player){
        if(player == 1){
            if( Card.getName ().compareTo ("The Ripper") == 0 ||
                    Card.getName ().compareTo ("Miraj") == 0 ||
                    Card.getName ().compareTo ("Goliath") == 0 ||
                    Card.getName ().compareTo ("Warden") == 0
            )
                addToGrid (Card, 2);
            else
                addToGrid (Card, 3);
            return;
        }
        if( Card.getName ().compareTo ("The Ripper") == 0 ||
                Card.getName ().compareTo ("Miraj") == 0 ||
                Card.getName ().compareTo ("Goliath") == 0 ||
                Card.getName ().compareTo ("Warden") == 0
        )
            addToGrid (Card, 1);
        else
            addToGrid (Card, 0);

    }
    public int getRowSize(int number)
    {
        return rowSizes[number];
    }
    public void decRowSize(int number){
        rowSizes[number]--;
    }
    public void removeFromGrid(int row, int column){
        int i;
        for(i = column;i<rowSizes[row]-1;i++);
        grid[row][i]=grid[row][i+1];
        grid[row][rowSizes[row]-1]=null;
        rowSizes[row]--;
    }
}
