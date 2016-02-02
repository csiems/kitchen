import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import org.sql2o.*;

public class Dish {
  private int mId;
  private String mName;

  public int getId() {
    return mId;
  }

  public String getName() {
    return mName;
  }

  public Dish(String name) {
    this.mName = name;
  }

  @Override
  public boolean equals(Object otherDish) {
    if(!(otherDish instanceof Dish)) {
      return false;
    } else {
      Dish newDish = (Dish) otherDish;
      return this.getName().equals(newDish.getName());
    }
  }

  public static List<Dish> all() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT id AS mId, name AS mName from dishes";
      return con.createQuery(sql)
                .executeAndFetch(Dish.class);
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO dishes(name) VALUES (:name)";
      this.mId = (int) con.createQuery(sql, true)
                          .addParameter("name", this.mName)
                          .executeUpdate()
                          .getKey();
    }
  }

  public static Dish find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT id AS mId, name AS mName FROM dishes WHERE id = :id";
      Dish dish = con.createQuery(sql)
                     .addParameter("id", id)
                     .executeAndFetchFirst(Dish.class);
      return dish;
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String sqlDeleteDishConnection = "DELETE FROM dishes_ingredients WHERE dishes_ingredients.dish_id = :id;";
      String sqlDeleteDish = "DELETE FROM dishes WHERE id = :id;";
      con.createQuery(sqlDeleteDishConnection + sqlDeleteDish)
         .addParameter("id", mId)
         .executeUpdate();
    }
  }

  public void update(String newName) {
    mName = newName;
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE dishes SET name = :name WHERE id = :id";
      con.createQuery(sql)
         .addParameter("name", newName)
         .addParameter("id", mId)
         .executeUpdate();
    }
  }

  public void addIngredient(int ingredientId, int ingredientAmount) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO dishes_ingredients (dish_id, ingredient_id, ingredient_amount) VALUES (:dishId, :ingredientId, :ingredientAmount)";
      con.createQuery(sql)
         .addParameter("dishId", this.getId())
         .addParameter("ingredientId", ingredientId)
         .addParameter("ingredientAmount", ingredientAmount)
         .executeUpdate();
    }
  }

  public List<Ingredient> getAllIngredients() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT ingredients.id AS mId, ingredients.name AS mName, ingredients.unit AS mUnit, ingredients.desired_on_hand as mDesiredOnHand, ingredients.shelf_life_days AS mShelfLifeDays FROM dishes_ingredients INNER JOIN ingredients ON dishes_ingredients.ingredient_id = ingredients.id WHERE dishes_ingredients.dish_id = :dishid";
      return con.createQuery(sql)
         .addParameter("dishid", mId)
         .executeAndFetch(Ingredient.class);
    }
  }


}