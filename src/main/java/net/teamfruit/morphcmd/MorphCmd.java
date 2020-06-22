package net.teamfruit.morphcmd;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod(
        modid = MorphCmd.MOD_ID,
        name = MorphCmd.MOD_NAME,
        version = MorphCmd.VERSION,
        acceptableRemoteVersions = "*"
)
public class MorphCmd {
    public static final String MOD_ID = "morphcmd";
    public static final String MOD_NAME = "MetaMorph-Command";
    public static final String VERSION = "1.0-SNAPSHOT";

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new MorphCmdCommand(this));
    }
}
