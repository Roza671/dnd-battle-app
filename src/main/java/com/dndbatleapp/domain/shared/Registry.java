package com.dndbatleapp.domain.shared;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Registry<ID, T extends Identifiable<ID>> {
  private final Map<ID, T> store = new ConcurrentHashMap<>();

  public T add(T entity) {
    store.put(entity.id(), entity);

    return entity;
  }

  public Optional<T> find(ID id) {
    return Optional.ofNullable(store.get(id));
  }

  public List<T> findAll() {
    return List.copyOf(store.values());
  }

  public boolean remove(ID id) {
    return store.remove(id) != null;
  }

  public long count() {
    return store.size();
  }

  public List<T> findBy(Predicate<? super T> predicate) {
    return store.values().stream().filter(predicate).toList();
  }

  public List<T> findSorted(Comparator<? super T> comparator) {
    return store.values().stream().sorted(comparator).toList();
  }

  public <R> List<R> project(Function<? super T, ? extends R> mapper) {
    return store.values().stream().map(mapper).collect(Collectors.toList());
  }

  public Optional<T> findFirst(Predicate<? super T> predicate) {
    return store.values().stream().filter(predicate).findFirst();
  }
}
