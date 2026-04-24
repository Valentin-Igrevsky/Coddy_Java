import java.util.function.Consumer;
import java.util.function.BiFunction;

class PlacementTool {
    BiFunction<Integer, Integer, GameObject> factory;
    Consumer<GameObject> placer;
    String pattern;
    String name;

    public PlacementTool(
            BiFunction<Integer, Integer, GameObject> factory,
            Consumer<GameObject> placer,
            String pattern,
            String name
    ) {
        this.factory = factory;
        this.placer = placer;
        this.pattern = pattern;
        this.name = name;
    }
}