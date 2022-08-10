package me.imjaxs.elyscube.timesell.objects.multiplier;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class PermissionMultiplier {
    private final String identifier, permission;
    private final int priority;
    private final double multiplier;
}
