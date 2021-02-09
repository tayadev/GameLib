package one.taya.gamelib.commands;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.OfflinePlayer;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.CustomArgument;
import dev.jorel.commandapi.arguments.CustomArgument.CustomArgumentException;
import dev.jorel.commandapi.arguments.CustomArgument.MessageBuilder;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import net.md_5.bungee.api.ChatColor;
import one.taya.gamelib.GameLib;
import one.taya.gamelib.enums.GamePlayerType;
import one.taya.gamelib.game.Game;
import one.taya.gamelib.game.GamePlayer;

public class GameLibCommand {
    
    // TODO: permissions

    private static final String NONE = ChatColor.RED + "None";
    private static final String FALSE = ChatColor.RED + "False";
    private static final String TRUE = ChatColor.GREEN + "True";

    public GameLibCommand() {

        new CommandAPICommand("gamelib")
        .withAliases("gl")
        .withSubcommand(new CommandAPICommand("players")
            .withSubcommand(new CommandAPICommand("list")
                .withArguments(new MultiLiteralArgument("all", "online", "offline"))
                .executes((sender, args) -> {
                    Set<OfflinePlayer> allPlayers = GameLib.getPlayers().stream().map(GamePlayer::getPlayer).collect(Collectors.toSet());
                    Set<OfflinePlayer> players = new HashSet<OfflinePlayer>();
                    switch((String) args[0]) {
                        case "all":
                            players = allPlayers;
                            break;

                        case "online":
                            players = allPlayers.stream().filter((OfflinePlayer p) -> p.isOnline()).collect(Collectors.toSet());
                            break;

                        case "offline":
                            players = allPlayers.stream().filter((OfflinePlayer p) -> !p.isOnline()).collect(Collectors.toSet());
                            break;
                    }

                    sender.sendMessage(GameLib.getChatPrefix() + "Players: " + players.stream().map(OfflinePlayer::getName).collect(Collectors.joining(", ")));
                })
            )
            .withSubcommand(new CommandAPICommand("get")
                .withArguments(gamePlayerArgument("player"))
                .executes((sender, args) -> {
                    GamePlayer gamePlayer = (GamePlayer) args[0];
                    OfflinePlayer player = gamePlayer.getPlayer();
                    sender.sendMessage(new String[] {
                        GameLib.getChatPrefix() + "Info for player: " + ChatColor.AQUA + player.getName(),
                        GameLib.getChatPrefix() + "Online: " + (player.isOnline() ? TRUE : FALSE),
                        GameLib.getChatPrefix() + "Type: " + (gamePlayer.getType() != null ? gamePlayer.getType() : NONE),
                        GameLib.getChatPrefix() + "Team: " + (gamePlayer.getTeam() != null ? gamePlayer.getTeam().getName() + ChatColor.GRAY + "(" + gamePlayer.getTeam().getId() + ")" : NONE),
                        GameLib.getChatPrefix() + "Game: " + (gamePlayer.getGame() != null ? gamePlayer.getGame().getName() + ChatColor.GRAY + "(" + gamePlayer.getGame().getId() + ")" : NONE)
                    });
                })
            )
            .withSubcommand(new CommandAPICommand("set")
                .withSubcommand(new CommandAPICommand("type")
                    .withArguments(gamePlayerTypeArgument("gamePlayerType"))
                    .withArguments(gamePlayerArgument("player"))
                    .executes((sender, args) -> {
                        GamePlayerType gamePlayerType = (GamePlayerType) args[0];
                        GamePlayer gamePlayer = (GamePlayer) args[1];

                        gamePlayer.setType(gamePlayerType);

                        sender.sendMessage(GameLib.getChatPrefix() + "Set " + ChatColor.AQUA + gamePlayer.getPlayer().getName() + ChatColor.RESET + "'s type to " + args[0]);
                    })
                )
            )
        )
        .withSubcommand(new CommandAPICommand("games")
            .withSubcommand(new CommandAPICommand("list")
            .executes((sender, args) -> {
                sender.sendMessage(GameLib.getChatPrefix() + "Games: " + GameLib.getGames().stream().map(Game::getId).collect(Collectors.joining(", ")));
            })
            )
            .withSubcommand(new CommandAPICommand("get")
                .withArguments(gameArgument("game"))
                .executes((sender, args) -> {
                    Game game = (Game) args[0];
                    sender.sendMessage(new String[] {
                        GameLib.getChatPrefix() + "Info for game: " + game.getName(),
                        GameLib.getChatPrefix() + "Status: " + (game.getStatus() != null ? game.getStatus() : NONE)
                    });
                })
            )
        )
        .register();
        
    }

    public Argument gamePlayerArgument(String nodeName) {
    
        return new CustomArgument<GamePlayer>(nodeName, (input) -> {
            GamePlayer gamePlayer = GameLib.getPlayers().stream().filter((GamePlayer p) -> p.getPlayer().getName().equals(input)).findFirst().orElse(null);
        
            if(gamePlayer == null) {
                throw new CustomArgumentException(new MessageBuilder("Unknown Player: ").appendArgInput());
            } else {
                return gamePlayer;
            }
        }).overrideSuggestions(sender -> {
            return GameLib.getPlayers().stream().map(GamePlayer::getPlayer).map(OfflinePlayer::getName).toArray(String[]::new);
        });
    }

    public Argument gamePlayerTypeArgument(String nodeName) {
        return new CustomArgument<GamePlayerType>(nodeName, (input) -> {
            GamePlayerType gamePlayerType = GamePlayerType.valueOf(input);
        
            if(gamePlayerType == null) {
                throw new CustomArgumentException(new MessageBuilder("Unknown GamePlayerType: ").appendArgInput());
            } else {
                return gamePlayerType;
            }
        }).overrideSuggestions(sender -> {
            return Stream.of(GamePlayerType.values()).map(GamePlayerType::name).collect(Collectors.toSet()).toArray(new String[0]);
        });
    }

    public Argument gameArgument(String nodeName) {
    
        return new CustomArgument<Game>(nodeName, (input) -> {
            Game game = GameLib.getGames().stream().filter((Game g) -> g.getId().equals(input)).findFirst().orElse(null);
        
            if(game == null) {
                throw new CustomArgumentException(new MessageBuilder("Unknown Game: ").appendArgInput());
            } else {
                return game;
            }
        }).overrideSuggestions(sender -> {
            return GameLib.getGames().stream().map(Game::getId).toArray(String[]::new);
        });
    }

}
