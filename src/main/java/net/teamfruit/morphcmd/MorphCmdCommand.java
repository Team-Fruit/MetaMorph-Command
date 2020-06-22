package net.teamfruit.morphcmd;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketSpawnMob;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.StringUtils;
import scala.actors.threadpool.Arrays;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MorphCmdCommand extends CommandBase {
    private final MorphCmd mod;

    public MorphCmdCommand(MorphCmd mod) {
        this.mod = mod;
    }

    @Override
    public String getName() {
        return "morphx";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "/morphx <player> <entity> [nbt]";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            sender.sendMessage(new TextComponentString("/crash <player>"));
            return;
        }
        List<EntityPlayerMP> players = getPlayers(server, sender, args[0]);
        if (players.size() == 0)
            throw new PlayerNotFoundException("commands.generic.player.notFound", args[0]);
        for (EntityPlayerMP player : players) {
//            crash(player);
//            sender.sendMessage(new TextComponentString(TextFormatting.DARK_GREEN + "[" + TextFormatting.DARK_GREEN + "★" + TextFormatting.DARK_GREEN + "] " + TextFormatting.GREEN + args[0] + "をクラッシュさせました"));
//            server.getPlayerList().sendMessage(new TextComponentString(TextFormatting.DARK_PURPLE + "[★] " + TextFormatting.LIGHT_PURPLE + sender.getName() + "が" + TextFormatting.RED + args[0] + "をクラッシュさせました"));
        }
    }

    @Override public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 1) {
            String arg = args[0];
            List<String> players = Arrays.asList(server.getOnlinePlayerNames());
            if (arg.length() > 0)
                players = players.stream().filter(e -> StringUtils.startsWithIgnoreCase(e, arg)).collect(Collectors.toList());
            return players;
        }
        return Collections.emptyList();
    }
}
