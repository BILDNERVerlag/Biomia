package de.biomia.spigot.tools;

import com.chaseoes.voidworld.VoidWorld;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class VoidWorldGenerator {
    //TODO: fertigmachen, testen
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return new Generator();
    }

    class Generator extends ChunkGenerator {

        public List<BlockPopulator> getDefaultPopulators(World world) {
            return Collections.emptyList();
        }

        public boolean canSpawn(World world, int x, int z) {
            return true;
        }

        //TODO: evtl ändern? eigentlich stand da ?
        public byte[] generate(World world, Random rand, int chunkx, int chunkz) {
            return new byte['0'];
        }

        public Location getFixedSpawnLocation(World world, Random random) {
            return new Location(world, 0.0D, 128.0D, 0.0D);
        }
    }
}
