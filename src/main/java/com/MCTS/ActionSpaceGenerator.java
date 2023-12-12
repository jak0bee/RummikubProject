package com.MCTS;

import java.util.ArrayList;
import java.util.List;




public class ActionSpaceGenerator {

    private ArrayList<ArrayList<ArrayList<Integer>>> resultingBoards;
    private ArrayList<ArrayList<Integer>> resultingRacks;
    private ArrayList<Integer> startingBoard;
    private ArrayList<Integer> startingRack;
    private ArrayList<ArrayList<Integer>> allPossibleSets;
    private ArrayList<ArrayList<Integer>> possibleSets;
    private  ArrayList<Integer> availableTilesStart;

    public static void main(String[] args) {
        // Example usage
        ArrayList<Integer> array = new ArrayList<>(List.of(40, 42, 53, 44));
        ArrayList<Integer> set = new ArrayList<>(List.of(40, 53, 42));
        System.out.println(CustomUtility.canCreateSet(array, set));
       // CustomUtility.canCreateSet(array, set)
    }


    public ActionSpaceGenerator(ArrayList<Integer> board, ArrayList<Integer> rack){
        System.out.println("IN Action");
        allPossibleSets= AllSetGenerator.generateAllSets();

        this.resultingBoards = new ArrayList<>();
        this.resultingRacks = new ArrayList<>();
        this.startingBoard = board;
        this.startingRack = rack;
        this.possibleSets = CustomUtility.possibleSets(this.startingRack,this.startingBoard, this.allPossibleSets);
        //probably put this somewhere else
        availableTilesStart = new ArrayList<>();
        for(Integer tile: startingRack){
            availableTilesStart.add(tile);
        }
        for(Integer tile: startingBoard){
            availableTilesStart.add(tile);
        }
    }
    
    public void createAllMoves(ArrayList<ArrayList<Integer>> currentBoard, ArrayList<Integer> availableTiles ,ArrayList<Integer> currentRack, int lastCheckedSet){
        // if all sets have been checked return
        if(lastCheckedSet == this.possibleSets.size()){
            return;
        }

        for(int i = lastCheckedSet ; i < this.possibleSets.size();i++){
            // If cannot create set with tiles go to next one
            if(!CustomUtility.canCreateSet(availableTiles, possibleSets.get(i))){
                continue;
            }
            // Create copy of board so changes in this iteration of the loop are unique to the next iteration
            // Pass this copy as a reference in the recursion
            ArrayList<ArrayList<Integer>> currentBoardCopy = CustomUtility.deepCopy(currentBoard);
            currentBoardCopy.add(this.possibleSets.get(i));
            ArrayList<Integer> currentAvailableTiles = new ArrayList<>(availableTiles);
            CustomUtility.customRemove(currentAvailableTiles, this.possibleSets.get(i));

            // Remove tiles in the set in our rack and available tiles list
            CustomUtility.customRemove(currentRack, this.possibleSets.get(i));
            //now check if the board is valid
            if(CustomUtility.validBoard(currentBoardCopy,this.startingBoard)){
                // TODO: Check results of resulting racks
                ArrayList<Integer> currentValidRack = new ArrayList<>(currentRack);

                // Add board and racks to the results as board is valid
                resultingBoards.add(currentBoardCopy);
                resultingRacks.add(currentValidRack);

            }
            createAllMoves(currentBoardCopy, currentAvailableTiles,currentRack, i);
        }
    }
    public ArrayList<ArrayList<Integer>> getPossibleSets(){
        return this.possibleSets;
    }

    public ArrayList<ArrayList<ArrayList<Integer>>> getResultingBoards() {
        return resultingBoards;
    }
}