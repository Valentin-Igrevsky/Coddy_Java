import java.util.ArrayList;

class Level {
    ArrayList<Platform> platforms = new ArrayList<>();
    ArrayList<DamageZone> hazards = new ArrayList<>();
    ArrayList<PhysicsDecoration> physicsDecorations = new ArrayList<>();
    ArrayList<Decoration> decorations = new ArrayList<>();
    ArrayList<Trigger> triggers = new ArrayList<>();
}