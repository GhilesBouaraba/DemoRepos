package bakery;
import java.util.Random;

import bakery.CustomerOrder.CustomerOrderStatus;

import java.util.List;
import java.util.Stack;
import java.util.ArrayList;
import java.util.Collection;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import util.CardUtils;
import util.CardUtils.*;

import java.io.*;

/**
 * The class that handles all the stuff regarding customer orders
 * @author Big Ghilou
 * @version 1.0
 */
public class Customers implements Serializable{
    private Collection<CustomerOrder> activeCustomers = new LinkedList<CustomerOrder>();
    private Collection<CustomerOrder> customerDeck;
    private List<CustomerOrder> inactiveCustomers = new ArrayList<>();;
    private Random random;
    private static final long serialVersionUID = 0;

    /**
     * Constructor for customers
     * @param deckFile file path
     * @param randomIn random obj
     * @param layersIn layers to be added
     * @param numPlayers number of players as an int
     * @throws FileNotFoundException thrown when file path isnt found
     */
    public Customers(String deckFile, Random randomIn, Collection<Layer> layersIn, int numPlayers) throws FileNotFoundException{
        random = randomIn;
        ((LinkedList<CustomerOrder>)activeCustomers).add(null);
        ((LinkedList<CustomerOrder>)activeCustomers).add(null);
        ((LinkedList<CustomerOrder>)activeCustomers).add(null);
        try {
            initialiseCustomerDeck(deckFile, layersIn, numPlayers);
        }
        catch (FileNotFoundException e) {
            throw e;
        }
    }

    /**
     * A very long method that I think works?
     * @param deckFile The file path of the deck of customer cards
     * @param layers the layers from the constructor
     * @param numPlayers the number of players playing
     * @throws Exception idk
     */
    private void initialiseCustomerDeck(String deckFile, Collection<Layer> layers, int numPlayers) throws FileNotFoundException {
        customerDeck = new Stack<CustomerOrder>();
        List<CustomerOrder> outputFromUtils = new ArrayList<CustomerOrder>();
        try {
            outputFromUtils = CardUtils.readCustomerFile(deckFile, layers);
        }
        catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        }
        Collections.shuffle(outputFromUtils, random);
        // 2P 
        if (numPlayers == 2) {
            int counter = 0;
            for (CustomerOrder order : outputFromUtils) {
                if (((Stack<CustomerOrder>)customerDeck).size() >= 4) {
                    break;
                }
                if (order.getLevel() == 1) {
                    ((Stack<CustomerOrder>)customerDeck).push(order);
                    counter += 1;
                }
                
            }
            counter = 0;
            for (CustomerOrder order : outputFromUtils) {
                if (((Stack<CustomerOrder>)customerDeck).size() >= 6 ) {
                    break;
                }
                if (order.getLevel() == 2) {
                    ((Stack<CustomerOrder>)customerDeck).push(order);
                    counter += 1;
                }
            }
            counter = 0;
            for (CustomerOrder order : outputFromUtils) {
                if (((Stack<CustomerOrder>)customerDeck).size() >= 7) {
                    break;
                }
                if (order.getLevel() == 3) {
                    ((Stack<CustomerOrder>)customerDeck).push(order);
                    counter += 1;
                }
            }
        }
        if (numPlayers == 3 || numPlayers == 4) {
            int counter = 0;
            for (CustomerOrder order : outputFromUtils) {
                if (counter >= 1) {
                    break;
                }
                if (order.getLevel() == 1) {
                    ((Stack<CustomerOrder>)customerDeck).push(order);
                    counter += 1;
                }
            }
            counter = 0;
            for (CustomerOrder order : outputFromUtils) {
                if (counter >= 2) {
                    break;
                }
                if (order.getLevel() == 2) {
                    ((Stack<CustomerOrder>)customerDeck).push(order);
                    counter += 1;
                }
            }
            counter = 0;
            for (CustomerOrder order : outputFromUtils) {
                if (counter >= 4) {
                    break;
                }
                if (order.getLevel() == 3) {
                    ((Stack<CustomerOrder>)customerDeck).push(order);
                    counter += 1;
                }
            }
        }
        if (numPlayers == 5) {
            int counter = 0;
            for (CustomerOrder order : outputFromUtils) {
                if (counter >= 1) {
                    break;
                }
                if (order.getLevel() == 2) {
                    ((Stack<CustomerOrder>)customerDeck).push(order);
                    counter += 1;
                }
            }
            counter = 0;
            for (CustomerOrder order : outputFromUtils) {
                if (counter >= 6) {
                    break;
                }
                if (order.getLevel() == 3) {
                    ((Stack<CustomerOrder>)customerDeck).push(order);
                    counter += 1;
                }
            }
        }
        Collections.shuffle((Stack<CustomerOrder>)customerDeck, random);
    }

    /**
     * Returns all currently active customers
     * @return the Collection that contains all the currently active customers
     */
    public Collection<CustomerOrder> getActiveCustomers() {
        return activeCustomers;
    }

    /**
     * This returns the deck containing the customer cards
     * @return the customer deck
     */
    public Collection<CustomerOrder> getCustomerDeck() {
        return customerDeck;        
    }

    /**
     * This method takes a CustomerOrderStatus as a parameter and returns all the inactive customers with that status
     * @param status the status to be checked for
     * @return a Collection of all the inactive customers with the specified status
     */
    public Collection<CustomerOrder> getInactiveCustomersWithStatus(CustomerOrderStatus status) {
        //Iterate thru inactiveCustomers, 
        //identify which ones have the relevant status and then
        //add those to a colleciton
        Collection<CustomerOrder> relevantCustomers = new ArrayList<>();
        for (CustomerOrder currentOrder : inactiveCustomers) {
            CustomerOrderStatus currentStatus = currentOrder.getStatus();
            if (currentStatus == status) {
                relevantCustomers.add(currentOrder);
            }
        }
        return relevantCustomers;
    }

    /**
     * Checks whether the activeCustomers Collection is empty
     * @return the boolean variable representing whether or not the activeCustomers collection is empty
     */
    public boolean isEmpty() {
        int noOfOrders = 0;
        for (CustomerOrder order : activeCustomers) {
            if (order != null) {
                noOfOrders += 1;
            }
        }
        if (noOfOrders == 0) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Returns the inactive customers list
     * @return The list of inactive customers
     */
    public Collection<CustomerOrder> getInactiveCustomers() {
        return inactiveCustomers;
    }
    /**
     * Returns how many active customers there are
     * @return an int representing the size of activeCustomers
     */
    public int size() {
        int noOfCards = 0;
        for (CustomerOrder order : activeCustomers) {
            if (order != null) {
                noOfCards += 1;
            }
        }
        return noOfCards;
    }

    /**
     * Removes the specified customer from activeCustomers
     * @param customer the customer to be removed
     */
    public void remove(CustomerOrder customer) {
        ((List<CustomerOrder>)inactiveCustomers).add(customer);
        ((LinkedList<CustomerOrder>)activeCustomers).set(((LinkedList<CustomerOrder>)activeCustomers).indexOf(customer), null);
        if (!customerWillLeaveSoon()) {
            for (CustomerOrder o : activeCustomers) {
                if (o != null) {
                    o.setStatus(CustomerOrderStatus.WAITING);
                }
            }
        }
    }

    /**
     * This method takes a list of ingredients as a param and returns a collection of all the customer orders that can be fulfilled using that hand
     * @param hand the hand to be checked against
     * @return the collection of all fulfillable orders
     */
    public Collection<CustomerOrder> getFulfillable(List<Ingredient> hand) {
        Collection<CustomerOrder> returnList = new ArrayList<CustomerOrder>();
        for (CustomerOrder o : activeCustomers) {
            if (o != null) {
                if (o.canFulfill(hand)) {
                    returnList.add(o);
                }
            }
        }
        return returnList;
        // Collection<CustomerOrder> returnList = new ArrayList<>();
        // for (CustomerOrder currentCustomer : activeCustomers) {
        //     List<Ingredient> currentCustomerRecipe = currentCustomer.getRecipe();
        //     boolean ghilouChecker = true;
        //     for (Ingredient currentIngredient : currentCustomerRecipe) {
        //         if (hand.contains(currentIngredient)) {
        //             continue;
        //         }
        //         else {
        //             ghilouChecker = false;
        //         }
        //     }
        //     if (ghilouChecker) {
        //         returnList.add(currentCustomer);
        //     }
        //     else {
        //         continue;
        //     }
        // }
    }

    /**
     * Draws a card from customerDeck 
     * @return card drawn
     * @throws EmptyStackException thrown when deck is empty
     */
    public CustomerOrder drawCustomer() throws EmptyStackException{ 
        CustomerOrder returnOrder = null;
        if (((Stack<CustomerOrder>)customerDeck).isEmpty()) {
            throw new EmptyStackException();
        }
        else {
            return ((Stack<CustomerOrder>)customerDeck).pop();
        }
        


        
        
    }
    
    /**
     * Returns the rightmost order using a kind of reverse indexing
     * @return the order furtherst to the right
     */
    public CustomerOrder peek() {
        return ((LinkedList<CustomerOrder>)activeCustomers).get(2);
    }
    

    /**
     * Returns a boolean value representing whether a customer will leave at the end of the round or not
     * @return the boolean value described above
     */
    public boolean customerWillLeaveSoon() {
        if (((Stack<CustomerOrder>)customerDeck).isEmpty() == false) {
            if (size() == 3) {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            if (size() == 3) {
                return true;
            }
            if (size() == 2) {
                if (((LinkedList<CustomerOrder>)activeCustomers).get(0) == null) {
                    return true;
                }
                else {
                    return false;
                }
            }
            if (size() == 1) {
                if (((LinkedList<CustomerOrder>)activeCustomers).get(2) != null) {
                    return true;
                }
                else {
                    return false;
                }
            }
            if (size() == 0) {
                return false;
            }
            
        }
        return (Boolean)null;
    }

    /**
     * Makes space at the furthest left position
     * @return the order that was discarded
     */
    public CustomerOrder timePasses() {
        CustomerOrder returnOrder = null;
        CustomerOrder temp = null;
        if (((Stack<CustomerOrder>)customerDeck).isEmpty() == false) {
            if (size() == 3) {
                ((LinkedList<CustomerOrder>)activeCustomers).get(2).setStatus(CustomerOrderStatus.GIVEN_UP);
                inactiveCustomers.add(((LinkedList<CustomerOrder>)activeCustomers).get(2));
                returnOrder = ((LinkedList<CustomerOrder>)activeCustomers).get(2);
                ((LinkedList<CustomerOrder>)activeCustomers).set(2, ((LinkedList<CustomerOrder>)activeCustomers).get(1));
                ((LinkedList<CustomerOrder>)activeCustomers).set(1, ((LinkedList<CustomerOrder>)activeCustomers).get(0));
                ((LinkedList<CustomerOrder>)activeCustomers).set(0, null);                
            }
            else {
                if (size() == 2) {
                    int indexOfNull = 0;
                    for (int i = 0; i <= 2; i++) {
                        CustomerOrder o = ((LinkedList<CustomerOrder>)activeCustomers).get(i);
                        if (o == null) {
                            indexOfNull = ((LinkedList<CustomerOrder>)activeCustomers).indexOf(o);
                            break;
                        }
                    }
                    if (indexOfNull == 2) {
                        ((LinkedList<CustomerOrder>)activeCustomers).set(2, ((LinkedList<CustomerOrder>)activeCustomers).get(1));
                        ((LinkedList<CustomerOrder>)activeCustomers).set(1, ((LinkedList<CustomerOrder>)activeCustomers).get(0));
                        ((LinkedList<CustomerOrder>)activeCustomers).set(0, null);
                    }
                    if (indexOfNull == 1) {
                        ((LinkedList<CustomerOrder>)activeCustomers).set(1, ((LinkedList<CustomerOrder>)activeCustomers).get(0));
                        ((LinkedList<CustomerOrder>)activeCustomers).set(0, null);
                        (((LinkedList<CustomerOrder>)activeCustomers).get(1)).setStatus(CustomerOrderStatus.WAITING);
                        (((LinkedList<CustomerOrder>)activeCustomers).get(2)).setStatus(CustomerOrderStatus.WAITING);
                    }
                    if (indexOfNull == 0) {
                        (((LinkedList<CustomerOrder>)activeCustomers).get(1)).setStatus(CustomerOrderStatus.WAITING);
                        (((LinkedList<CustomerOrder>)activeCustomers).get(2)).setStatus(CustomerOrderStatus.WAITING);
                    }
                    
                }
                if (size() == 1) {
                    int indexOfOrder = 0;
                    for (int i = 0; i <= 2; i++) {
                        CustomerOrder e = ((LinkedList<CustomerOrder>)activeCustomers).get(i);
                        if (e != null) {
                            indexOfOrder = i;
                            break;
                        }
                    }
                    if (indexOfOrder == 0) {
                        ((LinkedList<CustomerOrder>)activeCustomers).set(1, ((LinkedList<CustomerOrder>)activeCustomers).get(0));
                        ((LinkedList<CustomerOrder>)activeCustomers).set(0, null);
                    }
                }
            }
        }
        else {
            if (size() == 3) {

                ((LinkedList<CustomerOrder>)activeCustomers).get(2).setStatus(CustomerOrderStatus.GIVEN_UP);
                inactiveCustomers.add(((LinkedList<CustomerOrder>)activeCustomers).get(2));
                returnOrder = ((LinkedList<CustomerOrder>)activeCustomers).get(2);
                ((LinkedList<CustomerOrder>)activeCustomers).set(2, ((LinkedList<CustomerOrder>)activeCustomers).get(1));
                ((LinkedList<CustomerOrder>)activeCustomers).set(1, ((LinkedList<CustomerOrder>)activeCustomers).get(0));
                ((LinkedList<CustomerOrder>)activeCustomers).set(0, null);
                (((LinkedList<CustomerOrder>)activeCustomers).get(2)).setStatus(CustomerOrderStatus.IMPATIENT);
                return returnOrder;
            }
            if (size() == 2) {
                int indexOfNull = 0;
                for (int i = 0; i <= 2; i++) {
                    CustomerOrder u = ((LinkedList<CustomerOrder>)activeCustomers).get(i);
                    if (u == null) {
                        indexOfNull = i;
                        break;
                    }
                }
                if (indexOfNull == 2) {
                    ((LinkedList<CustomerOrder>)activeCustomers).set(2, ((LinkedList<CustomerOrder>)activeCustomers).get(1));
                    ((LinkedList<CustomerOrder>)activeCustomers).set(1, ((LinkedList<CustomerOrder>)activeCustomers).get(0));
                    ((LinkedList<CustomerOrder>)activeCustomers).set(0, null);
                    (((LinkedList<CustomerOrder>)activeCustomers).get(2)).setStatus(CustomerOrderStatus.IMPATIENT);

                }
                if (indexOfNull == 1) {
                    ((LinkedList<CustomerOrder>)activeCustomers).set(1, ((LinkedList<CustomerOrder>)activeCustomers).get(0));
                    ((LinkedList<CustomerOrder>)activeCustomers).set(0, null);
                    (((LinkedList<CustomerOrder>)activeCustomers).get(2)).setStatus(CustomerOrderStatus.IMPATIENT);

                }
                if (indexOfNull == 0) {
                    ((LinkedList<CustomerOrder>)activeCustomers).get(2).setStatus(CustomerOrderStatus.GIVEN_UP);
                    inactiveCustomers.add(((LinkedList<CustomerOrder>)activeCustomers).get(2));
                    returnOrder = ((LinkedList<CustomerOrder>)activeCustomers).get(2);
                    ((LinkedList<CustomerOrder>)activeCustomers).set(2, ((LinkedList<CustomerOrder>)activeCustomers).get(1));
                    ((LinkedList<CustomerOrder>)activeCustomers).set(1, null);
                    (((LinkedList<CustomerOrder>)activeCustomers).get(2)).setStatus(CustomerOrderStatus.IMPATIENT);

                }
                return returnOrder;
            }
            if (size() == 1) {
                int indexOfOrder = 0;
                for (int i = 0; i <= 2; i++) {
                    CustomerOrder g = ((LinkedList<CustomerOrder>)activeCustomers).get(i);
                    if (g != null) {
                        indexOfOrder = i;
                        break;
                    }
                }
                if (indexOfOrder == 0) {
                    ((LinkedList<CustomerOrder>)activeCustomers).set(1, ((LinkedList<CustomerOrder>)activeCustomers).get(0));
                    ((LinkedList<CustomerOrder>)activeCustomers).set(0, null);
                }
                if (indexOfOrder == 1) {
                    ((LinkedList<CustomerOrder>)activeCustomers).set(2, ((LinkedList<CustomerOrder>)activeCustomers).get(1));
                    ((LinkedList<CustomerOrder>)activeCustomers).set(1, null);
                    (((LinkedList<CustomerOrder>)activeCustomers).get(2)).setStatus(CustomerOrderStatus.IMPATIENT);
                    // CustomerOrder temp7 = ((LinkedList<CustomerOrder>)activeCustomers).get(2);

                }
                if (indexOfOrder == 2) {
                    ((LinkedList<CustomerOrder>)activeCustomers).get(2).setStatus(CustomerOrderStatus.GIVEN_UP);
                    inactiveCustomers.add(((LinkedList<CustomerOrder>)activeCustomers).get(2));
                    returnOrder = ((LinkedList<CustomerOrder>)activeCustomers).get(2);
                    ((LinkedList<CustomerOrder>)activeCustomers).set(2, null);
                }
                return returnOrder;
            }
        }
        return returnOrder;        
    }

    

    /**
     * Adds customer card to board
     * @return card removed 
     * @throws EmptyStackException thrown when deck is empty
     */
    public CustomerOrder addCustomerOrder() throws EmptyStackException{
        CustomerOrder removedOrder = timePasses();
        CustomerOrder cardToAdd = null;
        try {
            cardToAdd = drawCustomer();
            ((LinkedList<CustomerOrder>)activeCustomers).set(0, cardToAdd);
            if (((Stack<CustomerOrder>)customerDeck).isEmpty() == false && size() == 3) {
                (((LinkedList<CustomerOrder>)activeCustomers).get(2)).setStatus(CustomerOrderStatus.IMPATIENT);
            }
            if (((Stack<CustomerOrder>)customerDeck).isEmpty() == true && size() == 3) {
                (((LinkedList<CustomerOrder>)activeCustomers).get(2)).setStatus(CustomerOrderStatus.IMPATIENT);
            }
            return removedOrder;
        }
        catch (EmptyStackException e) {

            throw e;
        }
    }

    // public void addCustomerOrderAtStart() {
    //     CustomerOrder orderToAdd = drawCustomer();
    //     ((LinkedList<CustomerOrder>)activeCustomers).add(orderToAdd);
    // }

    /**
     * Removes one customer from activeCustomer and places them in inactiveCustomers
     * @param order the customer to remove from activeCustomer
     */
    public void setInactive(CustomerOrder order) {
        activeCustomers.remove(order);
        inactiveCustomers.add(order);
    }

    // public void addNull() {
    //     ((LinkedList<CustomerOrder>)activeCustomers).add(null);
    // }


        
}