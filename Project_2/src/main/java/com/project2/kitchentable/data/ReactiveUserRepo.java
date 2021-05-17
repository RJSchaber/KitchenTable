package com.project2.kitchentable.data;

import java.util.UUID;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

import com.project2.kitchentable.beans.User;

import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

@Repository
public interface ReactiveUserRepo extends ReactiveCassandraRepository<User, String> {
	@AllowFiltering
	Mono<User> findByLastnameAndFirstname(String lastname, String firstname);
	@AllowFiltering
	Mono<User> findByUserID(UUID userID);
	Mono<Void> deleteByUserID(UUID id);
}