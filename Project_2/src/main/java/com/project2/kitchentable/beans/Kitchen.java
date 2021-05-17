package com.project2.kitchentable.beans;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("kitchen")
public class Kitchen implements Serializable{
	private static final long serialVersionUID = 9003969767524051063L;
	@PrimaryKeyColumn(
			name="id",
			ordinal=0,
			type=PrimaryKeyType.PARTITIONED)
	private UUID id;
	@PrimaryKeyColumn(
			name="headuser",
			ordinal=1,
			type=PrimaryKeyType.CLUSTERED)
	private UUID headUser;
	@Column
	private UUID familyID;
	@Column
	private Map<UUID, Double> shoppingList;
	@Column
	private Map<UUID, Double> inventory;
	
	public Kitchen() {
		super();
	}

	@SuppressWarnings("unchecked")
	public Kitchen(UUID id, UUID headUser, UUID familyID, Object[] shoppingList, Object[] inventory) {
		this.id = id;
		this.headUser = headUser;
		this.familyID = familyID;
		this.shoppingList = ArrayUtils.toMap(shoppingList);
		this.inventory = ArrayUtils.toMap(inventory);
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getHeadUser() {
		return headUser;
	}

	public void setHeadUser(UUID headUser) {
		this.headUser = headUser;
	}

	public UUID getFamilyID() {
		return familyID;
	}

	public void setFamilyID(UUID familyID) {
		this.familyID = familyID;
	}

	public Map<UUID, Double> getShoppingList() {
		return shoppingList;
	}

	public void setShoppingList(Map<UUID, Double> shoppingList) {
		this.shoppingList = shoppingList;
	}

	public Map<UUID, Double> getInventory() {
		return inventory;
	}

	public void setInventory(Map<UUID, Double> inventory) {
		this.inventory = inventory;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((familyID == null) ? 0 : familyID.hashCode());
		result = prime * result + ((headUser == null) ? 0 : headUser.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((inventory == null) ? 0 : inventory.hashCode());
		result = prime * result + ((shoppingList == null) ? 0 : shoppingList.hashCode());
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
		Kitchen other = (Kitchen) obj;
		if (familyID == null) {
			if (other.familyID != null)
				return false;
		} else if (!familyID.equals(other.familyID))
			return false;
		if (headUser == null) {
			if (other.headUser != null)
				return false;
		} else if (!headUser.equals(other.headUser))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (inventory == null) {
			if (other.inventory != null)
				return false;
		} else if (!inventory.equals(other.inventory))
			return false;
		if (shoppingList == null) {
			if (other.shoppingList != null)
				return false;
		} else if (!shoppingList.equals(other.shoppingList))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Kitchen [id=" + id + ", headUser=" + headUser + ", familyID=" + familyID + ", shoppingList="
				+ shoppingList + ", inventory=" + inventory + "]";
	}
	
	
}
