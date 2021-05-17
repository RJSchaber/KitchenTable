package com.project2.kitchentable.beans;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("notes")
public class Notes implements Serializable{
	private static final long serialVersionUID = -5753279626984652956L;

	@PrimaryKeyColumn(
			name="userid",
			ordinal=1,
			type = PrimaryKeyType.CLUSTERED)
	private UUID userId;
	@PrimaryKeyColumn(
			name="recipeid",
			ordinal=0,
			type = PrimaryKeyType.PARTITIONED)
	private UUID recipeId;
	@Column
	private String body;
	
	public Notes() {
		super();
	}

	public Notes(UUID userId, UUID recipeId, String body) {
		this.userId = userId;
		this.recipeId = recipeId;
		this.body = body;
	}
	
	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public UUID getRecipeId() {
		return recipeId;
	}

	public void setRecipeId(UUID recipeId) {
		this.recipeId = recipeId;
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
		result = prime * result + ((recipeId == null) ? 0 : recipeId.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
		Notes other = (Notes) obj;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (recipeId == null) {
			if (other.recipeId != null)
				return false;
		} else if (!recipeId.equals(other.recipeId))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Notes [userId=" + userId + ", recipeId=" + recipeId + ", body=" + body + "]";
	}

	
}
