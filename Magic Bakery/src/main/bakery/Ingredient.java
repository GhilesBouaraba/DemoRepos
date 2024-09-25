package bakery;

import java.io.Serializable;
import java.util.Objects;
/**
 * YUH
 * @author Big Ghilou
 * @version 1.0
 */
public class Ingredient implements Serializable, Comparable<Ingredient>{
    /**
     * All the attributes of the ingredient class
     */
    private String name;
    private static final long serialVersionUID = 0;
    /**
     * HelpFul duck not very helpful tho fr
     */
    public static final Ingredient HELPFUL_DUCK = new Ingredient("helpful duck 𓅭");


    /**
     * Constructor method for the ingredient class
     * @param nameIn is the name of the ingredient
     */
    public Ingredient(String nameIn) {
        name = nameIn;
    }    
    /**
     * The toString method returns the name of the ingredient
     * @return the name of the ingredient
     */
    public String toString() {
        return name;
    }

    /**
     * unfinished method
     * @return unfinished
     */
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    /**
     * yeeee
     * @param o yuh
     * @return int
     */
    public boolean equals (Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Ingredient ingredient = (Ingredient) o;
        return Objects.equals(name, ingredient.name);
    }
    

    /**
     * yuh
     * @param in Ingredient to check
     * @return int 
     */
    public int compareTo(Ingredient in) {
        return name.compareTo(in.toString());
    }
}