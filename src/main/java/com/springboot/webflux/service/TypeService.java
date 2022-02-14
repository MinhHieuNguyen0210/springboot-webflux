package com.springboot.webflux.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TypeService {

    Flux<TypeService> findAll();

    Mono<TypeService> create();

    Mono<TypeService> update(Integer id, TypeService typeService);
}
