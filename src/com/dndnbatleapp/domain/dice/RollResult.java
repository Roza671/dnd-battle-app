package com.dndnbatleapp.domain.dice;

import java.util.List;

public record RollResult(int total, List<Integer> rolls, int modifier) {
}
