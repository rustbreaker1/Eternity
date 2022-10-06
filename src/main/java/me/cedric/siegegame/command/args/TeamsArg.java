package me.cedric.siegegame.command.args;

import me.cedric.siegegame.SiegeGame;
import me.cedric.siegegame.player.GamePlayer;
import me.cedric.siegegame.teams.Team;
import me.deltaorion.common.command.CommandException;
import me.deltaorion.common.command.FunctionalCommand;
import me.deltaorion.common.command.sent.SentCommand;

public class TeamsArg extends FunctionalCommand {

    private final SiegeGame plugin;

    public TeamsArg(SiegeGame plugin) {
        super("siegegame.admin.start");
        this.plugin = plugin;
    }

    @Override
    public void commandLogic(SentCommand sentCommand) throws CommandException {
        for (Team team : plugin.getGameManager().getCurrentMap().getTeams()) {
            for (GamePlayer player : team.getPlayers()) {
                sentCommand.getSender().sendMessage("TEAM " + team.getName() + ": " + player.getBukkitPlayer().getName());
            }
        }
    }

}
