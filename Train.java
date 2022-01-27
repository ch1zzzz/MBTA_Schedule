import java.util.HashMap;
import java.util.Map;

public class Train extends Entity {
  private Train(String name) { super(name); }
  private static Map<String, Train> trainMap = new HashMap<>();
  public static Train make(String name) {
    // Change this method!
    if (trainMap.keySet().contains(name)) {
      return trainMap.get(name);
    }
    Train t = new Train(name);
    trainMap.put(name, t);
    return t;
  }
}
