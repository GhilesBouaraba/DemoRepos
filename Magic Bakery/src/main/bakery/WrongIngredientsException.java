package bakery;

/**
 * Exception
 * @author Big Ghilou
 * @version 1.0
 */
public class WrongIngredientsException extends java.lang.IllegalArgumentException {
   /**
    * The string constructor
    * @param msg The error message
    */
    public WrongIngredientsException(String msg) {
        super(msg);
    }
}