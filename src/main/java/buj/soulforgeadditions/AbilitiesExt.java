package buj.soulforgeadditions;

import com.pulsar.soulforge.ability.AbilityBase;
import com.pulsar.soulforge.trait.TraitBase;
import com.pulsar.soulforge.trait.Traits;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;

public class AbilitiesExt {
    private AbilitiesExt() {}

    private static final HashMap<Identifier, TraitBase[]> DUALS = new HashMap<>();

    static {
        DUALS.put(
                Identifier.of("soulforge", "lightning_rod"),
                new TraitBase[] {
                        Traits.bravery,
                        Traits.justice,
                }
        );
        DUALS.put(
                Identifier.of("soulforge", "hestias_hearth"),
                new TraitBase[] {
                        Traits.bravery,
                        Traits.kindness,
                }
        );
        DUALS.put(
                Identifier.of("soulforge", "fearless_instincts"),
                new TraitBase[] {
                        Traits.bravery,
                        Traits.integrity,
                }
        );
        DUALS.put(
                Identifier.of("soulforge", "perfected_aura_technique"),
                new TraitBase[] {
                        Traits.bravery,
                        Traits.perseverance,
                }
        );
        DUALS.put(
                Identifier.of("soulforge", "stockpile"),
                new TraitBase[] {
                        Traits.bravery,
                        Traits.perseverance,
                }
        );
        DUALS.put(
                Identifier.of("soulforge", "friendliness_pellets"),
                new TraitBase[] {
                        Traits.justice,
                        Traits.kindness,
                }
        );
        DUALS.put(
                Identifier.of("soulforge", "reload"),
                new TraitBase[] {
                        Traits.justice,
                        Traits.patience,
                }
        );
        DUALS.put(
                Identifier.of("soulforge", "accelerated_pellet_aura"),
                new TraitBase[] {
                        Traits.justice,
                        Traits.integrity,
                }
        );
        DUALS.put(
                Identifier.of("soulforge", "armory"),
                new TraitBase[] {
                        Traits.justice,
                        Traits.perseverance,
                }
        );
        DUALS.put(
                Identifier.of("soulforge", "status_inversion"),
                new TraitBase[] {
                        Traits.kindness,
                        Traits.patience,
                }
        );
        DUALS.put(
                Identifier.of("soulforge", "enduring_heal"),
                new TraitBase[] {
                        Traits.kindness,
                        Traits.perseverance,
                }
        );
        DUALS.put(
                Identifier.of("soulforge", "nanomachines"),
                new TraitBase[] {
                        Traits.kindness,
                        Traits.perseverance,
                }
        );
        DUALS.put(
                Identifier.of("soulforge", "your_shield"),
                new TraitBase[] {
                        Traits.kindness,
                        Traits.perseverance,
                }
        );
        DUALS.put(
                Identifier.of("soulforge", "wormhole"),
                new TraitBase[] {
                        Traits.patience,
                        Traits.integrity,
                }
        );
    }

    private static final ArrayList<Identifier> WARNED_ABBS = new ArrayList<>();

    /**
     * Get traits of an ability. Will return 0-length array if ability was null or
     * if no trait could be resolved.
     */
    public static TraitBase[] getTraitsOf(AbilityBase ability) {
        if (ability == null) return new TraitBase[0];

        TraitBase[] traits = Traits
                .all()
                .stream()
                .filter(x -> x.getAbilities().stream().anyMatch(y -> y.getID().equals(ability.getID())))
                .toArray(TraitBase[]::new);
        if (traits.length > 0) return traits;

        traits = DUALS.get(ability.getID());
        if (traits != null && traits.length > 0) return traits;

        if (!WARNED_ABBS.contains(ability.getID())) {
            SoulForgeAdditions.LOG.warn("No traits defined for trait {}", ability.getID().toString());
            WARNED_ABBS.add(ability.getID());
        }

        return new TraitBase[0];
    }
}
