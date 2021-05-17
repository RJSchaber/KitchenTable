package com.project2.kitchentable.data;

import java.util.UUID;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import com.project2.kitchentable.beans.Kitchen;

import reactor.core.publisher.Mono;

@Repository
public interface ReactiveKitchenRepo extends ReactiveCassandraRepository<Kitchen, String>{
	@AllowFiltering
	Mono<Kitchen> findById(UUID id);

}