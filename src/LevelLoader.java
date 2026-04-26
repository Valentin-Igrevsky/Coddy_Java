class LevelLoader {
    public static Level load(int id) {
        Level level = new Level();

        switch (id) {
            case 0 -> level.platforms.add(new Platform(0, 15, "platform/2.png"));
            case 1 -> {
                level.platforms.add(new Platform(0, 15, "platform/2.png"));
                level.platforms.add(new Platform(177, -53, "platform/1.png"));
                level.platforms.add(new Platform(47, -18, "platform/1.png"));
                level.physicsDecorations.add(new PhysicsDecoration(50, -41, "physical_decorations/Barrel Sprite.png"));
                level.physicsDecorations.add(new PhysicsDecoration(74, -5, "physical_decorations/Barrel Sprite.png"));
                level.decorations.add(new Decoration(77, -14, "decorations/rocks.png"));
                level.platforms.add(new Platform(-48, 18, "platform/1.png"));
                level.platforms.add(new Platform(-101, 6, "platform/1.png"));
                level.platforms.add(new Platform(-169, 5, "platform/1.png"));
                level.hazards.add(new DamageZone(-266, 28, "hazards/lava.png", 50));
                level.platforms.add(new Platform(229, -33, "platform/1.png"));
                level.hazards.add(new DamageZone(229, -43, "hazards/spike.png", 50));
                level.hazards.add(new DamageZone(239, -43, "hazards/spike.png", 50));
                level.platforms.add(new Platform(209, -84, "platform/1.png"));
                level.physicsDecorations.add(new PhysicsDecoration(216, -100, "physical_decorations/Barrel Sprite.png"));
                level.platforms.add(new Platform(302, -34, "platform/1.png"));
                level.platforms.add(new Platform(261, -124, "platform/3.png"));
                level.platforms.add(new Platform(261, -144, "platform/3.png"));
                level.platforms.add(new Platform(329, -37, "platform/3.png"));
                level.triggers.add(new LevelExit(334, -51, 10, 10));
                level.hazards.add(new DamageZone(0, 28, "hazards/lava.png", 50));
                level.hazards.add(new DamageZone(133, 28, "hazards/lava.png", 50));
                level.hazards.add(new DamageZone(266, 28, "hazards/lava.png", 50));
                level.hazards.add(new DamageZone(399, 28, "hazards/lava.png", 50));
                level.hazards.add(new DamageZone(532, 28, "hazards/lava.png", 50));
                level.hazards.add(new DamageZone(-133, 28, "hazards/lava.png", 50));
                level.hazards.add(new DamageZone(-266, 48, "hazards/lava.png", 50));
                level.hazards.add(new DamageZone(-133, 48, "hazards/lava.png", 50));
                level.hazards.add(new DamageZone(0, 48, "hazards/lava.png", 50));
                level.hazards.add(new DamageZone(133, 48, "hazards/lava.png", 50));
                level.hazards.add(new DamageZone(399, 48, "hazards/lava.png", 50));
                level.hazards.add(new DamageZone(532, 48, "hazards/lava.png", 50));
                level.hazards.add(new DamageZone(266, 48, "hazards/lava.png", 50));
                level.triggers.add(new KillZone(-266, 62, 931, 10));
            }

            case 2 -> {
                level.platforms.add(new Platform(0, 15, "platform/2.png"));
                level.physicsDecorations.add(new PhysicsDecoration(73, -1, "physical_decorations/Barrel Sprite.png"));
                level.physicsDecorations.add(new PhysicsDecoration(81, -19, "physical_decorations/Barrel Sprite.png"));
                level.physicsDecorations.add(new PhysicsDecoration(90, -1, "physical_decorations/Barrel Sprite.png"));
                level.triggers.add(new LevelExit(158, 3, 10, 10));
            }
            case 3 -> {
                level.platforms.add(new Platform(0, 15, "platform/2.png"));
            }
            default -> {
                level.platforms.add(new Platform(0, 15, "platform/2.png"));
                level.physicsDecorations.add(new PhysicsDecoration(73, -1, "physical_decorations/Barrel Sprite.png"));
            }
        }

        return level;
    }
}