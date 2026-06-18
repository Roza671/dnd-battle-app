package com.dndbatleapp.domain.creature;

import com.dndbatleapp.domain.attribute.Attribute;

import java.util.Map;

public record CreatureStats(Map<Attribute, Integer> attributes) {
  public CreatureStats {
    attributes = Map.copyOf(attributes);
  }

  public int modifier(Attribute attribute) {
    Integer value = attributes.get(attribute);

    if (value == null) {
      throw new IllegalStateException("Missing attribute: " + attribute);
    }

    return Math.floorDiv(value - 10, 2);
  }
}
