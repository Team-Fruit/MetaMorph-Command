package net.teamfruit.morphcmd;

import mchorse.metamorph.api.MorphAPI;
import mchorse.metamorph.api.MorphList;
import mchorse.metamorph.api.MorphManager;
import mchorse.metamorph.api.morphs.AbstractMorph;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.List;
import java.util.Map;

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
        return "metamorph.commands.morph";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }

    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return sender instanceof CommandBlockBaseLogic || super.checkPermission(server, sender);
    }

    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1) {
            throw new WrongUsageException(this.getUsage(sender));
        } else {
            List<EntityPlayerMP> players = getPlayers(server, sender, args[0]);

            if (args.length < 2) {
                for (EntityPlayerMP player : players)
                    MorphAPI.demorph(player);

                sender.sendMessage(new TextComponentTranslation("metamorph.success.demorph", args[0]));
            } else {
                AbstractMorph morph = null;

                if (args.length < 3 || NumberUtils.isDigits(args[2])) {
                    int value = 0;
                    if (args.length >= 3)
                        value = NumberUtils.toInt(args[2], 0);

                    Map<String, List<MorphList.MorphCell>> morphs = MorphManager.INSTANCE.getMorphs(sender.getEntityWorld()).morphs;
                    List<MorphList.MorphCell> list = morphs.get(args[1]);
                    if (list == null || list.isEmpty())
                        list = morphs.get(new ResourceLocation(args[1]).toString());
                    if (!(list == null || list.isEmpty())) {
                        if (value < 0 || list.size() <= value)
                            throw new WrongUsageException("エンティティIDが範囲外です");
                        NBTTagCompound nbt = new NBTTagCompound();
                        list.get(value).morph.toNBT(nbt);
                        morph = MorphManager.INSTANCE.morphFromNBT(nbt);
                    }
                }

                if (morph == null) {
                    NBTTagCompound tag = null;

                    if (args.length >= 3) {
                        try {
                            tag = JsonToNBT.getTagFromJson(mergeArgs(args, 2));
                        } catch (Exception var8) {
                            throw new CommandException("metamorph.error.morph.nbt", new Object[]{var8.getMessage()});
                        }
                    }

                    if (tag == null) {
                        tag = new NBTTagCompound();
                    }

                    morph = MorphManager.INSTANCE.morphFromNBT(tag);
                }

                for (EntityPlayerMP player : players)
                    MorphAPI.morph(player, morph, true);

                sender.sendMessage(new TextComponentTranslation("metamorph.success.morph", args[0], args[1]));
            }
        }
    }

    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames()) : super.getTabCompletions(server, sender, args, pos);
    }

    public static String mergeArgs(String[] args, int i) {
        String dataTag;
        for (dataTag = ""; i < args.length; ++i) {
            dataTag = dataTag + args[i] + (i == args.length - 1 ? "" : " ");
        }

        return dataTag;
    }
}
