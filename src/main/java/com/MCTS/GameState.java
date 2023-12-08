package com.MCTS;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameState {
    //we have to account for draws idk how

    // Represents the current state of the game
    // includes the board and all the hands of the players (predicted)
    // and tiles currently in the pile
    // includes current active player (who can do the next move)
    private ArrayList<Integer>[] racks;
    private ArrayList<Integer> pile;
    private ArrayList<ArrayList<Integer>> board;
    private boolean[] couldntPlay;
    private int winner;
    private Random random;

    public GameState(ArrayList<Integer> rackPlayer1, ArrayList<Integer> rackPlayer2, ArrayList<ArrayList<Integer>>board, ArrayList<Integer> pile){
        this.racks[0] = rackPlayer1;
        this.racks[1] = rackPlayer2;
        this.board = board;
        this.pile = pile;
        this.random = new Random();
        this.couldntPlay[0] = false;
        this.couldntPlay[1] = false;
        this.winner = -1;
    }

    // kinda wonky code but if nothing happened returns a 0,
    // if output is 1 someone won the game
    // if output is 2 the game ended in a draw
    public int updateGameState(ArrayList<ArrayList<Integer>> newBoard, int playerIndex){
        //check if the player whos move it was now has an empty rack
        if(racks[playerIndex].isEmpty()){
            this.winner = playerIndex;
            return 1;
        }
        else if (newBoard.get(0).get(0) == -1){
            //this means that this player could not play so we dont modify the board
            //then we check if there are tiles on the pile, if yes we can simply draw
            if(!this.pile.isEmpty()){
                drawCard(playerIndex);
                //if there is none on the pile however we set that this player coulnt play and dont anything
            } else {
                this.couldntPlay[playerIndex] = true;
                //now if the previous player also could not play the game ends in a draw
                if(this.couldntPlay[(playerIndex+1)%2]){
                    //this means that the current player could not play and the player before it also couldnt play thus its a draw
                    return 2;
                }
            }
        }
        else if (newBoard.equals(this.board)){
            drawCard(playerIndex);
            this.couldntPlay[playerIndex] = false;
        //if the game did not finish or one player did not just draw, then one player played a move and we have to update his
        //rack and the board 
        } else {
            customRemove(this.racks[playerIndex], getDifference(newBoard)); 
            this.couldntPlay[playerIndex] = false;
        }
        return 0;
    }

    public ArrayList<Integer>[] getRacks(){
        return this.racks;
    }

    public ArrayList<ArrayList<Integer>> getBoard(){
        return this.board;
    }

    // -1 if noone won yet
    public int getWinner(){
        return this.winner;
    }

    private void drawCard(int playerIndex){
        int x = this.random.nextInt(this.pile.size());
        Integer tile = Integer.valueOf(this.pile.get(x));
        this.pile.remove(tile);
        this.racks[playerIndex].add(tile);
    }

    //this function takes in a board and gets the difference in tiles from the old one
    private ArrayList<Integer> getDifference(ArrayList<ArrayList<Integer>> newBoard){
        ArrayList<Integer> result = new ArrayList<>();
        ArrayList<Integer> decomposedOld = decompose(this.board);
        ArrayList<Integer> decomposedNew = decompose(newBoard);
        for(Integer tile: decomposedNew){
            if(decomposedOld.contains(tile)){
                result.add(tile);
            }
        }
        return result;
    }

    private ArrayList<Integer> decompose(ArrayList<ArrayList<Integer>> board){
        ArrayList<Integer> result = new ArrayList<>();
        for(ArrayList<Integer> row: board){
            for(Integer tile: row){
                result.add(tile);
            }
        }
        return result;
    }

    private static void customRemove(List<Integer> list, ArrayList<Integer> elementsToRemove) {
        for (Integer element : elementsToRemove) {
            list.remove(element);
        }
    }

}
