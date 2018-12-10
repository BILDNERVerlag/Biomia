package de.biomia.spigot.tools;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

public class VoidWorldGenerator extends ChunkGenerator {

    //FIXME: ChunkData no longer exists (?)

//    public final ChunkGenerator.ChunkData generateChunkData(World world, Random random, int x, int z, ChunkGenerator.BiomeGrid biome) {
//        ChunkGenerator.ChunkData chunkData = this.createChunkData(world);
//        for (int i = 0; i < 16; ++i) {
//            for (int j = 0; j < 16; ++j) {
//                biome.setBiome(i, j, Biome.VOID);
//            }
//        }
//        return chunkData;
//    }

    public final Location getFixedSpawnLocation(World world, Random random) {
        return new Location(world, 0, 65, 0);
    }
}
