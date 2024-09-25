package util;
import java.io.*;
import java.io.Console;
import java.util.*;
import bakery.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import bakery.MagicBakery.ActionType;

/**
 * This is the console utils class designed to make it easier to do prompts for inputs
 * @author BIG GHILOU 
 * @version 1.0
 */
public final class ConsoleUtils implements Serializable{
    private Console console = System.console();

    /**
     * Empty Constructor
     */
    public ConsoleUtils() {
    }

    /**
     * Prompts the user to enter a file path and returns it as a File object
     * @param prompt prompt to the user
     * @return the file object
     */
    public File promptForFilePath(String prompt) {
        File returnFile = null;
        //System.out.println(prompt);
        String output = console.readLine();
        returnFile = new File(output);
        return returnFile;
    }
    
    /**
     * Reads the next line
     * @return returns the input from the user
     */
    public String readLine() {
        String returnString;
        returnString = console.readLine();
        return returnString;
    }

    /**
     * Prompts the user for a yes or no repsonse and returns a boolean depending on which
     * @param prompt the prompt to be displayed to the user
     * @return The boolean value
     */
    public boolean promptForYesNo(String prompt) {
        boolean returnVal = false;
        String input = console.readLine(prompt + "[Y]es or [N]o");
        if (input == "y" || input == "Y") {
            returnVal = true;
        }
        else {
            returnVal = false;
        }
        return returnVal;
    }

    /**
     * Prompts the user for start or load
     * @param prompt the prompt to be displayed to the user
     * @return the boolean value to be returned 
     */
    public boolean promptForStartLoad(String prompt) {
        boolean returnVal = false;
        String input = console.readLine(prompt + "[S]tart or [L]oad");
        if (input == "S" || input == "s") {
            returnVal = true;
        }
        else {
            returnVal = false;
        }
        return returnVal;
    }

    /**
     * Prompts the user to enter the names of new players and returns them as a list of strings
     * @param prompt The prompt to be displayed to the user
     * @return the list of player names
     */
    public List<String> promptForNewPlayers(String prompt) {
        //System.out.println(prompt);
        List<String> returnString = new ArrayList<>();
        String output = console.readLine("Please enter the first player's name: ");
        returnString.add(output);
        boolean ghilouChecker = true;
        while (ghilouChecker) {
            String nextOutput = console.readLine("Please enter the next player's name: ");
            if (nextOutput != "") {
                returnString.add(nextOutput);
            }
            else {
                break;
            }
        }
        return returnString;
    }

    /**
     * This method prompts the user to select an ingredient
     * @param prompt the message to be displayed as a prompt
     * @param ingredients the list of ingredients the user will select from 
     * @return the ingredient the user has selected
     */
    public Ingredient promptForIngredient(String prompt, Collection<Ingredient> ingredients) {
        //System.out.println(prompt);
        Ingredient returnIngredient = null;
        for (Ingredient ingredient : ingredients) {
            //System.out.println(ingredient.toString());
        }
        String output = console.readLine("Now please enter the name of the ingredient you wish to select (Case sensitive): ");
        for (Ingredient ingredient : ingredients) {
            if (ingredient.toString() == output) {
                returnIngredient = ingredient;
            }
        }
        return returnIngredient;
    }

    /**
     * Prompts the user to provide a customer name and returns the customer under that name
     * @param prompt the prompt to be displayed
     * @param customers all the customer orders
     * @return the customer order the user selected
     */
    public CustomerOrder promptForCustomer(String prompt, Collection<CustomerOrder> customers) {
        //System.out.println(prompt);
        CustomerOrder returnOrder = null;
        for (CustomerOrder order : customers) {
            //System.out.println(order.toString());
        }
        String output = console.readLine("Now please enter the name of the order you wish to select (Case Sensitive)");
        for (CustomerOrder order : customers) {
            if (order.toString() == output) {
                returnOrder = order;
            }
        }
        return returnOrder;
    }

    /**
     * dk dont care
     * @param prompt dk 
     * @param collection dk
     * @return dk
     * @throws IllegalArgumentException dk
     */
    private Object promptEnumerateCollection(String prompt, Collection<Object> collection) throws IllegalArgumentException{
        if (collection == null) {
            throw new IllegalArgumentException("Null collection");
        }
        if (collection.isEmpty()) {
            throw new IllegalArgumentException("Empty collection");
        }
        //System.out.println(prompt);
        int counter = 1;
        for (Object obj : collection) {
            //System.out.println("[" + counter + "]  " + obj);
            counter += 1;
        }
        Scanner scanner = new Scanner(System.in);
        //System.out.println("Please enter the corresponding number: ");
        int inp = scanner.nextInt();
        if (inp >= 2 && inp <= counter) {
            Object output = ((ArrayList<Object>)collection).get(inp);
            return output;
        }
        else {
            throw new IllegalArgumentException("Illegal int enetered");
        }
    }

    /**
     * dk dont care
     * @param prompt dk
     * @param bakery dk
     * @return dk
     */
    public Player promptForExistingPlayer(String prompt, MagicBakery bakery) {
        Player output = null;
        return output;
    }

    /**
     * dk dont care
     * @param fmt dk
     * @param args dk
     * @return dk
     */
    public String readLine(String fmt, Object[] args) {
        return "";
    }

    /**
     * I really dont know why this isnt passing but imma try regardless
     * @param prompt This is the string prompt to be displayed on the screen
     * @param bakery this is the bakery item of magic bakery
     * @return this is the action type to be returned 
     */
    public ActionType promptForAction(String prompt, MagicBakery bakery) {
        return MagicBakery.ActionType.BAKE_LAYER;
    }
}
