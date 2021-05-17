package com.project2.kitchentable.beans;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("recipe")
public class Recipe implements Serializable {
	private static final long serialVersionUID = -5825156404073366871L;
	@PrimaryKeyColumn(name = "recipeid", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private UUID recipeId;
	@PrimaryKeyColumn(name = "cuisine", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
	private int cuisine;
	@Column
	private String recipeName;
	@Column
	private Map<UUID, Double> ingredients;
	@Column
	private double rating;

	public Recipe() {
		super();
	}

	@SuppressWarnings("unchecked")
	public Recipe(UUID recipeId, int cuisine, String recipeName, Object[] ingredients, double rating) {
		this.recipeId = recipeId;
		this.cuisine = cuisine;
		this.recipeName = recipeName;
		this.ingredients = ArrayUtils.toMap(ingredients);
		this.rating = rating;
	}

	public UUID getRecipeId() {
		return recipeId;
	}

	public void setRecipeId(UUID recipeId) {
		this.recipeId = recipeId;
	}

	public String getRecipeName() {
		return recipeName;
	}

	public void setRecipeName(String recipeName) {
		this.recipeName = recipeName;
	}

	public int getCuisine() {
		return cuisine;
	}

	public void setCuisine(int cuisine) {
		this.cuisine = cuisine;
	}

	public Map<UUID, Double> getIngredients() {
		return ingredients;
	}

	public void setIngredients(Map<UUID, Double> ingredients) {
		this.ingredients = ingredients;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cuisine;
		result = prime * result + ((ingredients == null) ? 0 : ingredients.hashCode());
		long temp;
		temp = Double.doubleToLongBits(rating);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((recipeId == null) ? 0 : recipeId.hashCode());
		result = prime * result + ((recipeName == null) ? 0 : recipeName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Recipe other = (Recipe) obj;
		if (cuisine != other.cuisine)
			return false;
		if (ingredients == null) {
			if (other.ingredients != null)
				return false;
		} else if (!ingredients.equals(other.ingredients))
			return false;
		if (Double.doubleToLongBits(rating) != Double.doubleToLongBits(other.rating))
			return false;
		if (recipeId == null) {
			if (other.recipeId != null)
				return false;
		} else if (!recipeId.equals(other.recipeId))
			return false;
		if (recipeName == null) {
			if (other.recipeName != null)
				return false;
		} else if (!recipeName.equals(other.recipeName))
			return false;
		if (recipeId == null) {
			if (other.recipeId != null)
				return false;
		} else if (!recipeId.equals(other.recipeId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Recipe [recipeId=" + recipeId + ", cuisine=" + cuisine + ", recipeName=" + recipeName + ", ingredients="
				+ ingredients + ", rating=" + rating + "]";
	}

}
