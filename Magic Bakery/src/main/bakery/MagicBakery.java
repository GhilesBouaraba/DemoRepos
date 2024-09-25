package bakery;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.Collection;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.Random;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Stack;

import bakery.CustomerOrder.CustomerOrderStatus;
import util.ConsoleUtils;
import util.CardUtils;
import util.StringUtils;
import java.io.*;
import java.lang.reflect.Field;

/**
 * This is the class that contains a large chunk of the game logic
 * @author BIG GHILOU
 * @version 1.0
 */
public class MagicBakery implements Serializable{

    /**
     * HELPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP
     */
    public enum ActionType {
        DRAW_INGREDIENT,
        PASS_INGREDIENT,
        BAKE_LAYER,
        FULFIL_ORDER,
        REFRESH_PANTRY
    }



    //ConsoleUtils console = new ConsoleUtils();
    private Customers customers;
    private Collection<Layer> layers = new ArrayList<Layer>();
    private Collection<Player> players = new ArrayList<Player>();
    private Collection<Ingredient> pantry = new ArrayList<Ingredient>();
    private Collection<Ingredient> pantryDeck = new Stack<Ingredient>();
    private Collection<Ingredient> pantryDiscard = new Stack<Ingredient>();
    private Player currentPlayer;
    private Random random;
    private int numPlayers;
    private static final long serialVersionUID = 0;

    /**
     * Constructor
     * @param seed number
     * @param ingredientDeckFile file path to the ingredients 
     * @param layerDeckFile file path to the layers 
     * @throws FileNotFoundException ok
     */
    public MagicBakery(long seed, String ingredientDeckFile, String layerDeckFile) throws FileNotFoundException{
        random = new Random(seed);
        try {
            List<Ingredient> outputFromIngredientsUtils = CardUtils.readIngredientFile(ingredientDeckFile);
            for (Ingredient i : outputFromIngredientsUtils) {
                ((Stack<Ingredient>)pantryDeck).push(i);
            }
        }
        catch (IOException e) {
            throw new FileNotFoundException();
        }
        try {
            List<Layer> outputFromLayersUtils = CardUtils.readLayerFile(layerDeckFile);
            for (Layer l : outputFromLayersUtils) {
                layers.add(l);
            }
        }
        catch (IOException e) {
            throw new FileNotFoundException();
        }
    }

    /**
     * This method idek what it does atp
     * @param playerNames the player names
     * @param customerDeckFile the file path to the customers
     * @throws FileNotFoundException ok
     * @throws IllegalArgumentException ok
     */
    public void startGame(List<String> playerNames, String customerDeckFile) throws FileNotFoundException, IllegalArgumentException{
        numPlayers = playerNames.size();
        if (numPlayers > 5 || numPlayers < 2) {
            throw new IllegalArgumentException();
        }
        //Creates the players
        for (String str : playerNames) {
            Player temp = new Player(str);
            players.add(temp);
        }
        try {
            customers = new Customers(customerDeckFile, random, layers, numPlayers);
        }
        catch (FileNotFoundException e) {
            throw e;
        }
        Collections.shuffle((Stack<Ingredient>)pantryDeck, random);
        for (int i = 0; i < 5; i++) {
            pantry.add(((Stack<Ingredient>)pantryDeck).pop());
        }
        customers.addCustomerOrder();
        if (numPlayers == 3 || numPlayers == 5) {
            customers.addCustomerOrder();
        }
        for (Player current : players) {
            if (numPlayers == 2 || numPlayers == 3) {
                current.setActionsRemaining(3);
            }
            else {
                current.setActionsRemaining(2);
            }
            current.addToHand(((Stack<Ingredient>)pantryDeck).pop());
            current.addToHand(((Stack<Ingredient>)pantryDeck).pop());
            current.addToHand(((Stack<Ingredient>)pantryDeck).pop());

        }
        currentPlayer = ((List<Player>)players).get(0);
    }

    /**
     * Ends the turn/ round
     * @return boolean representing whether round has finished or not
     */
    public boolean endTurn() {
        boolean roundOver = false;
        boolean gameOver = false;
        int currentPlayerIndex = ((List<Player>)players).indexOf(currentPlayer);
        if (currentPlayerIndex == players.size() - 1) {
            roundOver = true;
            currentPlayer = ((List<Player>)players).get(0);
        }
        else {
            currentPlayer = ((List<Player>)players).get(currentPlayerIndex + 1);
        }
        for (Player i : players) {
            if (players.size() == 2 || players.size() == 3) {
                i.setActionsRemaining(3);
            }
            else {
                i.setActionsRemaining(2);
            }
        }
        if (roundOver) {
            try {
                customers.addCustomerOrder();
            }
            catch (EmptyStackException e) {
                return false;
            }
        }        
        
        return true;
    }

    /**
     * Just a extra ghilou method 
     * @return size of the pantry deck
     */
    public int getPantryDeckSize() {
        return pantryDeck.size();
    }

    /**
     * Returns the layers
     * @return collection of the layers
     */
    public Collection<Layer> getLayers() {
        Collection<Layer> returnList = new ArrayList<Layer>();
        for (Layer layer : layers) {
            if (returnList.contains(layer) == false) {
                returnList.add(layer);
            }
        }
        return returnList;
    }

    /**
     * returns whatever the currentPlayer is
     * @return the current player 
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Returns a collection containing all the players
     * @return the collection of all the players
     */
    public Collection<Player> getPlayers() {
        return players;
    }

    /**
     * returns the pantry 
     * @return collection containing all the ingredients in the pantry
     */
    public Collection<Ingredient> getPantry() {
        return pantry;
    }

    /**
     * returns all the customers
     * @return customers
     */
    public Customers getCustomers() {
        return customers;
    }

    /**
     * Returns all the layers that the player can currently bake
     * @return a collection containing all the layers the player can bake with what's in their hand
     */
    public Collection<Layer> getBakeableLayers() {
        Collection<Layer> bakeableLayers = new ArrayList<Layer>();
        Collection<Layer> checkedLayers = new ArrayList<Layer>();
        List<Ingredient> currentPlayerHand = currentPlayer.getHand();
        for (Layer currentLayer : layers) {
            if (!checkedLayers.contains(currentLayer)) {
                boolean output = currentLayer.canBake(currentPlayerHand);
                if (output) {
                    bakeableLayers.add(currentLayer);
                }
            }
            checkedLayers.add(currentLayer);
        }
        return bakeableLayers;
    }

    /**
     * Returns a collection containing all the fulfillable customers with what the player currently has in their hand
     * @return the collection containing all the fulfillable customers
     */
    public Collection<CustomerOrder> getFulfillableCustomers() {
        Collection<CustomerOrder> fulfillable = new ArrayList<CustomerOrder>();
        Collection<CustomerOrder> activeCustomers = customers.getActiveCustomers();
        for (CustomerOrder o : activeCustomers) {
            if (o != null) {
                if (o.canFulfill(currentPlayer.getHand())) {
                    fulfillable.add(o);
                }
            }
        }
        return fulfillable;


        // Collection<CustomerOrder> fulfillableCustomers = new ArrayList<>();
        // Collection<CustomerOrder> activeCustomerOrders = customers.getActiveCustomers();
        // List<Ingredient> currentPlayerHand = currentPlayer.getHand();
        // for (CustomerOrder currentActiveCustomerOrder : activeCustomerOrders) {
        //     List<Ingredient> currentActiveCustomerOrderIngredients = currentActiveCustomerOrder.getRecipe();
        //     boolean ghilouChecker = true;
        //     for (Ingredient currentIngredient : currentActiveCustomerOrderIngredients) {
        //         if (currentPlayerHand.contains(currentIngredient)) {
        //             continue;
        //         }
        //         else {
        //             // HELPFUL_DUCK would probably be implemented around here
        //             ghilouChecker = false;
        //         }
        //     }
        //     if (ghilouChecker) {
        //         fulfillableCustomers.add(currentActiveCustomerOrder);
        //     }
        // }
        // return fulfillableCustomers;
    }

    /**
     * Returns a collection of all the customers that can have their garnishes fulfilled
     * @return the collection mentioned above
     */
    public Collection<CustomerOrder> getGarnishableCustomers() {
        Collection<CustomerOrder> output = customers.getActiveCustomers();
        Collection<CustomerOrder> returnList = new ArrayList<CustomerOrder>();
        List<Ingredient> handCopy = new ArrayList<>(currentPlayer.getHand());
        for (CustomerOrder order : output) {
            if (order.canFulfill(handCopy)) {
                for (Ingredient i : order.getRecipe()) {
                    Ingredient ingToRemove = null;
                    for (Ingredient o : handCopy) {
                        if (o.toString().equals(i.toString())) {
                            ingToRemove = o;
                            break;
                        }
                    }
                    if (ingToRemove != null) {
                        handCopy.remove(ingToRemove);
                    }
                }
                if (order.canGarnish(handCopy)) {
                    returnList.add(order);
                }

            }
        }
        return returnList;

        // Collection<CustomerOrder> output = customers.getActiveCustomers();
        // Collection<CustomerOrder> returnList = new ArrayList<>();
        // for (CustomerOrder currentOrder : output) {
        //     boolean output1 = currentOrder.canGarnish(currentPlayer.getHand());
        //     if (output1) {
        //         returnList.add(currentOrder);
        //     }
        // }
        // return returnList;
    }

    /**
     * Allows the player to bake a layer
     * @param layer the layer to be baked
     * @throws TooManyActionsException thrown when the player has used up all their actions and tries to make a play
     * @throws WrongIngredientsException thrown when the player doesn't have the necessary ingredients to bake the layer
     */
    public void bakeLayer(Layer layer) throws TooManyActionsException, WrongIngredientsException{
        int actionsLeft = currentPlayer.getActionsLeft();
        int next = actionsLeft - 1;
        if (next == 0) {
            currentPlayer.setTurnTaken(true);
        }
        if (actionsLeft > 0) {
            currentPlayer.setActionsRemaining(next);
            boolean bakeable = false;
            bakeable = layer.canBake(currentPlayer.getHand());
            if (bakeable) {
                int noOfMissingElements = 0;
                for (Ingredient i : layer.getRecipe()) {
                    if (currentPlayer.getHand().contains(i)) {
                        continue;
                    }
                    else {
                        noOfMissingElements += 1;
                    }
                }
                //System.out.println(noOfMissingElements);
                for (Ingredient i : layer.getRecipe()) {
                    try {
                        currentPlayer.removeFromHand(i);
                        ((Stack<Ingredient>)pantryDiscard).push(i);
                    }
                    catch (WrongIngredientsException e) {
                        (currentPlayer.getHand()).remove(Ingredient.HELPFUL_DUCK);
                        ((Stack<Ingredient>)pantryDiscard).push(Ingredient.HELPFUL_DUCK);
                    }
                }
                currentPlayer.addToHand(layer);
                layers.remove(layer);
            }
            else {
                currentPlayer.setActionsRemaining(next + 1);

                throw new WrongIngredientsException("Wrong Ingredients");
            }
        }
        else {
            throw new TooManyActionsException();
        }
    }

    
    /**
     * Returns the pantryDeck
     * @return the pantryDeck
     */
    public Collection<Ingredient> getPantryDeck() {
        return pantryDeck;
    }

    /**
     * draws a card from pantry
     * @param ingredient idk im too tired for ts
     * @throws TooManyActionsException cba
     * @throws WrongIngredientsException cba
     */
    public void drawFromPantry(Ingredient ingredient) throws TooManyActionsException, WrongIngredientsException{
        int currentPlayerActionsRemaining = currentPlayer.getActionsLeft();
        int next = currentPlayerActionsRemaining - 1;
        if (next == 0) {
            currentPlayer.setTurnTaken(true);
        }
        boolean checker = false;
        for (Ingredient ing : pantry) {
            if (ing.toString().equals(ingredient.toString())) {
                checker = true;
            }
            else {
                continue;
            }
        }
        if (currentPlayerActionsRemaining > 0) {
            currentPlayer.setActionsRemaining(next);
            if (checker) {
                pantry.remove(ingredient);
                currentPlayer.addToHand(ingredient);
                Ingredient outputIngredient = drawFromPantryDeck();
                pantry.add(outputIngredient);
            }
            else {
                currentPlayer.setActionsRemaining(next + 1);
                throw new WrongIngredientsException("Ingredient not in pantry");
            }
        }
        else {
            throw new TooManyActionsException();
        }
    }

    /**
     * idk cba
     * @param ingredientName the name of the ingredient
     * @throws TooManyActionsException cba
     * @throws WrongIngredientsException cba
     */
    public void drawFromPantry(String ingredientName) throws TooManyActionsException, WrongIngredientsException{
        int numOfActionsLeft = currentPlayer.getActionsLeft();
        int next = numOfActionsLeft - 1;
        if (numOfActionsLeft > 0) {
            currentPlayer.setActionsRemaining(next);

            Ingredient selectedIngredient = null;
            for (Ingredient ingredient : pantry) {
                if (ingredient.toString() == ingredientName) {
                    selectedIngredient = ingredient;
                }
            }
            if (selectedIngredient == null) {
                currentPlayer.setActionsRemaining(next + 1);
                throw new WrongIngredientsException("Ingredient not in pantry");
            }
            pantry.remove(selectedIngredient);
            currentPlayer.addToHand(selectedIngredient);
            Ingredient outputIngredient = drawFromPantryDeck();
            pantry.add(outputIngredient);
        }
        else {
            throw new TooManyActionsException();
        }


    }

    /**
     * This method takes a card from the pantry deck and replaces it
     * @return the ingredient returned
     */
    private Ingredient drawFromPantryDeck() throws EmptyPantryException{
        if (pantryDeck.isEmpty() && pantryDiscard.isEmpty()) {
            throw new EmptyPantryException(null, null);
        }
        if (pantryDeck.isEmpty()) {
            for (Ingredient ing : pantryDiscard) {
                ((Stack<Ingredient>)pantryDeck).push(ing);
            }
            pantryDiscard.clear();
            Collections.shuffle(((Stack<Ingredient>)pantryDeck), random);
        }
        Ingredient output = ((Stack<Ingredient>)pantryDeck).pop();
        return output;       
    }

    /**
     * Removes a card from the current players hand and adds it to the recipient's hand
     * @param ingredient the card to be 'passed'
     * @param recipieint the recipient
     * @throws TooManyActionsException thrown when the actions have been done too many times
     * @throws WrongIngredientsException thrown when the ingredient that was to be passed isnt in the current players hand
     */
    public void passCard(Ingredient ingredient, Player recipieint) throws TooManyActionsException, WrongIngredientsException{
        int numOfActionsLeft = currentPlayer.getActionsLeft();
        int next = numOfActionsLeft - 1;
        if (next == 0) {
            currentPlayer.setTurnTaken(true);
        }
        if (next < 0) {
            currentPlayer.setActionsRemaining(0);
        }
        currentPlayer.setActionsRemaining(next);
        if (numOfActionsLeft > 0) {
            if (currentPlayer.getHand().contains(ingredient)) {
                currentPlayer.removeFromHand(ingredient);
                recipieint.addToHand(ingredient);
            }
            else {
                currentPlayer.setActionsRemaining(next + 1);
                throw new WrongIngredientsException(null);
            }
        }  
        else {
            currentPlayer.setActionsRemaining(next + 1);

            throw new TooManyActionsException();
        }
    }

    /**
     * Discards the pantry and draws a new set of cards
     * @throws TooManyActionsException idk
     */
    public void refreshPantry() throws TooManyActionsException{
        int numOfActionsLeft = currentPlayer.getActionsLeft();
        if (numOfActionsLeft > 0) {
            int next = numOfActionsLeft - 1;
            currentPlayer.setActionsRemaining(next);
            for (Ingredient ing : pantry) {
                ((Stack<Ingredient>)pantryDiscard).push(ing);
            }
            pantry.clear();
            for (int i = 0; i < 5; i++) {
                pantry.add(((Stack<Ingredient>)pantryDeck).pop());
            }
        }
        else {
            throw new TooManyActionsException();
        }
    }

    /**
     * This outputs to console how many customers are fulfilled, given up and garnished
     */
    public void printCustomerServiceRecord() {
        Collection<CustomerOrder> inActiveCustomersCopy = customers.getInactiveCustomers();
        int fulfilled = 0;
        int garnished = 0;
        int givenUp = 0;
        for (CustomerOrder order : inActiveCustomersCopy) {
            if (order.getStatus() == CustomerOrderStatus.FULFILLED || order.getStatus() == CustomerOrderStatus.GARNISHED) {
                fulfilled += 1;
            }
            if (order.getStatus() == CustomerOrderStatus.GARNISHED) {
                garnished += 1;
            }
            if (order.getStatus() == CustomerOrderStatus.GIVEN_UP) {
                givenUp += 1;
            }
        }

        // Collection<CustomerOrder> fulfilledOrders = customers.getInactiveCustomersWithStatus(CustomerOrderStatus.FULFILLED);
        // Collection<CustomerOrder> garnishedOrders = customers.getInactiveCustomersWithStatus(CustomerOrderStatus.GARNISHED);
        // Collection<CustomerOrder> givenUpOrder = customers.getInactiveCustomersWithStatus(CustomerOrderStatus.GIVEN_UP);
        // int fulfilled = fulfilledOrders.size();
        // int garnished = garnishedOrders.size();
        // int givenUp = givenUpOrder.size();
        System.out.println("Happy customers eating baked goods: " + fulfilled + "(" + garnished + " garnished)" + "\nGone to Greggs instead: " + givenUp);
    }

    /**
     * Returns the number of actions each player can take in a turn
     * @return the number of actions as an int
     */
    public int getActionsPermitted() {
        if (players.size() == 2 || players.size() == 3) {
            return 3;
        }
        if (players.size() == 4 || players.size() == 5) {
            return 2;
        }
        return -1;
    }

    /**
     * Prints the available layers, pantry, and customers
     */
    public void printGameState() {
        System.out.println("Layers: ");
        List<String> outputLayers = StringUtils.layersToStrings(layers);
        for (String string : outputLayers) {
            System.out.println(string);
        }
        System.out.println("Pantry: ");
        List<String> outputPantry = StringUtils.ingredientsToStrings(pantry);
        for (String string : outputPantry) {
            System.out.println(string);
        }
        if (customers.isEmpty()) {
            System.out.println("No customers waiting -- time for a brew :)");
        }
        else {
            System.out.println("Waiting for service: ");
            Collection<CustomerOrder> output = customers.getActiveCustomers();
            List<String> outputCustomers = StringUtils.customerOrdersToStrings(output);
            for (String string : outputCustomers) {
                System.out.println(string);
            }
        }
        System.out.println(currentPlayer.toString() + " it's your turn. Your hand contains: " + currentPlayer.getHandStr());

        
    }

    /**
     * Returns the number of actions the current player has left
     * @return The number of actions
     */
    public int getActionsRemaining() {
        int actionsLeft = currentPlayer.getActionsLeft();
        return actionsLeft;
    }

    /**
     * Allows the player to fulfill an order
     * @param order the order to be fulfilled
     * @param garnish the garnish param
     * @return the list of cards that were drawn as a bonus
     * @throws TooManyActionsException thrown when player has used too many actions
     */
    public List<Ingredient> fulfillOrder(CustomerOrder order, boolean garnish) throws TooManyActionsException {
        // Should call fulfill, 
        //System.out.println("Layers size at point 1: " + layers.size());
        List<Ingredient> drawnCards = new ArrayList<Ingredient>();
        int actionsLeft = currentPlayer.getActionsLeft();
        int next = actionsLeft - 1;
        currentPlayer.setActionsRemaining(next);
        if (next == 0) {
            currentPlayer.setTurnTaken(true);
        }
        if (next < 0) {
            currentPlayer.setActionsRemaining(0);
        }
        //System.out.println("Layers size at point 2: " + layers.size());
        if (actionsLeft > 0) {
            List<Ingredient> currentPlayerHand = currentPlayer.getHand();
            List<Ingredient> ingredientsUsed = order.fulfill(currentPlayerHand, garnish);
           // System.out.println("Layers size at point 3: " + layers.size());
            for (Ingredient i : ingredientsUsed) {
                if (i.getClass() == Layer.class) {
                    layers.add((Layer)i);
                }
                if (i.getClass() == Ingredient.class) {
                    ((Stack<Ingredient>)pantryDiscard).push(i);
                }
                currentPlayerHand.remove(i);
            }
          //  System.out.println("Layers size at point 4: " + layers.size());
            customers.remove(order);
            if (order.getStatus() == CustomerOrderStatus.GARNISHED) {
                Ingredient o = null;
                for (int i = 0; i < 2; i++) {
                    o = drawFromPantryDeck();
                    currentPlayer.addToHand(o);
                    drawnCards.add(o);
                }
            }
          //  System.out.println("Layers size at point 5: " + layers.size());
        }
        else {
            throw new TooManyActionsException();
        }
        return drawnCards;




        
    }

    /**
     * Saves the current state to a file
     * @param file the file object that will contain the save file
     * @throws FileNotFoundException for when the file isnt found
     * @throws IOException idk
     */
    public void saveState(File file) throws FileNotFoundException, IOException{
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        else {
            try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file))) {
                output.writeObject(this);
            }
            catch (IOException e) {
                throw e;
            }
        }
    }

    /**
     * Loads a magic bakery object from the file object
     * @param file the object that has the magic bakery save file
     * @return the magic bakery object
     * @throws IOException honestly im not even actually sure
     * @throws ClassNotFoundException not sure
     */
    public static MagicBakery loadState(File file) throws IOException, ClassNotFoundException{
        MagicBakery returnBakery = null;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            returnBakery = (MagicBakery) in.readObject();
            return returnBakery;
        }
        catch (IOException | ClassNotFoundException e) {
            throw e;
        }
    }

}



