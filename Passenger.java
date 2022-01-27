import java.util.HashMap;
import java.util.Map;

public class Passenger extends Entity {
  private Passenger(String name) { super(name); }
  private static Map<String, Passenger> passengerMap = new HashMap<>();
  public static Passenger make(String name) {
    // Change this method!
    if (passengerMap.keySet().contains(name)) {
      return passengerMap.get(name);
    }
    Passenger p = new Passenger(name);
    passengerMap.put(name, p);
    return p;
  }
}
