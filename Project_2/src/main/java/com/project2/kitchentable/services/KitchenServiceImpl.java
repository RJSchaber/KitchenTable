package com.project2.kitchentable.services;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project2.kitchentable.beans.Kitchen;
import com.project2.kitchentable.beans.Recipe;
import com.project2.kitchentable.data.ReactiveKitchenRepo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class KitchenServiceImpl implements KitchenService {
	private static Logger log = LogManager.getLogger(KitchenServiceImpl.class);
	private String shopping = "shopping";
	private String inventory = "inventory";

	@Autowired
	private ReactiveKitchenRepo kitchenRepo;

	@Override
	public Mono<Kitchen> addKitchen(Kitchen k) {
		return kitchenRepo.save(k);
	}

	public Flux<Kitchen> getKitchens() {
		return kitchenRepo.findAll();
	}

	public Mono<Kitchen> updateKitchen(Kitchen k) {
		return kitchenRepo.save(k);
	}

	public Mono<Kitchen> getKitchenByID(UUID id) {
		return kitchenRepo.findById(id);
	}

	public Mono<Void> removeKitchen(Kitchen k) {
		return kitchenRepo.delete(k);
	}

	@Override
	public Mono<Kitchen> removeFood(String listname, Kitchen k, UUID ingredient, Double amount) {
		Map<UUID, Double> list = new HashMap<>();
		if (listname.equals(shopping)) {
			if(k.getShoppingList() != null) {
				list = k.getShoppingList();
			}
		} else if (listname.equals(inventory)) {
			if(k.getInventory() != null) {
				list = k.getInventory();
			}
		}
		for (UUID iID : list.keySet()) {
			if (iID.equals(ingredient)) {
				double newAmt = list.get(iID) - amount;
				if (newAmt > 0) {
					list.replace(iID, newAmt);
					log.trace("Successfully removed " + amount + "of " + ingredient);
				} else if (newAmt == 0){
					list.remove(iID);
				} else {
					log.trace("Failed to remove " + amount + "of " + ingredient
							+ ". The requested amount was too large.");
				}
			}

		}
		if (listname.equals(shopping)) {
			k.setShoppingList(list);
		} else if (listname.equals(inventory)) {
			k.setInventory(list);
		}
		
		return updateKitchen(k);
	}

	@Override
	public Mono<Kitchen> addFood(String listname, Kitchen k, UUID ingredient, Double amt) {
		Map<UUID, Double> list = new HashMap<>();
		if (listname.equals(shopping) && k.getShoppingList() != null) {
			list = k.getShoppingList();
		} else if (listname.equals(inventory) && k.getInventory() != null) {
			list = k.getInventory();
		}
		if (list.putIfAbsent(ingredient, amt) != null) {
			for (UUID iID : list.keySet()) {
				if (iID.equals(ingredient)) {
					double newAmt = list.get(iID) + amt;
					list.replace(iID, newAmt);
				}
			}
		}
		if (listname.equals(shopping)) {
			k.setShoppingList(list);
		} else if (listname.equals(inventory)) {
			k.setInventory(list);
		}

		return updateKitchen(k);
	}

	@Override
	public Mono<Kitchen> cook(Recipe r, Kitchen k) {
		Map<UUID, Double> rIngredients = r.getIngredients();
		Map<UUID, Double> kIngredients = k.getInventory();

		for (UUID iID : rIngredients.keySet()) {
			if (kIngredients.containsKey(iID)) {
				if (kIngredients.get(iID) - rIngredients.get(iID) >= 0) {
					return this.removeFood(inventory, k, iID, rIngredients.get(iID)).delayElement(Duration.ofSeconds(2));
				} else {
					log.trace("Unable to cook recipe: Insufficient amount of ingredient " + iID.toString());
				}
			} else {
				log.trace("Unable to cook recipe: Missing one or more ingredients ");
			}

		}
		return Mono.just(k);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<UUID, Double> getShoppingList(String kitchenId) {
		Mono<Kitchen> userKitchen = kitchenRepo.findById(kitchenId);
		Map<UUID, Double> list = new HashMap<UUID, Double>();

		try {
			list = (Map<UUID, Double>) userKitchen.subscribe(Kitchen::getShoppingList);
		} catch (Exception e) {
			log.warn(e.getMessage());
			for (StackTraceElement st : e.getStackTrace())
				log.debug(st.toString());
			return null;
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<UUID, Double> getKitchenInv(String kitchenId) {
		Mono<Kitchen> userKitchen = kitchenRepo.findById(kitchenId);
		Map<UUID, Double> list = new HashMap<UUID, Double>();

		try {
			list = (Map<UUID, Double>) userKitchen.subscribe(Kitchen::getInventory);
		} catch (Exception e) {
			log.warn(e.getMessage());
			for (StackTraceElement st : e.getStackTrace())
				log.debug(st.toString());
			return null;
		}

		return list;
	}

}
