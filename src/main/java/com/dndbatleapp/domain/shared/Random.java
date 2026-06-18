package com.dndbatleapp.domain.shared;

@FunctionalInterface
public interface Random {
  int next(int max);
}
