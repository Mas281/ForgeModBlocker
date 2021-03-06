package me.itsmas.forgemodblocker.mods;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * Data holding a player's mods and their versions
 */
public class ModData
{
    /**
     * Map of mod IDs to versions
     */
    private final Map<String, String> mods;

    public ModData(Map<String, String> mods)
    {
        this.mods = mods;
    }

    /**
     * Fetches the list of mod IDs
     *
     * @see #mods
     * @return An immutable set of mod IDs
     */
    public Set<String> getMods()
    {
        return Collections.unmodifiableSet(mods.keySet());
    }

    /**
     * Fetches the mods map
     *
     * @see #mods
     * @return The mods map
     */
    public Map<String, String> getModsMap()
    {
        return Collections.unmodifiableMap(mods);
    }
}
