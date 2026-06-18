package com.dndnbatleapp.domain.creature;

import com.dndnbatleapp.domain.attribute.Attribute;

import java.util.Map;

public record CreatureStats(Map<Attribute, Integer> attributes) {
  public CreatureStats {
    attributes = Map.copyOf(attributes);
  }

  public Map<Attribute, Integer> get() {
    return Map.copyOf(attributes);
  }
}
