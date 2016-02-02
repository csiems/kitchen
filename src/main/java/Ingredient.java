import java.util.ArrayList;
import java.util.List;

import org.sql2o.*;

public class Ingredient {
  private int mId;
  private String mName;
  private String mUnit;
  private int mDesiredOnHand;
  private int mShelfLifeDays;

  public Ingredient(String name, String unit, int desiredOnHand, int shelfLifeDays) {
    mName = name;
    mUnit = unit;
    mDesiredOnHand = desiredOnHand;
    mShelfLifeDays = shelfLifeDays;
  }

  public int getId() {
    return mId;
  }

  public String getName() {
    return mName;
  }

  public String getUnit() {
    return mUnit;
  }

  public int getDesiredOnHand() {
    return mDesiredOnHand;
  }

  public int getShelfLife() {
    return mShelfLifeDays;
  }

  @Override
  public boolean equals(Object otherIngredient) {
    if(!(otherIngredient instanceof Ingredient)) {
      return false;
    } else {
      Ingredient newIngredient = (Ingredient) otherIngredient;
      return this.getId() == newIngredient.getId() &&
             this.getName().equals(newIngredient.getName()) &&
             this.getUnit().equals(newIngredient.getUnit()) &&
             this.getDesiredOnHand() == newIngredient.getDesiredOnHand() &&
             this.getShelfLife() == newIngredient.getShelfLife();
    }
  }

  public void save() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO ingredients (name, unit, desired_on_hand, " +
                   "shelf_life_days) VALUES (:name, :unit, :desired_on_hand, " +
                   ":shelf_life_days)";
      mId = (int) con.createQuery(sql, true)
        .addParameter("name", mName)
        .addParameter("unit", mUnit)
        .addParameter("desired_on_hand", mDesiredOnHand)
        .addParameter("shelf_life_days", mShelfLifeDays)
        .executeUpdate()
        .getKey();
    }
  }

  public static Ingredient find(int id) {
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT id AS mId, name AS mName, unit AS mUnit, " +
                   "desired_on_hand AS mDesiredOnHand, shelf_life_days AS " +
                   "mShelfLifeDays FROM ingredients WHERE id = :id";
      return con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Ingredient.class);
    }
  }

  public static List<Ingredient> all() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT id AS mId, name AS mName, unit AS mUnit, " +
                   "desired_on_hand AS mDesiredOnHand, shelf_life_days AS " +
                   "mShelfLifeDays FROM ingredients ORDER BY name ASC";
      return con.createQuery(sql).executeAndFetch(Ingredient.class);
    }
  }

  public void delete() {
    try (Connection con = DB.sql2o.open()) {
      String ingredientDelete = "DELETE FROM ingredients WHERE id = :id";
      con.createQuery(ingredientDelete)
        .addParameter("id", mId)
        .executeUpdate();
    }
    try (Connection con = DB.sql2o.open()) {
      String joinDelete = "DELETE FROM dishes_ingredients " +
                          "WHERE ingredient_id = :id";
      con.createQuery(ingredientDelete)
        .addParameter("ingredient_id", mId)
        .executeUpdate();
    }
  }

  public void update(String name, String unit, int desiredOnHand, int shelfLifeDays) {
    try (Connection con = DB.sql2o.open()) {
      String sql = "UPDATE ingredients SET name = :name, unit = :unit, " +
                   "desired_on_hand = :desiredOnHand, shelf_life_days = " +
                   ":shelfLifeDays WHERE id = :id";
      con.createQuery(sql)
        .addParameter("name", name)
        .addParameter("unit", unit)
        .addParameter("desired_on_hand", desiredOnHand)
        .addParameter("shelf_life_days", shelfLifeDays)
        .executeUpdate();
    }
  }


}