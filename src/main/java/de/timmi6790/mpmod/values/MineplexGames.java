package de.timmi6790.mpmod.values;

import lombok.Getter;

import java.util.Optional;

public enum MineplexGames {
    ALIEN_INVASION("", "Player"),
    AREA_51_RAID("", "MilitarySniper", "MilitaryPolice", "MilitaryMedic", "NarutoRunner", "Kyle", "Chad"),
    BACON_BRAWL("", "ElMuchachoPigo", "MamaPiggles", "Pig", "ChrisPBacon"),
    BLOCK_HUNT("", "SwapperHider", "InstantHider", "SchockingHider", "InfestorHider", "LeaperHunter",
            "TNTHunter", "RadarHunter", "TaserHunter"),
    BOMB_LOBBERS("", "Acrobat", "Armorer", "Pitcher", "Waller"),
    CAKE_WARS_DUOS("CW", "Builder", "Warrior", "Archer", "Frosting"),
    CAKE_WARS_STANDARD("CW4", CAKE_WARS_DUOS.kits),
    CASTLE_ASSAULT("", "Alchemist", "Archer", "Demolitionist", "Fighter", "Tank"),
    CASTE_SIEGE("", "CastleWolf", "CastleMarksman", "CastleKnight", "CastlePaladin", "UndeadGhoul",
            "UndeadArcher", "UndeadZombie", "UndeadSummoner"),
    CHAMPIONS_CTF("", "Brute", "Ranger", "Knight", "Mage", "Assassin"),
    CHAMPIONS_DOMINATION("", CHAMPIONS_CTF.kits),
    CHAMPIONS_TDM("", CHAMPIONS_CTF.kits),
    CLANS("", CHAMPIONS_CTF.kits),
    CHRISTMAS_CHAOS("", "Santa'sHelper"),
    CHRISTMAS_CHAOS_II("", CHRISTMAS_CHAOS.kits),
    DEATH_TAG("", "RunnerBasher", "RunnerArcher", "RunnerTraitor", "AlphaChaser"),
    DRAGON_ESCAPE("", "Jumper", "Disruptor", "Warper", "Digger"),
    DRAGONS("", "Coward", "Marksman", "Pyrotechnic"),
    DRAW_MY_THING("", "Artist"),
    EVOLUTION("", "Darwinist", "QuickEvolver", "HealthHarvester"),
    GLADIATOR("", "Gladiator"),
    HALLOWEEN_HAVOC("", "Berserker", "Archer", "Mage", "Rogue"),
    HALLOWEEN_HORROR("", "FinnTheHuman", "RobinHood", "Thor"),
    HEROES_OF_GWEN("", "Hattori", "Devon", "Anath", "Dana", "Biff", "Larissa", "Bardolf", "Rowena", "Ivy"),
    MASTER_BUILDERS("", "Builder"),
    MICRO_BATTLE("", "Archer", "Worker", "Fighter"),
    MILK_THE_COW("", "RabbitFarmer", "TheAngryCow"),
    MINE_STRIKE("", "Player"),
    MINEPLEX_EVENT(""),
    MONSTER_MAZE("", "Jumper", "BodyBuilder", "Slowballer", "Repulsor"),
    NANO(""),
    ONE_IN_THE_QUIVER("", "Jumper", "Brawler", "SlamShooter", "Enchanter", "Ninja"),
    PUMPKINS_REVENGE("", HALLOWEEN_HORROR.kits),
    ROSE_RUSH("", "Archer", "Gardener", "Leaper"),
    RUNNER("", "Jumper", "Archer", "Frosty"),
    SHEEP_QUEST("", "Berserker", "Archer", "Brute"),
    SKYFALL("", "Speeder", "Booster", "Jouster", "Stunner", "Aeronaught", "Deadeye"),
    SKYFALL_TEAMS("", SKYFALL.kits),
    SKYWARS(""),
    SKYWARS_TEAMS("", SKYWARS.kits),
    SNAKE("", "SpeedySnake", "SuperSnake", "ReversalSnake"),
    SNEAKY_ASSASSINS("", "EscapeArtist", "RangedAssassin", "Revealer", "Briber"),
    SNOW_FIGHT("", "Sportsman", "Tactician", "Medic"),
    SPEED_BUILDERS(""),
    SQUID_SHOOTER("", "RetroSquid"),
    SUPER_PAINTBALL("", "Rifle", "Shotgun", "MachineGun", "Sniper"),
    SUPER_SMASH_MOBS("", "Skeleton", "IronGolem", "Spider", "Slime", "SkySquid", "Creeper", "Enderman",
            "Snowman", "Wolf", "MagmaCube", "Witch", "WitherSkeleton", "Zombie", "Cow", "SkeletalHorse", "Pig",
            "Blaze", "Chicken", "Guardian", "Sir.Sheep", "Villager"),
    SUPER_SMASH_MOBS_TEAMS("", SUPER_SMASH_MOBS.kits),
    SUPER_SPLEEF("", "", "Snowballer", "Archer", "Brawler"),
    SURVIVAL_GAMES("", "Axeman", "Knight", "Archer", "Brawler", "Assassin", "Beastmaster", "Bomber",
            "Necromancer", "Barbarian", "Horseman"),
    SURVIVAL_GAMES_TEAMS("", SURVIVAL_GAMES.kits),
    THE_BRIDGES("", "Apple", "Berserker", "Brawler", "Archer", "Miner", "Bomber", "Destructor"),
    TUG_OF_WOOL("", "FarmerJoe", "Butch", "PostmanPat"),
    TURF_WARS("", "Marksman", "Infiltrator", "Shredder"),
    WITHER_ASSAULT("", "HumanArcher", "HumanMedic", "HumanEditor", "Wither"),
    WIZARDS("", "Mage", "Sorcerer", "Mystic", "WitchDoctor"),
    LOBBY("Lobby"),
    STAFF("Staff");

    @Getter
    private final String name;

    private final String[] kits;

    MineplexGames(final String name, final String... kits) {
        this.name = name;
        this.kits = kits;
    }

    public static Optional<MineplexGames> getGameByName(final String name) {
        for (final MineplexGames games : MineplexGames.values()) {
            if (games.getName().equalsIgnoreCase(name)) {
                return Optional.of(games);
            }
        }
        return Optional.empty();
    }

    public String[] getKits() {
        return this.kits.clone();
    }
}
