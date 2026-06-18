package com.dndbatleapp.domain.combat;

public sealed interface ActionResult permits AttackHit, AttackMiss, Defended, Healed, Skipped, SpellCast {
}
