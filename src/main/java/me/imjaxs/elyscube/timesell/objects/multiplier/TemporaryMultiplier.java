package me.imjaxs.elyscube.timesell.objects.multiplier;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @AllArgsConstructor
public class TemporaryMultiplier {
    private final UUID uniqueID;
    @Setter private double multiplier;
    @Setter private int seconds;
}
