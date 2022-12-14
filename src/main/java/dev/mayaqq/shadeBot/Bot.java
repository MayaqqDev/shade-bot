package dev.mayaqq.shadeBot;

import dev.mayaqq.shadeBot.messageCommands.MessageListener;
import dev.mayaqq.shadeBot.slashCommands.SlashCommandListener;
import dev.mayaqq.shadeBot.utils.Tenor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class Bot {
    public static void main(String[] args) throws InterruptedException {
        Config.load();

        //Check the token if its right in the console
        String token = "*";
        for (int i = 0; i < Config.CONFIG.token.length() - 7; i++) token += "*";
        System.out.println("Token: " + Config.CONFIG.token.substring(0, 3) + token +Config.CONFIG.token.substring(Config.CONFIG.token.length() - 3));

        JDABuilder jdaBotBuilder = JDABuilder.createDefault(Config.CONFIG.token, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES);

        jdaBotBuilder.addEventListeners(new SlashCommandListener(), new MessageListener());
        jdaBotBuilder.disableCache(CacheFlag.VOICE_STATE, CacheFlag.EMOJI, CacheFlag.STICKER, CacheFlag.SCHEDULED_EVENTS);

        JDA shadeBot = jdaBotBuilder.build();

        shadeBot.updateCommands().addCommands(
                Commands.slash("info", "Tells you general info about this server."),
                Commands.slash("embed", "Sends an embed with the given message.")
                        .addOption(OptionType.STRING, "title", "The title of the embed.", true)
                        .addOption(OptionType.STRING, "message", "The message of the embed.", true)
                        .addOption(OptionType.STRING, "color", "The color of the embed.", false)
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MANAGE_CHANNEL, Permission.MODERATE_MEMBERS)),
                Commands.slash("link", "embed a link!")
                        .addOption(OptionType.STRING, "title", "The title of the link", true)
                        .addOption(OptionType.STRING, "url", "The url of the link", true),
                Commands.slash("tracker", "Tracks total Modrinth downloads")
                        .addOption(OptionType.STRING, "userid", "The Modrinth userid to track", true)
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MANAGE_CHANNEL, Permission.MODERATE_MEMBERS)),
                Commands.slash("hug", "Hug someone!")
                        .addOption(OptionType.USER, "user", "The user to hug", true),
                Commands.slash("pat", "Pat someone!")
                        .addOption(OptionType.USER, "user", "The user to pat", true),
                Commands.slash("kiss", "Kiss someone!")
                        .addOption(OptionType.USER, "user", "The user to kiss", true),
                Commands.slash("cuddle", "Cuddle someone!")
                        .addOption(OptionType.USER, "user", "The user to cuddle", true),
                Commands.slash("highfive", "highfive someone!")
                        .addOption(OptionType.USER, "user", "The user to highfive", true),
                Commands.slash("randommod", "Get a random mod!")
                        .addOption(OptionType.BOOLEAN, "ephemeral", "Should the response be ephemeral?", false),
                Commands.slash("mod", "Get info about a mod!")
                        .addOption(OptionType.STRING, "slug", "The modrinth mod Slug", true),
                Commands.slash("user", "Get info about a user!")
                        .addOption(OptionType.STRING, "userid", "The modrinth user id", true)
        ).queue();

        shadeBot.awaitReady();

        //Set the status of the bot to the number of members the bot is watching over
        int memberCount = 0;

        for (int i = 0; i < shadeBot.getGuilds().size(); i++) {
            memberCount += shadeBot.getGuilds().get(i).getMemberCount();
        }
        if (memberCount == 1) {
            shadeBot.getPresence().setPresence(OnlineStatus.ONLINE, Activity.watching("over " + memberCount + " member!"));
        } else {
            shadeBot.getPresence().setPresence(OnlineStatus.ONLINE, Activity.watching("over " + memberCount + " members!"));
        }
    }
}
