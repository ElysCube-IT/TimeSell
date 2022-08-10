package me.imjaxs.elyscube.timesell.objects.multiplier;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @AllArgsConstructor
public class Multiplier {
    private final UUID uniqueID;
    @Setter private double multiplier;
}
