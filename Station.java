import java.util.HashMap;
import java.util.Map;

public class Station extends Entity {
  private Station(String name) { super(name); }
  private static Map<String, Station> stationMap = new HashMap<>();
  public static Station make(String name) {
    // Change this method!
    if (stationMap.keySet().contains(name)) {
      return stationMap.get(name);
    }
    Station s = new Station(name);
    stationMap.put(name, s);
    return s;
  }
}
