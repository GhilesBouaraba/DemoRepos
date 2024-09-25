package bakery;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import java.util.List;
import java.io.Serializable;

/**
 * This is the class that manages everything to do with the players
 * @author Ghiles
 * @version 1.0
 */
public class Player implements Serializable{
    private List<Ingredient> hand = new ArrayList<>();
    private String name;
    private boolean turnTaken = false;
    private int actionsRemaining;
    private static final long serialVersionUID = 0;

    /**
     * Constructor for the player class
     * @param nameIn the name of the player
     */
    public Player(String nameIn) {
        name = nameIn;
    }

    /**
     * Set method for boolean variable that checks whether or not the turn has been taken
     * @param in the boolean variable to set 
     */
    public void setTurnTaken(boolean in) {
        turnTaken = in;
    }

    /**
     * The set method for the actions remaining
     * @param in the number of actions
     */
    public void setActionsRemaining(int in) {
        actionsRemaining = in;
    }

    /**
     * The get method for turn taken
     * @return boolean var
     */
    public boolean getTurnTaken() {
        return turnTaken;
    }

    /**
     * yuh
     * @return something
     */
    public int getActionsLeft() {
        return actionsRemaining;
    }
    /**
     * Allows the game to add ingredients to the player's hand
     * @param ingredients list of ingredients to be added to the player's hand
     */
    public void addToHand(List<Ingredient> ingredients) {
        for (Ingredient currentIngredient : ingredients) {
            hand.add(currentIngredient);
        }
    }

    /**
     * Adds a single ingredient to the player's hand
     * @param ingredient the ingredient to be added to the player's hand
     */
    public void addToHand(Ingredient ingredient) {
        hand.add(ingredient);
    }

    /**
     * Checks whether or not the player has a specific ingredient in their hand
     * @param ingredient the ingredient to be searched for
     * @return the boolean representing whether or not the player has that ingredient
     */
    public boolean hasIngredient(Ingredient ingredient) {
        boolean ghilouChecker = false;
        for (Ingredient currentIngredient : hand) {
            if (currentIngredient == ingredient) {
                ghilouChecker = true;
                return ghilouChecker;
            }
        }
        return ghilouChecker;
    }

    /**
     * Removes the specified ingredient from the hand
     * @param ingredient the ingredient to be removed
     * @throws WrongIngredientsException thrown when the given ingredient isnt in the hand
     */
    public void removeFromHand(Ingredient ingredient) throws WrongIngredientsException{
        Ingredient ingToRemove = null;
        for (Ingredient i : hand) {
            if (i.toString().equals(ingredient.toString())) {
                ingToRemove = i;
                break;
            }
        }
        if (ingToRemove != null) {
            hand.remove(ingToRemove);
        }
        else {
            throw new WrongIngredientsException("YE");
        }


        // Redundant code?
        // int desiredIndex = -1;
        // for (Ingredient currentIngredient : hand) {
        //     if (currentIngredient.toString().equals(ingredient.toString())) {
        //         desiredIndex = hand.indexOf(currentIngredient);
        //         break;
        //     }
        // }
        // if (desiredIndex == -1) {
        //     throw new WrongIngredientsException("");
        // }
        // hand.remove(desiredIndex);
    }

    /**
     * returns the player's hand as a list of ingredients
     * @return the player's hand
     */
    public List<Ingredient> getHand() {
        return hand;
    }

    /**
     * returns the player's hand as a single string with all the ingredients separated by string
     * @return the return string
     */
    public String getHandStr() {
        String returnString = "";
        if (hand.isEmpty()) {
            return "";
        }
        List<String> tempArray = new ArrayList<>();
        for (Ingredient currentIngredient : hand) {
            int currentIngredientCount = 0;
            for (Ingredient currentIng : hand) {
                if (currentIngredient.toString() == currentIng.toString()) {
                    currentIngredientCount += 1;
                }
            }

            if (currentIngredientCount == 1) {
                String currentIngredientName = currentIngredient.toString();
                String currentUpperName = currentIngredientName.substring(0, 1).toUpperCase() + currentIngredientName.substring(1);
                tempArray.add(currentUpperName);
                
                //returnString += currentUpperName + ", ";
            }
            if (currentIngredientCount > 1) {
                String currentIngredientName = currentIngredient.toString();
                String currentUpperName = currentIngredientName.substring(0, 1).toUpperCase() + currentIngredientName.substring(1);
                tempArray.add(currentUpperName + " (x" + currentIngredientCount + ")");
            }
        }
        List<String> tempArr = new ArrayList<>();
        for (String currentStr : tempArray) {
            //System.out.println(currentStr);
            boolean alreadyChecked = false;
            for (String current : tempArray) {
                //System.out.println(current);
                //System.out.println(alreadyChecked);
                if (tempArr.contains(current)) {
                    continue;
                }
                else if (current == currentStr) {
                    alreadyChecked = true;
                    tempArr.add(current);
                }
            }
        }
        Collections.sort(tempArr, Comparator.comparing(s -> s.substring(0,1)));
        //System.out.println("");
        //System.out.println("");
        for (String currentStr : tempArr) {
            //System.out.println(currentStr);
            returnString += currentStr + ", ";
        }
        returnString.trim();
        returnString = returnString.substring(0, returnString.length() - 2);
        return returnString;
    }

    /**
     * Returns the player's name
     * @return the player's name as a string
     */
    public String toString() {
        return name;
    }
}