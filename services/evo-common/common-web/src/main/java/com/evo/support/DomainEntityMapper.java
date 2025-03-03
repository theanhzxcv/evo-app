package com.evo.support;

public interface DomainEntityMapper<D, E> {
    D toDomain(E entity);
    E toEntity(D domain);
}
