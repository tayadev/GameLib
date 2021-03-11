package one.taya.gamelib.commands;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.CustomArgument;
import dev.jorel.commandapi.arguments.CustomArgument.CustomArgumentException;
import dev.jorel.commandapi.arguments.CustomArgument.MessageBuilder;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import net.md_5.bungee.api.ChatColor;
import one.taya.gamelib.GameLib;
import one.taya.gamelib.enums.GamePlayerType;
import one.taya.gamelib.game.Area;
import one.taya.gamelib.game.Arena;
import one.taya.gamelib.game.Game;
import one.taya.gamelib.game.GamePlayer;
import one.taya.gamelib.game.Section;

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
        .withSubcommand(new CommandAPICommand("arenas")
            .withSubcommand(new CommandAPICommand("list")
                .withArguments(gameArgument("game"))
                .executes((sender, args) -> {
                    Game game = (Game) args[0];
                    sender.sendMessage(GameLib.getChatPrefix() + "Arenas for " + game.getName() + ": " + game.getArenas().stream().map(Arena::getId).collect(Collectors.joining(", ")));
                })
            )
            .withSubcommand(new CommandAPICommand("get")
                .withArguments(arenaArgument("arena"))
                .executes((sender, args) -> {
                    Arena arena = (Arena) args[0];
                    sender.sendMessage(new String[] {
                        GameLib.getChatPrefix() + "Info for arena: " + arena.getName()
                    });
                })
            )
        )
        .withSubcommand(new CommandAPICommand("areas")
            .withSubcommand(new CommandAPICommand("list")
                .withArguments(arenaArgument("arena"))
                .executes((sender, args) -> {
                    Arena arena = (Arena) args[0];
                    sender.sendMessage(GameLib.getChatPrefix() + "Areas for " + arena.getName() + ": " + arena.getAreas().stream().map(Area::getId).collect(Collectors.joining(", ")));
                })
            )
            .withSubcommand(new CommandAPICommand("get")
                .withArguments(areaArgument("area"))
                .executes((sender, args) -> {
                    Area area = (Area) args[0];
                    sender.sendMessage(new String[] {
                        GameLib.getChatPrefix() + "Info for area: " + area.getId()
                    });
                })
            )
        )
        .withSubcommand(new CommandAPICommand("sections")
            .withSubcommand(new CommandAPICommand("list")
                .withArguments(areaArgument("area"))
                .executes((sender, args) -> {
                    Area area = (Area) args[0];
                    sender.sendMessage(GameLib.getChatPrefix() + "Sections for " + area.getId() + ": " + area.getSections().stream().map(Section::getId).collect(Collectors.joining(", ")));
                })
            )
            .withSubcommand(new CommandAPICommand("get")
                .withArguments(sectionArgument("section"))
                .executes((sender, args) -> {
                    Section section = (Section) args[0];
                    sender.sendMessage(new String[] {
                        GameLib.getChatPrefix() + "Info for section: " + section.getId()
                    });
                })
            )
            .withSubcommand(new CommandAPICommand("visualize")
                .withArguments(sectionArgument("section"))
                .executesPlayer((executor, args) -> {
                    Player player = (Player) executor;
                    Section section = (Section) args[0];

                    section.visualize(player);
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

    public Argument arenaArgument(String nodeName) {
    
        return new CustomArgument<Arena>(nodeName, (input) -> {
            GameLib.logDebug(input, GameLib.getPlugin());
            String[] splitInput = input.split("\\.");
            GameLib.logDebug(splitInput[0], GameLib.getPlugin());
            GameLib.logDebug(splitInput[1], GameLib.getPlugin());
            Game game = GameLib.getGames().stream().filter((Game g) -> g.getId().equals(splitInput[0])).findFirst().orElse(null);
            Arena arena = game.getArenas().stream().filter((Arena a) -> a.getId().equals(splitInput[1])).findFirst().orElse(null);
        
            if(game == null || arena == null) {
                throw new CustomArgumentException(new MessageBuilder("Unknown Arena: ").appendArgInput());
            }else {
                return arena;
            }
        }).overrideSuggestions(sender -> {
            Set<String> suggestions = new HashSet<String>();

            for(Game game : GameLib.getGames()) {
                for(Arena arena : game.getArenas()) {
                    suggestions.add(game.getId() + "." + arena.getId());
                }
            }

            return suggestions.toArray(String[]::new);
        });
    }

    public Argument areaArgument(String nodeName) {

        return new CustomArgument<Area>(nodeName, (input) -> {
            String[] splitInput = input.split("\\.");
            Game game = GameLib.getGames().stream().filter((Game g) -> g.getId().equals(splitInput[0])).findFirst().orElse(null);
            Arena arena = game.getArenas().stream().filter((Arena a) -> a.getId().equals(splitInput[1])).findFirst().orElse(null);
            Area area = arena.getAreas().stream().filter((Area a) -> a.getId().equals(splitInput[2])).findFirst().orElse(null);
        
            if(game == null || arena == null || area == null) {
                throw new CustomArgumentException(new MessageBuilder("Unknown Area: ").appendArgInput());
            }else {
                return area;
            }
        }).overrideSuggestions(sender -> {
            Set<String> suggestions = new HashSet<String>();

            for(Game game : GameLib.getGames()) {
                for(Arena arena : game.getArenas()) {
                    for(Area area : arena.getAreas()) {
                        suggestions.add(game.getId() + "." + arena.getId() + "." + area.getId());
                    }
                }
            }

            return suggestions.toArray(String[]::new);
        });

    }

    public Argument sectionArgument(String nodeName) {

        return new CustomArgument<Section>(nodeName, (input) -> {
            String[] splitInput = input.split("\\.");
            Game game = GameLib.getGames().stream().filter((Game g) -> g.getId().equals(splitInput[0])).findFirst().orElse(null);
            Arena arena = game.getArenas().stream().filter((Arena a) -> a.getId().equals(splitInput[1])).findFirst().orElse(null);
            Area area = arena.getAreas().stream().filter((Area a) -> a.getId().equals(splitInput[2])).findFirst().orElse(null);
            Section section = area.getSections().stream().filter((Section s) -> s.getId().equals(splitInput[3])).findFirst().orElse(null);
        
            if(game == null || arena == null || area == null || section == null) {
                throw new CustomArgumentException(new MessageBuilder("Unknown Section: ").appendArgInput());
            }else {
                return section;
            }
        }).overrideSuggestions(sender -> {
            Set<String> suggestions = new HashSet<String>();

            for(Game game : GameLib.getGames()) {
                for(Arena arena : game.getArenas()) {
                    for(Area area : arena.getAreas()) {
                        for(Section section : area.getSections()) {
                            suggestions.add(game.getId() + "." + arena.getId() + "." + area.getId() + "." + section.getId());
                        }
                    }
                }
            }

            return suggestions.toArray(String[]::new);
        });

    }

}
