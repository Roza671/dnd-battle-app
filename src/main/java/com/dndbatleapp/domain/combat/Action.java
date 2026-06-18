package com.dndbatleapp.domain.combat;

public sealed interface Action permits Attack, CastDamageSpell, Defend, Heal, Skip {
}
