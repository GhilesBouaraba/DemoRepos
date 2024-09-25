package bakery;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;


/**
 * The layer class
 * @author Big Ghilou
 * @version 1.0
 */
public class Layer extends Ingredient implements Serializable{
    private List<Ingredient> recipe = new ArrayList<>();
    private final static long serialVersionUID = 0;

    /**
     * Constructor for layer 
     * @param nameIn name of the layer
     * @param recipeIn recipe of the layer
     * @throws WrongIngredientsException thrown when the recipe is either empty or null
     */
    public Layer(String nameIn, List<Ingredient> recipeIn) throws WrongIngredientsException{
        super(nameIn);
        if (recipeIn == null) {
            throw new WrongIngredientsException("Null recipe");
        }
        if (recipeIn.isEmpty()) {
            throw new WrongIngredientsException("Empty Recipe");
        }
        recipe = recipeIn;
        
    }

    /**
     * Takes a list of ingredients as a parameter and returns whether or not the layer can be baked using those ingredients
     * @param ingredients the list of ingredients
     * @return boolean value representing whether the layer can be baked or not
     */
    public boolean canBake(List<Ingredient> ingredients) {
        List<Ingredient> recipeReplica = new ArrayList<>(recipe);
        List<Ingredient> ingredientsReplica = new ArrayList<>(ingredients);
        boolean ghilouChecker = true;
        int missingIngredients = 0;
        int correctIng = 0;
        int noOfDucks = 0;
        for (Ingredient currentIng : ingredients) {
            if (currentIng.toString() == "helpful duck 𓅭") {
                noOfDucks += 1;
            }
        }
        for (Ingredient currentIng : ingredients) {
            //System.out.println(currentIng.toString());
            if (recipeReplica.contains(currentIng)) {
                //System.out.println("True");
                correctIng += 1;
                recipeReplica.remove(currentIng);
            }
        }
        for (Ingredient currentIng : recipe) {
            if (ingredientsReplica.contains(currentIng) == false) {
                missingIngredients += 1;
            }
            else {
                ingredientsReplica.remove(currentIng);
            }
        }
        //System.out.println(noOfDucks);
        //System.out.println(missingIngredients);
        if (recipe.size() == correctIng) {
            ghilouChecker = true;
        } 
        else if (missingIngredients > 0 && (missingIngredients == noOfDucks) || (missingIngredients < noOfDucks)) {
            ghilouChecker = true;
        }
        else {
            ghilouChecker = false;
        }
        return ghilouChecker;
    }

    /**
     * get method for the recipe
     * @return list of ingredients required to bake this layer
     */
    public List<Ingredient> getRecipe() {
        return recipe;
    }

    /**
     * returns the ingredients required to make this layer as a single string, with each ingredient separated by a comma
     * @return string containing all the ingredients
     */
    public String getRecipeDescription() {
        String returnString = "";
        for (Ingredient currentIngredient : recipe) {
            String currentIngredientName = currentIngredient.toString();
            returnString += currentIngredientName;
            returnString += ", ";
        }
        returnString = returnString.substring(0, returnString.length() - 2);
        return returnString;
    }


    @Override
    /**
     * NOT COMPLETED YET
     * @return integer
     */
    public int hashCode() {
        return 2;
    }
}   
