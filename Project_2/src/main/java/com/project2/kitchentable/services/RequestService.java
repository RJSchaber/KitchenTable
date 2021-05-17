package com.project2.kitchentable.services;

import java.util.UUID;

import com.project2.kitchentable.beans.Requests;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RequestService {

	Mono<Requests> addRequest(Requests r);

	Flux<Requests> getRequests();

	Mono<Requests> updateRequest(Requests r);

	Mono<Requests> getRequestById(UUID id);

	Mono<Void> removeRequest(Requests r);

	Mono<Requests> approveOrReject(Requests q);

}