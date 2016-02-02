import java.util.Arrays;
import java.util.List;

import org.junit.*;
import static org.junit.Assert.*;

public class IngredientTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void all_emptyAtFirst() {
    assertEquals(0, Ingredient.all().size());
  }

  @Test
  public void ingredientInitiatesCorrectly() {
    Ingredient ingredient = new Ingredient("Flour", "ounce", 800, 180);
    ingredient.save();
    Ingredient savedIngredient = Ingredient.find(ingredient.getId());
    assertEquals("Flour", ingredient.getName());
    assert(ingredient.equals(savedIngredient));
  }

  @Test
  public void delete_ingredientIsDeleted() {
    Ingredient ingredient = new Ingredient("Flour", "ounce", 800, 180);
    ingredient.save();
    ingredient.delete();
    assertEquals(0, Ingredient.all().size());
  }

  @Test
  public void update_ingredientIsUpdated() {
    Ingredient ingredient = new Ingredient("Flour", "ounce", 800, 180);
    ingredient.save();
    ingredient.update("Apple", "apple", 50, 21);
    assertEquals("Apple", Ingredient.find(ingredient.getId()).getName());
  }

  @Test
  public void getInventories_inventoriesAttachedToIngredientAreReturned() {
    Ingredient ingredient = new Ingredient("Flour", "ounce", 800, 180);
    ingredient.save();
    Inventory inventory = new Inventory(ingredient.getId(), 50);
    inventory.save();
    assertEquals(1, ingredient.getInventories().size());
  }

  @Test
  public void getMostRecentExpiration_returnsMostRecentExpiration() {
    Ingredient ingredient = new Ingredient("Flour", "ounce", 800, 180);
    ingredient.save();
    Inventory firstInventory = new Inventory(ingredient.getId(), 50);
    firstInventory.save();
    Inventory secondInventory = new Inventory(ingredient.getId(), 40);
    secondInventory.save();
    Inventory thirdInventory = new Inventory(ingredient.getId(), 2);
    thirdInventory.save();
    thirdInventory.updateExpiration("2000-01-01");
    assertEquals(Inventory.find(thirdInventory.getId()).getExpirationDate(), Ingredient.find(ingredient.getId()).getMostRecentExpiration());
  }

  @Test
  public void getTotalAmountOnHand_returnsCorrectAmount() {
    Ingredient ingredient = new Ingredient("Flour", "ounce", 800, 180);
    ingredient.save();
    Inventory firstInventory = new Inventory(ingredient.getId(), 50);
    firstInventory.save();
    Inventory secondInventory = new Inventory(ingredient.getId(), 40);
    secondInventory.save();
    Inventory thirdInventory = new Inventory(ingredient.getId(), 2);
    thirdInventory.save();
    thirdInventory.updateExpiration("2000-01-01");
    assertEquals(92, ingredient.getTotalOnHand());
  }


}
