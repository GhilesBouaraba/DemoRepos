package util;
import java.util.*;
import java.io.*;
import bakery.*;



/**
 * Yuh
 * @author Ghilou
 * @version 1.0
 */
public class CardUtils {

    /**
     * Empty constructor for cardutils
     */
    private CardUtils() {

    }

    /**
     * Checks each line and returns a list of however many copies of the ingredient
     * @param str line to use
     * @return list of ingredients
     */
    private static List<Ingredient> stringToIngredients(String str) {
        List<Ingredient> returnList = new ArrayList<>();
        String[] stringArray = str.split(",");
        String ingredientName = stringArray[0].trim();
        String quantity = stringArray[1].trim();
        int quantityInt = Integer.parseInt(quantity);
        for (int i = 0; i < quantityInt; i++) {
            Ingredient currentIngredient = new Ingredient(ingredientName);
            returnList.add(currentIngredient);
        }
        return returnList;
    }



    /**
     * Takes a file parameter as a path and returns a list containing all the ingredients
     * @param path the file path
     * @return the list of all ingredients
     * @throws IOException kd
     */
    public static List<Ingredient> readIngredientFile(String path) throws IOException {
        int counter = 1;
        List<Ingredient> returnList = new ArrayList<>();
        Scanner scanner = new Scanner(new File(path));
        if (scanner.hasNext() == false) {
            return returnList;
        }
        scanner.nextLine();
        while (scanner.hasNext()) {
            List<Ingredient> output = stringToIngredients(scanner.nextLine());
            for (Ingredient currentIngredient : output) {
                returnList.add(currentIngredient);
            }
        }
        return returnList;
    }




    /**
     * String to layers
     * @param str the line to be used
     * @return the list of layers
     */
    private static List<Layer> stringToLayers(String str) {
        List<Ingredient> currentLayerRecipe = new ArrayList<>();
        List<Layer> returnLayer = new ArrayList<>();
        String[] currentArray = str.split(",");
        List<String> trimmedIngredients = new ArrayList<String>();
        String currentLayerName = currentArray[0].trim();
        String unseparatedIngredients = currentArray[1];
        String[] separatedIngredientsWithSpaces = unseparatedIngredients.split(";");
        for (String string : separatedIngredientsWithSpaces) {
            String strToBeAdded = string.trim();
            trimmedIngredients.add(strToBeAdded);
        }
        for (String string : trimmedIngredients) {
            currentLayerRecipe.add(new Ingredient(string));
        }
        Layer currentLayer = new Layer(currentLayerName, currentLayerRecipe);
        
        returnLayer.add(currentLayer);
        return returnLayer;
    }

    /**
     * HELLPPPPPPPPP
     * @param path idk
     * @return idk
     * @throws IOException idk
     */
    public static List<Layer> readLayerFile(String path) throws IOException{
        List<Layer> returnList = new ArrayList<>();
        Scanner scanner = new Scanner(new File(path));
        if (scanner.hasNext() == false) {
            return returnList;
        }
        scanner.nextLine();
        while (scanner.hasNext()) {
            String currentLine = scanner.nextLine();
            List<Layer> output = stringToLayers(currentLine);
            for (Layer currentLayer : output) {
                for (int i = 0; i < 4; i++) {
                    returnList.add(currentLayer);
                }
            }
        }
        return returnList;
    }

    /**
     * Ok this method takes a line and returns the customer order
     * @param str the line
     * @param layers all the layers
     * @return Returns the customer order
     */
    private static CustomerOrder stringToCustomerOrder(String str, Collection<Layer> layers) {
        boolean garnishable = false;
        List<Ingredient> currentRecipe = new ArrayList<>();
        List<Ingredient> currentGarnish = new ArrayList<>();
        String[] initialSplitString = str.split(",");
        for (String s : initialSplitString) {
            //System.out.println(s);
        }
        if (initialSplitString.length == 4) {
            garnishable = true;
        }
        else if (initialSplitString.length == 3) {
            garnishable = false;
        }
        String untrimmedName = initialSplitString[1];
        String untrimmedLevel = initialSplitString[0];
        String trimmedName = untrimmedName.trim();
        String trimmedLevel = untrimmedLevel.trim();
        int levelInt = Integer.parseInt(trimmedLevel);
        //System.out.println("Garnish?" + garnishable);
        if (garnishable) {
            String unseparatedRecipe = initialSplitString[2];
            String unseparatedGarnish = initialSplitString[3];
            //System.out.println(unseparatedRecipe);
            //System.out.println(unseparatedGarnish);
            String[] separatedUntrimmedRecipe = unseparatedRecipe.split(";");
            List<String> trimmedRecipe = new ArrayList<String>();
            for (String yee : separatedUntrimmedRecipe) {
                trimmedRecipe.add(yee);
            }
            for (int i = 0; i < trimmedRecipe.size(); i++) {
                boolean layer = false;
                String currentElement = trimmedRecipe.get(i);
                for (Layer currentLayer : layers) {
                    if (currentLayer.toString().equals(currentElement)) {
                        layer = true;
                        currentRecipe.add(currentLayer);
                    }
                }
                if (layer) {
                    continue;
                }
                else {
                    Ingredient currentIngredientToAdd = new Ingredient(currentElement);
                    currentRecipe.add(currentIngredientToAdd);
                }
            }
            String[] separatedUntrimmedGarnish = unseparatedGarnish.split(";");
            List<String> trimmedGarnish = new ArrayList<String>();
            for (String yee : separatedUntrimmedGarnish) {
                trimmedGarnish.add(yee.trim());
            }
            for (int i = 0; i < trimmedGarnish.size(); i++) {
                boolean layer1 = false;
                String currentElement = trimmedGarnish.get(i);
                for (Layer currentLayer : layers) {
                    if (currentLayer.toString().equals(currentElement)) {
                        layer1 = true;
                        currentGarnish.add(currentLayer);
                    }
                }
                if (layer1) {
                    continue;
                }
                else {
                    Ingredient currentIngredient1 = new Ingredient(currentElement);
                    currentGarnish.add(currentIngredient1);
                }
            }
        }
        else {
            String unseparatedRecipe = initialSplitString[2];
            String[] separatedUntrimmedRecipe = unseparatedRecipe.split(";");
            List<String> trimmedRecipe = new ArrayList<String>();
            for (String yee : separatedUntrimmedRecipe) {
                trimmedRecipe.add(yee.trim());
            }

            for (String s : trimmedRecipe) {
                //System.out.println(s);
            }

            for (int i = 0; i < trimmedRecipe.size(); i++) {
                boolean layer = false;
                String currentElement = trimmedRecipe.get(i);
                //System.out.println("Current Element: [" + currentElement + "]");
                for (Layer currentLayer : layers) {
                    //System.out.println("Check1");
                    //System.out.println(currentLayer.toString());
                    String layerName = currentLayer.toString();
                    //System.out.println("Current layer name: [" + layerName + "]");
                    if (layerName.equals(currentElement)) {
                        //System.out.println("check2");
                        layer = true;
                        currentRecipe.add(currentLayer);
                        //System.out.println("Adding layer");
                    }
                }
                //System.out.println(layer);
                if (layer) {
                    continue;
                }
                else {
                    Ingredient currentIngredientToAdd = new Ingredient(currentElement);
                    currentRecipe.add(currentIngredientToAdd);
                }
            }
        }
        //System.out.println("Final Check");
        for (Object obj : currentRecipe) {
            //System.out.println(obj.getClass());
        }
        CustomerOrder returnOrder = new CustomerOrder(trimmedName, currentRecipe, currentGarnish, levelInt);
        return returnOrder;
    }

    /**
     * Takes a file path and a layers collection as a parameter and returns a list of all the customer orders in the file
     * @param path the file path
     * @param layers all the layers 
     * @return the list of all the orders
     * @throws FileNotFoundException In case the file path is invalid
     */
    public static List<CustomerOrder> readCustomerFile(String path, Collection<Layer> layers) throws FileNotFoundException{
        List<CustomerOrder> returnList = new ArrayList<>();
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(path));
        }
        catch (FileNotFoundException e) {
            throw e;
        }
        if (scanner.hasNext() == false) {
            scanner.close();
            return returnList;
        }
        scanner.nextLine();
        while (scanner.hasNext()) {
            String currentLine = scanner.nextLine();
            CustomerOrder output = stringToCustomerOrder(currentLine, layers);
            //System.out.println("[" + output.toString() + "]");
            returnList.add(output);
        }
        scanner.close();
        return returnList;
    }
}

