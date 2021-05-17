package com.project2.kitchentable.beans;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("reviews")
public class Reviews implements Serializable {
	private static final long serialVersionUID = -7568030855642391178L;
	
	@PrimaryKeyColumn(
			name="reviewid",
			ordinal=0,
			type=PrimaryKeyType.PARTITIONED)
	private UUID reviewId;
	@PrimaryKeyColumn(
			name="userid",
			ordinal=1,
			type=PrimaryKeyType.CLUSTERED)
	private UUID userId;
	@PrimaryKeyColumn(
			name="recipeid",
			ordinal=2,
			type=PrimaryKeyType.CLUSTERED)
	private UUID recipeId;
	@Column
	private double score;
	@Column
	private String body;

	public Reviews() {
		super();
	}
	
	public Reviews(UUID reviewId, UUID userId, UUID recipeId, double score, String body) {
		this.reviewId = reviewId;
		this.userId = userId;
		this.recipeId = recipeId;
		this.score = score;
		this.body = body;
	}

	public UUID getReviewId() {
		return reviewId;
	}

	public void setReviewId(UUID reviewId) {
		this.reviewId = reviewId;
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

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
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
		result = prime * result + ((reviewId == null) ? 0 : reviewId.hashCode());
		long temp;
		temp = Double.doubleToLongBits(score);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		Reviews other = (Reviews) obj;
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
		if (reviewId == null) {
			if (other.reviewId != null)
				return false;
		} else if (!reviewId.equals(other.reviewId))
			return false;
		if (Double.doubleToLongBits(score) != Double.doubleToLongBits(other.score))
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
		return "Reviews [reviewId=" + reviewId + ", userId=" + userId + ", recipeId=" + recipeId + ", score=" + score
				+ ", body=" + body + "]";
	}

}
