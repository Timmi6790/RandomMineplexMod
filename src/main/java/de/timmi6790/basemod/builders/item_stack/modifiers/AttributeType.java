package de.timmi6790.basemod.builders.item_stack.modifiers;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AttributeType {
    GENERIC_MAX_HEALTH("generic.maxHealth"),
    GENERIC_FOLLOW_RANGE("generic.followRange"),
    GENERIC_ATTACK_DAMAGE("generic.attackDamage"),
    GENERIC_MOVEMENT_SPEED("generic.movementSpeed"),
    GENERIC_KNOCKBACK_RESISTANCE("generic.knockbackResistance");

    private final String id;
}
