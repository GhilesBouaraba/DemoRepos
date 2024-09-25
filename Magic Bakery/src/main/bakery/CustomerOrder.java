package bakery;

import java.util.ArrayList;
import java.util.List;

import javax.naming.event.ObjectChangeListener;

import java.io.Serializable;
import java.util.*;

/**
 * YUH
 * @author Big ghilou
 * @version 1.0
 */
public class CustomerOrder implements Serializable{

    /**
     * enum for order status
     * @author Big ghilou
     * @version 1.0
     */
    public enum CustomerOrderStatus {
        /**
         * waiting
         */
        WAITING,
        /**
         * fulfilled
         */
        FULFILLED,
        /**
         * garnished
         */
        GARNISHED,
        /**
         * impatient
         */
        IMPATIENT,
        /**
         * given up
         */
        GIVEN_UP
    }

    /**
     * All the attributes
     */
    private List<Ingredient> garnish;
    private int level = 0;
    private String name = null;
    private List<Ingredient> recipe;
    private CustomerOrderStatus status = CustomerOrder.CustomerOrderStatus.WAITING;
    private static final long serialVersionUID = 0;

    /**
     * Constructor for customerorder
     * @param nameIn order name
     * @param recipeIn order recipe
     * @param garnishIn order garnish
     * @param levelIn order level
     * @throws WrongIngredientsException thrown when recipe or garnish is invalid
     */
    public CustomerOrder(String nameIn, List<Ingredient> recipeIn, List<Ingredient> garnishIn, int levelIn) throws WrongIngredientsException{
        name = nameIn;
        if (recipeIn == null) {
            throw new WrongIngredientsException("");
        }
        if (garnishIn == null) {
            throw new WrongIngredientsException("");
        }
        if (recipeIn.isEmpty()) {
            throw new WrongIngredientsException("");
        }
        // if (garnishIn.isEmpty()) {
        //     throw new WrongIngredientsException("");
        // }
        if (recipeIn.isEmpty() && garnishIn.isEmpty()) {
            throw new WrongIngredientsException("");
        }
        
        recipe = recipeIn;
        garnish = garnishIn;
        level = levelIn;
        List<Ingredient> recipeCopy = new ArrayList<>(recipe);
        List<Ingredient> checkerList = new ArrayList<>(); //Add each layer to the array, then if the layer is already in the array remove it from the recipt
        for (Ingredient i : recipeCopy) {
            if ((i.getClass() == Layer.class) && checkerList.contains(i)) {
                recipe.remove(i);
            }
            else {
                checkerList.add(i);
            }
        }
    }

    /**
     * Sets the status of the customer order to given up
     */
    public void abandon() {
        status = CustomerOrderStatus.GIVEN_UP;
    }

    /**
     * returns the list of ingredients needed to garnish the order
     * @return garnish ingredients
     */
    public List<Ingredient> getGarnish() {
        return garnish;
    }

    /**
     * Returns the list of ingredients needed to garnish the order as a single string
     * @return a single string containing all the ingredients needed to garnish, separated by a comma
     */
    public String getGarnishDescription() {
        String returnString = "";
        if (garnish.isEmpty()) {
            return "";
        }
        for (Ingredient currentIngredient : garnish) {
            String currentIngredientName = currentIngredient.toString();
            returnString += currentIngredientName;
            returnString += ", ";
        }
        String newReturnString = returnString.substring(0, returnString.length() - 2);
        return newReturnString;
    }

    /**
     * returns the level
     * @return int representing the level
     */
    public int getLevel() {
        return level;
    }

    /**
     * returns the list containing all the ingredients needed to fulfill this order
     * @return the list representing the recipe
     */
    public List<Ingredient> getRecipe() {
        return recipe;
    }

    /**
     * returns the recipe as a single string containing all the ingredients needed to fulfill this order, separated by a comma
     * @return the string of ingredients
     */
    public String getRecipeDescription() {
        String returnString = "";
        for (Ingredient currentIngredient : recipe) {
            String currentIngredientName = currentIngredient.toString();
            returnString += currentIngredientName.trim();
            returnString += ", ";
        }
        String newReturnString = returnString.substring(0, returnString.length() - 2);
        newReturnString = newReturnString.trim();
        return newReturnString;
    }
    
    /**
     * returns the status of this customer order
     * @return the cutomer order status
     */
    public CustomerOrderStatus getStatus() {
        return status;
    }

    /**
     * takes a customerorderstatus as a parameter and sets it as the status
     * @param statusIn the status parameter
     */
    public void setStatus(CustomerOrderStatus statusIn) {
        this.status = statusIn;
    }

    /**
     * returns the name of the order as a string
     * @return the name of the order
     */
    public String toString() {
        return name;
    }

    /**
     * takes a list of ingredients and checks if they are sufficient to garnish the order
     * @param ingredients the list of ingredients that will be checked
     * @return boolean value representing whether or not it can be garnished
     */
    public boolean canGarnish(List<Ingredient> ingredients) {
        boolean ghilouChecker = true;
        List<Ingredient> ingredientsCopy = new ArrayList<Ingredient>(ingredients);
        int noOfDucks = 0;
        int noOfIngredientsInRecipe = 0;
        int noOfLayersInRecipe = 0;
        int noOfIngredients = 0;
        int noOfLayers = 0;
        int correctIngredients = 0;
        int correctLayers = 0;
        int totalNumOfElements = 0;
        int noOfCorrectElements = 0;
        for (Ingredient curr : ingredientsCopy) {
            if (curr.toString().equals("helpful duck 𓅭")) {
                noOfDucks += 1;
            }
        }
        for (Object obj : garnish) {
            if (obj instanceof Layer) {
                noOfLayersInRecipe += 1;
            }
            if (obj instanceof Ingredient) {
                noOfIngredientsInRecipe += 1;
            }
        }
        for (Object obj : ingredientsCopy) {
            if (obj.getClass() == Layer.class) {
                noOfLayers += 1;
            }
            if (obj.getClass() == Ingredient.class) {
                noOfIngredients += 1;
            }
        }
        totalNumOfElements = noOfIngredients + noOfLayers;
        for (Ingredient currentRecp : garnish) {
            Ingredient ingToRemove = null;
            for (Object currentIng : ingredientsCopy) {
                if (currentIng.getClass() == Layer.class) {
                    if (currentIng.toString().equals(currentRecp.toString())) {
                        correctLayers += 1;
                        ingToRemove = (Ingredient)currentIng;
                    }
                }
                if (currentIng.getClass() == Ingredient.class) {
                    if (currentIng.toString().equals(currentRecp.toString())) {
                        correctIngredients += 1;
                        ingToRemove = (Ingredient)currentIng;
                    }
                }
            }
            if (ingToRemove != null) {
                ingredientsCopy.remove(ingToRemove);
            }
        }
        int missingIngredients = noOfIngredientsInRecipe - correctIngredients;
        int missingLayers = noOfLayersInRecipe - correctLayers;
        if ((missingIngredients == 0) && (missingLayers == 0)) {
            return true;
        }
        if ((missingIngredients != 0) && (missingLayers == 0)) {
            if (noOfDucks >= missingIngredients) {
                return true;
            }
            else {
                return false;
            }
        }
        if ((missingIngredients == 0) && (missingLayers != 0)) {
            return false;
        }
        if ((missingIngredients != 0) && (missingLayers != 0)) {
            return false;
        }
        return (Boolean) null;
    }

    /**
     * takes a list of ingredients as a parameter and checks if they are sufficient to fulfill the order
     * @param ingredients the lsit of ingredients that will be checked
     * @return boolean value representing whether or not the order can be fulfilled
     */
    public boolean canFulfill(List<Ingredient> ingredients) {
        System.out.println("canFulfill method is now executing");
        boolean ghilouChecker = true;
        List<Ingredient> ingredientsCopy = new ArrayList<Ingredient>(ingredients);
        for (Ingredient i : ingredients) {
            if (i.getClass() == Ingredient.class && i.toString() == "jam") {
                ingredientsCopy.remove(i);
                break;
            }
        }
        int noOfDucks = 0;
        int noOfIngredientsInRecipe = 0;
        int noOfLayersInRecipe = 0;
        int noOfIngredients = 0;
        int noOfLayers = 0;
        int correctIngredients = 0;
        int correctLayers = 0;
        int totalNumOfElements = 0;
        int noOfCorrectElements = 0;
        for (Ingredient curr : ingredientsCopy) {
            if (curr.toString().equals("helpful duck 𓅭")) {
                noOfDucks += 1;
            }
        }
        for (Object obj : recipe) {
            System.out.println("Current recipe item: " + obj.toString());
            if (obj.getClass() == Layer.class) {
                noOfLayersInRecipe += 1;
                System.out.println("Instance of layer");
                continue;
            }
            if (obj.getClass() == Ingredient.class) {
                System.out.println("Instance of Ingredient");
                noOfIngredientsInRecipe += 1;
            }
        }
        System.out.println("No of layers in recipe: " + noOfLayersInRecipe);
        System.out.println("No of ingredients in recipe: " + noOfIngredientsInRecipe);
        for (Object obj : ingredientsCopy) {
            System.out.println("Current item: " + obj.toString());
            if (obj.getClass() == Layer.class) {
                noOfLayers += 1;
            }
            if (obj.getClass() == Ingredient.class) {
                noOfIngredients += 1;
            }
        }
        System.out.println("noOfLayers: " + noOfLayers);
        System.out.println("noOfIngredients: " + noOfIngredients);

        totalNumOfElements = noOfIngredients + noOfLayers;
        for (Ingredient currentRecp : recipe) {
            System.out.println("Current recipe element: " + currentRecp.toString());
            System.out.println("Recipe item: " + currentRecp.toString());
            Ingredient ingToRemove = null;
            for (Object currentIng : ingredientsCopy) {
                System.out.println(currentIng.toString());
                System.out.println("Ingredients item: " + currentIng.toString());
                if (currentIng.getClass() == Layer.class) {
                    if (currentIng.toString().equals(currentRecp.toString())) {
                        correctLayers += 1;
                        System.out.println("Correct Layer found");
                        ingToRemove = (Ingredient)currentIng;
                        System.out.println("Adding a correct layer");
                        break;
                    }
                }
                if (currentIng.getClass() == Ingredient.class) {
                    if (currentIng.toString().equals(currentRecp.toString())) {
                        correctIngredients += 1;
                        System.out.println("Correct Ingredient found");
                        ingToRemove = (Ingredient)currentIng;
                        System.out.println("Adding a correct ingredient");
                        break;
                    }
                }
            }
            if (ingToRemove != null) {
                ingredientsCopy.remove(ingToRemove);
            }
        }
        System.out.println("Correct Layers: " + correctLayers);
        System.out.println("Correct Ingredients: " + correctIngredients);
        int missingIngredients = noOfIngredientsInRecipe - correctIngredients;
        int missingLayers = noOfLayersInRecipe - correctLayers;
        System.out.println("Missing Ingredients: " + missingIngredients);
        System.out.println("Missing layers: " + missingLayers);
        //System.out.println("Missing ingredients: " + missingIngredients);
        //System.out.println("Missing layers: " + missingLayers);
        if ((missingIngredients == 0) && (missingLayers == 0)) {
            return true;
        }
        if ((missingIngredients != 0) && (missingLayers == 0)) {
            if (noOfDucks >= missingIngredients) {
                return true;
            }
            else {
                return false;
            }
        }
        if ((missingIngredients == 0) && (missingLayers != 0)) {
            return false;
        }
        if ((missingIngredients != 0) && (missingLayers != 0)) {
            return false;
        }
        return (Boolean) null;
    }

    /**
     * not finished
     * @param ingredients NOT FINISHED
     * @param garnishIn NOT FINISHED
     * @return NOT FINISHED
     * @throws WrongIngredientsException NOT FINISHED
     */
    public List<Ingredient> fulfill(List<Ingredient> ingredients, boolean garnishIn) throws WrongIngredientsException{
        List<Ingredient> recipeCopy = new ArrayList<Ingredient>(recipe);
        List<Ingredient> garnishCopy = new ArrayList<Ingredient>(garnish);
        List<Ingredient> returnList = new ArrayList<Ingredient>();
        List<Ingredient> ingredientsCopy = new ArrayList<Ingredient>(ingredients);
        boolean canFulfillOrder = false;
        canFulfillOrder = canFulfill(ingredientsCopy);
        if (!canFulfillOrder) {
            throw new WrongIngredientsException("");
        }
        //System.out.println("Can fulfill? " + canFulfillOrder);
        if (canFulfillOrder) {
            for (Ingredient i : recipe) {
                Ingredient ingToRemove = null;
                for (Ingredient o : ingredientsCopy) {
                    if (o.toString().equals(i.toString())) {
                        returnList.add(o);
                        ingToRemove = o;
                        break;
                    }
                }
                if (ingToRemove != null) {
                    ingredientsCopy.remove(ingToRemove);
                }
            }
        }
        boolean canGarnishOrder = false;
        canGarnishOrder = canGarnish(ingredientsCopy);
        //System.out.println("Can Garnish? " + canGarnishOrder);
        if (canGarnishOrder && garnishIn) {
            for (Ingredient i : garnish) {
                Ingredient ingToRemove1 = null;
                for (Ingredient o : ingredientsCopy) {
                    if (o.toString().equals(i.toString())) {
                        returnList.add(o);
                        ingToRemove1 = o;
                        break;
                    }
                }
                if (ingToRemove1 != null) {
                    ingredientsCopy.remove(ingToRemove1);
                }
            }
        }
        
        List<Ingredient> returnListCopy = new ArrayList<Ingredient>(returnList);
        if (canFulfillOrder && canGarnishOrder && garnishIn) {  
            int noOfMissingFulfill = 0;
            int noOfMissingGarnish = 0;
            for (Ingredient i : recipe) {
                if (returnListCopy.contains(i)) {
                    returnListCopy.remove(i);
                    continue;
                }
                else {
                    noOfMissingFulfill += 1;
                }
            }
            for (Ingredient i : garnish) {
                if (returnListCopy.contains(i)) {
                    returnListCopy.remove(i);
                    continue;
                }
                else {
                    noOfMissingGarnish += 1;
                }
            }
            for (int i = 0; i < noOfMissingFulfill; i++) {
                returnList.add(Ingredient.HELPFUL_DUCK);
            }
            for (int i = 0; i < noOfMissingGarnish; i++) {
                returnList.add(Ingredient.HELPFUL_DUCK);
            }
            status = CustomerOrderStatus.GARNISHED;
        }
        if (!garnishIn && canFulfillOrder) {
            int noOfMissingFulfill = 0;
            for (Ingredient i : recipe) {
                if (returnListCopy.contains(i)) {
                    returnListCopy.remove(i);
                    continue;
                }
                else {
                    noOfMissingFulfill += 1;
                }
            }
            for (int i = 0; i < noOfMissingFulfill; i++) {
                returnList.add(Ingredient.HELPFUL_DUCK);
            }
            status = CustomerOrderStatus.FULFILLED;
        }
        if (canFulfillOrder && !canGarnishOrder) {
            status = CustomerOrderStatus.FULFILLED;
        }
        if (garnish.isEmpty() && status == CustomerOrderStatus.GARNISHED) {
            status = CustomerOrderStatus.FULFILLED;
        }
        //System.out.println("Status: " + status);
        return returnList;
    }
}

        





















        // List<Ingredient> returnList = new ArrayList<Ingredient>();
        // List<Ingredient> ingredientsCopy = new ArrayList<Ingredient>(ingredients);
        // int noOfDucks = 0;
        // for (Ingredient curr : ingredientsCopy) {
        //     if (curr.toString().equals("helpful duck 𓅭")) {
        //         noOfDucks += 1;
        //     }
        // }
        // boolean canFulfill = false;
        // boolean canFulfillAndGarnish = false;


        // int noOfIngredientsInRecipe = 0;
        // int noOfLayersInRecipe = 0;
        // int noOfIngredients = 0;
        // int noOfLayers = 0;
        // int correctIngredients = 0;
        // int correctLayers = 0;
        // int totalNumOfElements = 0;
        // int noOfCorrectElements = 0;
        // for (Object obj : recipe) {
        //     if (obj instanceof Layer) {
        //         noOfLayersInRecipe += 1;
        //     }
        //     if (obj instanceof Ingredient) {
        //         noOfIngredientsInRecipe += 1;
        //     }
        // }
        // for (Object obj : ingredientsCopy) {
        //     if (obj.getClass() == Layer.class) {
        //         noOfLayers += 1;
        //     }
        //     if (obj.getClass() == Ingredient.class) {
        //         noOfIngredients += 1;
        //     }
        // }
        // totalNumOfElements = noOfIngredients + noOfLayers;
        // for (Ingredient currentRecp : recipe) {
        //     Ingredient ingToRemove = null;
        //     for (Object currentIng : ingredientsCopy) {
        //         if (currentIng.getClass() == Layer.class) {
        //             if (currentIng.toString().equals(currentRecp.toString())) {
        //                 correctLayers += 1;
        //                 ingToRemove = (Ingredient)currentIng;
        //             }
        //         }
        //         if (currentIng.getClass() == Ingredient.class) {
        //             if (currentIng.toString().equals(currentRecp.toString())) {
        //                 correctIngredients += 1;
        //                 ingToRemove = (Ingredient)currentIng;
        //             }
        //         }
        //     }
        //     if (ingToRemove != null) {
        //         ingredientsCopy.remove(ingToRemove);
        //         returnList.add(ingToRemove);
        //     }
        // }
        // int missingIngredientsForFulfill = noOfIngredientsInRecipe - correctIngredients;
        // int missingLayersForFulfill = noOfLayersInRecipe - correctLayers;
        // if ((missingIngredientsForFulfill == 0) && (missingLayersForFulfill == 0)) {
        //     canFulfill = true;   
        // }
        // if ((missingIngredientsForFulfill != 0) && (missingLayersForFulfill == 0)) {
        //     if (noOfDucks >= missingIngredientsForFulfill) {
        //         canFulfill = true;
        //         noOfDucks -= missingIngredientsForFulfill;
        //     }
        //     else {
        //         canFulfill = false;
        //     }
        // }
        // if ((missingIngredientsForFulfill == 0) && (missingLayersForFulfill != 0)) {
        //     canFulfill = false;
        // }
        // if ((missingIngredientsForFulfill != 0) && (missingLayersForFulfill != 0)) {
        //     canFulfill = false;
        // }

        // int noOfIngredientsInGarnish = 0;
        // int noOfLayersInGarnish = 0;
        // int noOfIngredientsForGarnish = 0;
        // int noOfLayersForGarnish = 0;
        // int correctIngredientsForGarnish = 0;
        // int correctLayersForGarnish = 0;
        // int totalNumOfElementsForGarnish = 0;
        // int noOfCorrectElementsForGarnish = 0;
        // for (Object obj : garnish) {
        //     if (obj instanceof Layer) {
        //         noOfLayersInGarnish += 1;
        //     }
        //     if (obj instanceof Ingredient) {
        //         noOfIngredientsForGarnish += 1;
        //     }
        // }
        // for (Object obj : ingredientsCopy) {
        //     if (obj.getClass() == Layer.class) {
        //         noOfLayersInGarnish += 1;
        //     }
        //     if (obj.getClass() == Ingredient.class) {
        //         noOfIngredientsInGarnish += 1;
        //     }
        // }
        // totalNumOfElementsForGarnish = noOfIngredientsInGarnish + noOfLayersInGarnish;
        // for (Ingredient currentRecp : garnish) {
        //     Ingredient ingToRemove = null;
        //     for (Object currentIng : ingredientsCopy) {
        //         if (currentIng.getClass() == Layer.class) {
        //             if (currentIng.toString().equals(currentRecp.toString())) {
        //                 correctLayersForGarnish += 1;
        //                 ingToRemove = (Ingredient)currentIng;
        //             }
        //         }
        //         if (currentIng.getClass() == Ingredient.class) {
        //             if (currentIng.toString().equals(currentRecp.toString())) {
        //                 correctIngredientsForGarnish += 1;
        //                 ingToRemove = (Ingredient)currentIng;
        //             }
        //         }
        //     }
        //     if (ingToRemove != null) {
        //         ingredientsCopy.remove(ingToRemove);
        //         returnList.add(ingToRemove);
        //     }
        // }
        // int missingIngredientsForGarnish = noOfIngredientsInGarnish - correctIngredientsForGarnish;
        // int missingLayersForGarnish = noOfLayersInGarnish - correctLayersForGarnish;
        // if ((missingIngredientsForGarnish == 0) && (missingLayersForGarnish == 0)) {
        //     canFulfillAndGarnish = true;
        // }
        // if ((missingIngredientsForGarnish != 0) && (missingLayersForGarnish == 0)) {
        //     if (noOfDucks >= missingIngredientsForGarnish) {
        //         canFulfillAndGarnish = true;
        //     }
        //     else {
        //         canFulfillAndGarnish = false;
        //     }
        // }
        // if ((missingIngredientsForGarnish == 0) && (missingLayersForGarnish != 0)) {
        //     canFulfillAndGarnish = false;
        // }
        // if ((missingIngredientsForGarnish != 0) && (missingLayersForGarnish != 0)) {
        //     canFulfillAndGarnish = false;
        // }
        // if (garnishIn) {
        //     if (canFulfillAndGarnish) {
        //         status = CustomerOrderStatus.GARNISHED;
        //     }
        //     else {
        //         if (canFulfill) {
        //             status = CustomerOrderStatus.FULFILLED;
        //         }
        //     }
        // }
        // else {
        //     if (canFulfill) {
        //         status = CustomerOrderStatus.FULFILLED;
        //     }
        // }
        // return returnList;

