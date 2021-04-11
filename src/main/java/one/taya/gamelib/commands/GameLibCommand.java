package one.taya.gamelib.commands;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.CustomArgument;
import dev.jorel.commandapi.arguments.LocationArgument;
import dev.jorel.commandapi.arguments.CustomArgument.CustomArgumentException;
import dev.jorel.commandapi.arguments.CustomArgument.MessageBuilder;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import net.md_5.bungee.api.ChatColor;
import one.taya.gamelib.GameLib;
import one.taya.gamelib.enums.AreaFlag;
import one.taya.gamelib.enums.GameFlag;
import one.taya.gamelib.enums.GamePlayerType;
import one.taya.gamelib.game.Area;
import one.taya.gamelib.game.Arena;
import one.taya.gamelib.game.Game;
import one.taya.gamelib.game.GamePlayer;
import one.taya.gamelib.game.Section;
import one.taya.gamelib.game.Team;
import one.taya.gamelib.managers.GameManager;
import one.taya.gamelib.utils.ConfigUtil;

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
                        GameLib.getChatPrefix() + ChatColor.GRAY + "Info for player: " + ChatColor.RESET + ChatColor.AQUA + player.getName(),
                        GameLib.getChatPrefix() + ChatColor.GRAY + "Online: " + ChatColor.RESET + (player.isOnline() ? TRUE : FALSE),
                        GameLib.getChatPrefix() + ChatColor.GRAY + "Type: " + ChatColor.RESET + (gamePlayer.getType() != null ? gamePlayer.getType() : NONE),
                        GameLib.getChatPrefix() + ChatColor.GRAY + "Team: " + ChatColor.RESET + (gamePlayer.getTeam() != null ? gamePlayer.getTeam().getName() + ChatColor.GRAY + "(" + gamePlayer.getTeam().getId() + ")" : NONE),
                    });
                })
            )
            .withSubcommand(new CommandAPICommand("set")
                .withSubcommand(new CommandAPICommand("type")
                    .withArguments(gamePlayerArgument("player"))
                    .withArguments(gamePlayerTypeArgument("gamePlayerType"))
                    .executes((sender, args) -> {
                        GamePlayer gamePlayer = (GamePlayer) args[0];
                        GamePlayerType gamePlayerType = (GamePlayerType) args[1];

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
                        GameLib.getChatPrefix() + ChatColor.GRAY + "Info for game: " + ChatColor.RESET + game.getName(),
                        GameLib.getChatPrefix() + ChatColor.GRAY + "Status: " + ChatColor.RESET + (game.getStatus() != null ? game.getStatus() : NONE),
                        GameLib.getChatPrefix() + ChatColor.GRAY + "Flags: " + ChatColor.RESET + game.getFlags().stream().map(GameFlag::name).collect(Collectors.joining(", "))
                    });
                })
            )
            .withSubcommand(new CommandAPICommand("set")
                .withSubcommand(new CommandAPICommand("flags")
                    .withSubcommand(new CommandAPICommand("add")
                        .withArguments(gameArgument("game"))
                        .withArguments(gameFlagArgument("gameFlag"))
                        .executes((sender, args) -> {
                            Game game = (Game) args[0];
                            GameFlag flag = (GameFlag) args[1];
                            game.getFlags().add(flag);
                            sender.sendMessage(GameLib.getChatPrefix() + ChatColor.GREEN + "Added " + flag.name());
                        })
                    )
                    .withSubcommand(new CommandAPICommand("remove")
                        .withArguments(gameArgument("game"))
                        .withArguments(gameFlagArgument("gameFlag"))
                        .executes((sender, args) -> {
                            Game game = (Game) args[0];
                            GameFlag flag = (GameFlag) args[1];
                            game.getFlags().remove(flag);
                            sender.sendMessage(GameLib.getChatPrefix() + ChatColor.GREEN + "Removed " + flag.name());
                        })
                    )
                )
            )
            .withSubcommand(new CommandAPICommand("save")
                .withArguments(gameArgument("game"))
                .executes((sender, args) -> {
                    Game game = (Game) args[0];
                    game.getPlugin().getConfig().set(game.getId(), game);
                    game.getPlugin().saveConfig();
                    sender.sendMessage(GameLib.getChatPrefix() + "Saved " + game.getName() + ChatColor.RESET + " to the config");
                })
            )
            .withSubcommand(new CommandAPICommand("load")
                .withArguments(gameArgument("game"))
                .executes((sender, args) -> {
                    Game game = (Game) args[0];
                    Game loadedGame = ConfigUtil.loadGame(game.getPlugin().getConfig(), game.getId(), game.getPlugin());
                    game = loadedGame;
                    sender.sendMessage(GameLib.getChatPrefix() + "Loaded " + game.getName()  + ChatColor.RESET + " from the config");
                })
            )
            .withSubcommand(new CommandAPICommand("reset")
                .withArguments(gameArgument("game"))
                .executes((sender, args) -> {
                    Game game = (Game) args[0];
                    game.getPlugin().saveDefaultConfig();
                    Game loadedGame = (Game) game.getPlugin().getConfig().get("game");
                    game = loadedGame;
                    sender.sendMessage(GameLib.getChatPrefix() + "Reset and reloaded config for " + game.getName());
                })
            )
        )
        .withSubcommand(new CommandAPICommand("arenas")
            .withSubcommand(new CommandAPICommand("list")
                .withArguments(gameArgument("game"))
                .executes((sender, args) -> {
                    Game game = (Game) args[0];
                    sender.sendMessage(GameLib.getChatPrefix() + "Arenas for " + game.getName() + ChatColor.RESET + ": " + game.getArenas().stream().map(Arena::getId).collect(Collectors.joining(", ")));
                })
            )
            .withSubcommand(new CommandAPICommand("get")
                .withArguments(arenaArgument("arena"))
                .executes((sender, args) -> {
                    Arena arena = (Arena) args[0];
                    sender.sendMessage(new String[] {
                        GameLib.getChatPrefix() + ChatColor.GRAY + "Info for arena: " + ChatColor.RESET + arena.getName(),
                        GameLib.getChatPrefix() + ChatColor.GRAY + "Flags: " + ChatColor.RESET + arena.getFlags().stream().map(AreaFlag::name).collect(Collectors.joining(", "))
                    });
                })
            )
            .withSubcommand(new CommandAPICommand("set")
                .withSubcommand(new CommandAPICommand("flags")
                    .withSubcommand(new CommandAPICommand("add")
                        .withArguments(arenaArgument("arena"))
                        .withArguments(areaFlagArgument("areaFlag"))
                        .executes((sender, args) -> {
                            Arena arena = (Arena) args[0];
                            AreaFlag flag = (AreaFlag) args[1];
                            arena.getFlags().add(flag);
                            sender.sendMessage(GameLib.getChatPrefix() + ChatColor.GREEN + "Added " + flag.name());
                        })
                    )
                    .withSubcommand(new CommandAPICommand("remove")
                        .withArguments(arenaArgument("arena"))
                        .withArguments(areaFlagArgument("areaFlag"))
                        .executes((sender, args) -> {
                            Arena arena = (Arena) args[0];
                            AreaFlag flag = (AreaFlag) args[1];
                            arena.getFlags().remove(flag);
                            sender.sendMessage(GameLib.getChatPrefix() + ChatColor.GREEN + "Removed " + flag.name());
                        })
                    )
                )
            )
            .withSubcommand(new CommandAPICommand("queryflags")
                .withArguments(arenaArgument("arena"))
                .withArguments(new LocationArgument("location"))
                .executes((sender, args) -> {
                    Arena arena = (Arena) args[0];
                    Location location = (Location) args[1];
                    Set<AreaFlag> flags = arena.getFlagsForLocation(location);
                    sender.sendMessage(new String[] {
                        GameLib.getChatPrefix() + ChatColor.GRAY + "Flags for arena " + ChatColor.RESET + arena.getName() + ChatColor.RESET + " at " + ChatColor.AQUA + location.getX() + " " + location.getY() + " " + location.getZ(),
                        GameLib.getChatPrefix() + ChatColor.RESET + flags.stream().map(AreaFlag::name).collect(Collectors.joining(", "))
                    });
                })
            )
        )
        .withSubcommand(new CommandAPICommand("areas")
            .withSubcommand(new CommandAPICommand("list")
                .withArguments(arenaArgument("arena"))
                .executes((sender, args) -> {
                    Arena arena = (Arena) args[0];
                    sender.sendMessage(GameLib.getChatPrefix() + "Areas for " + arena.getName() + ChatColor.RESET + ": " + arena.getAreas().stream().map(Area::getId).collect(Collectors.joining(", ")));
                })
            )
            .withSubcommand(new CommandAPICommand("get")
                .withArguments(areaArgument("area"))
                .executes((sender, args) -> {
                    Area area = (Area) args[0];
                    sender.sendMessage(new String[] {
                        GameLib.getChatPrefix() + ChatColor.GRAY + "Info for area: " + ChatColor.RESET + area.getId(),
                        GameLib.getChatPrefix() + ChatColor.GRAY + "Flags: " + ChatColor.RESET + area.getFlags().stream().map(AreaFlag::name).collect(Collectors.joining(", "))
                    });
                })
            )
            .withSubcommand(new CommandAPICommand("set")
                .withSubcommand(new CommandAPICommand("flags")
                    .withSubcommand(new CommandAPICommand("add")
                        .withArguments(areaArgument("area"))
                        .withArguments(areaFlagArgument("areaFlag"))
                        .executes((sender, args) -> {
                            Area area = (Area) args[0];
                            AreaFlag flag = (AreaFlag) args[1];
                            area.getFlags().add(flag);
                            sender.sendMessage(GameLib.getChatPrefix() + ChatColor.GREEN + "Added " + flag.name());
                        })
                    )
                    .withSubcommand(new CommandAPICommand("remove")
                        .withArguments(areaArgument("area"))
                        .withArguments(areaFlagArgument("areaFlag"))
                        .executes((sender, args) -> {
                            Area area = (Area) args[0];
                            AreaFlag flag = (AreaFlag) args[1];
                            area.getFlags().remove(flag);
                            sender.sendMessage(GameLib.getChatPrefix() + ChatColor.GREEN + "Removed " + flag.name());
                        })
                    )
                )
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
                        GameLib.getChatPrefix() + ChatColor.GRAY + "Info for section: " + ChatColor.RESET + section.getId(),
                        GameLib.getChatPrefix() + ChatColor.GRAY + "Corners: " + ChatColor.RESET + "[" + section.getCorner1().getX() + ", " + section.getCorner1().getY() + ", " + section.getCorner1().getZ() + "] [" + section.getCorner2().getX() + ", " + section.getCorner2().getY() + ", " + section.getCorner2().getZ() + "]"
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
        .withSubcommand(new CommandAPICommand("joingame")
            .withArguments(arenaArgument("arena"))
            .executes((sender, args) -> {
                Arena arena = (Arena) args[0];
                Game game = GameLib.getGames().stream().filter((Game g) -> {return g.getArenas().contains(arena);}).findFirst().orElse(null);

                GameManager.joinGame(game, arena);
                sender.sendMessage(GameLib.getChatPrefix() + "Joining " + game.getName());
            })
        )
        .withSubcommand(new CommandAPICommand("leavegame")
            .executes((sender, args) -> {
                sender.sendMessage(GameLib.getChatPrefix() + "Leaving " + GameLib.getCurrentGame().getName());
                GameManager.leaveGame();
            })
        )
        .withSubcommand(new CommandAPICommand("teams")
            .withSubcommand(new CommandAPICommand("list")
                .executes((sender, args) -> {
                    sender.sendMessage(GameLib.getChatPrefix() + "Teams: " + GameLib.getTeams().stream().map(Team::getId).collect(Collectors.joining(", ")));
                })
            )
            .withSubcommand(new CommandAPICommand("get")
                .withArguments(teamArgument("team"))
                .executes((sender, args) -> {
                    Team team = (Team) args[0];
                    sender.sendMessage(new String[] {
                        GameLib.getChatPrefix() + ChatColor.GRAY + "Info for team: " + ChatColor.RESET + team.getName(),
                        GameLib.getChatPrefix() + ChatColor.GRAY + "ChatColor: " + ChatColor.RESET + team.getChatColor().getName(),
                        GameLib.getChatPrefix() + ChatColor.GRAY + "Players: " + ChatColor.RESET + team.getPlayers().stream().map(GamePlayer::getPlayer).map(OfflinePlayer::getName).collect(Collectors.joining(", "))
                    });
                })
            )
            .withSubcommand(new CommandAPICommand("join")
                .withArguments(gamePlayerArgument("player"))
                .withArguments(teamArgument("team"))
                .executes((sender, args) -> {
                    GamePlayer gamePlayer = (GamePlayer) args[0];
                    Team team = (Team) args[1];

                    gamePlayer.setTeam(team);
                    team.getPlayers().add(gamePlayer);
                    gamePlayer.setType(GamePlayerType.PLAYER);

                    sender.sendMessage(GameLib.getChatPrefix() + "Added " + ChatColor.AQUA + gamePlayer.getPlayer().getName() + ChatColor.RESET + " to team " + team.getName());
                })
            )
            .withSubcommand(new CommandAPICommand("leave")
                .withArguments(gamePlayerArgument("player"))
                .executes((sender, args) -> {
                    GamePlayer gamePlayer = (GamePlayer) args[0];

                    sender.sendMessage(GameLib.getChatPrefix() + "Removed " + ChatColor.AQUA + gamePlayer.getPlayer().getName() + ChatColor.RESET + " from team " + gamePlayer.getTeam().getName());

                    gamePlayer.getTeam().getPlayers().remove(gamePlayer);
                    gamePlayer.setTeam(null);
                    gamePlayer.setType(GamePlayerType.SPECTATOR);

                })
            )
        )
        .withSubcommand(new CommandAPICommand("config")
            .withSubcommand(new CommandAPICommand("save")
                .executes((sender, args) -> {
                    ConfigUtil.saveTeams();
                    sender.sendMessage(GameLib.getChatPrefix() + "Saved Teams to config");
                    for(Game game: GameLib.getGames()) {
                        ConfigUtil.saveGame(game);
                        sender.sendMessage(GameLib.getChatPrefix() + "Saved " + game.getName() + ChatColor.RESET + " to config");
                    }
                })
            )
            .withSubcommand(new CommandAPICommand("load")
                .executes((sender, args) -> {
                    ConfigUtil.loadTeams();
                    sender.sendMessage(GameLib.getChatPrefix() + "Loaded Teams from config");
                    for(Game game: GameLib.getGames()) {
                        game = ConfigUtil.loadGame(game.getPlugin().getConfig(), game.getId(), game.getPlugin());
                        sender.sendMessage(GameLib.getChatPrefix() + "Loaded " + game.getName() + ChatColor.RESET + " from config");
                    }
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
            String[] splitInput = input.split("\\.");
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

    public Argument gameFlagArgument(String nodeName) {
    
        return new CustomArgument<GameFlag>(nodeName, (input) -> {
            GameFlag flag = Set.of(GameFlag.values()).stream().filter((GameFlag f) -> f.name().equals(input)).findFirst().orElse(null);
        
            if(flag == null) {
                throw new CustomArgumentException(new MessageBuilder("Unknown GameFlag: ").appendArgInput());
            } else {
                return flag;
            }
        }).overrideSuggestions(sender -> {
            return Set.of(GameFlag.values()).stream().map(GameFlag::name).toArray(String[]::new);
        });
    }

    public Argument areaFlagArgument(String nodeName) {
    
        return new CustomArgument<AreaFlag>(nodeName, (input) -> {
            AreaFlag flag = Set.of(AreaFlag.values()).stream().filter((AreaFlag f) -> f.name().equals(input)).findFirst().orElse(null);
        
            if(flag == null) {
                throw new CustomArgumentException(new MessageBuilder("Unknown AreaFlag: ").appendArgInput());
            } else {
                return flag;
            }
        }).overrideSuggestions(sender -> {
            return Set.of(AreaFlag.values()).stream().map(AreaFlag::name).toArray(String[]::new);
        });
    }

     public Argument teamArgument(String nodeName) {
    
        return new CustomArgument<Team>(nodeName, (input) -> {
            Team team = GameLib.getTeams().stream().filter((Team t) -> t.getId().equals(input)).findFirst().orElse(null);
        
            if(team == null) {
                throw new CustomArgumentException(new MessageBuilder("Unknown Team: ").appendArgInput());
            } else {
                return team;
            }
        }).overrideSuggestions(sender -> {
            return GameLib.getTeams().stream().map(Team::getId).toArray(String[]::new);
        });
    }

}
