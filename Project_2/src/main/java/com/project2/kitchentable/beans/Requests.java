package com.project2.kitchentable.beans;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("requests")
public class Requests implements Serializable{
	private static final long serialVersionUID = 5876575538166852444L;

	@PrimaryKeyColumn(
			name="requestid",
			ordinal=0,
			type=PrimaryKeyType.PARTITIONED)
	private UUID requestId;
	@PrimaryKeyColumn(
			name="recipeid",
			ordinal=1,
			type=PrimaryKeyType.CLUSTERED)
	private UUID recipeId;
	@Column
	private int cuisine;
	@Column
	private Map<UUID, Double> ingredients;
	@Column
	private String name;
	@Column
	private double rating;
	@Column
	private String body;

	public Requests() {
		super();
	}
	
	@SuppressWarnings("unchecked")
	public Requests(UUID requestId, UUID recipeId, int cuisine, Object[] ingredients, String name, double rating,
			String body) {
		this.requestId = requestId;
		this.recipeId = recipeId;
		this.cuisine = cuisine;
		this.ingredients = ArrayUtils.toMap(ingredients);
		this.name = name;
		this.rating = rating;
		this.body = body;
	}

	public UUID getRequestId() {
		return requestId;
	}
	
	public void setRequestId(UUID requestId) {
		this.requestId = requestId;
	}
	
	public UUID getRecipeId() {
		return recipeId;
	}
	
	public void setRecipeId(UUID recipeId) {
		this.recipeId = recipeId;
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
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public double getRating() {
		return rating;
	}
	
	public void setRating(double rating) {
		this.rating = rating;
	}
	
	public String getBody() {
		return body;
	}
	
	public void setBody(String body) {
		this.body = body;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result + ((ingredients == null) ? 0 : ingredients.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		long temp;
		temp = Double.doubleToLongBits(rating);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((recipeId == null) ? 0 : recipeId.hashCode());
		result = prime * result + ((requestId == null) ? 0 : requestId.hashCode());
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
		Requests other = (Requests) obj;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (ingredients == null) {
			if (other.ingredients != null)
				return false;
		} else if (!ingredients.equals(other.ingredients))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (Double.doubleToLongBits(rating) != Double.doubleToLongBits(other.rating))
			return false;
		if (recipeId == null) {
			if (other.recipeId != null)
				return false;
		} else if (!recipeId.equals(other.recipeId))
			return false;
		if (requestId == null) {
			if (other.requestId != null)
				return false;
		} else if (!requestId.equals(other.requestId))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Requests [requestId=" + requestId + ", recipeId=" + recipeId + ", cuisine=" + cuisine + ", ingredients="
				+ ingredients + ", name=" + name + ", rating=" + rating + ", body=" + body + "]";
	}
}
