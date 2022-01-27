import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.jar.JarEntry;

public class MBTA {
  //config for MBTA(line: train-station; trip: passenger-station)
  //lines and trips are string representation for initialization with gson
  //line and trip are entity representation for implement
  public Map<String, List<String>> lines = new HashMap<>();
  public Map<String, List<String>> trips = new HashMap<>();
  public Map<Train, List<Station>> line = new HashMap<>();
  public Map<Passenger, List<Station>> trip = new HashMap<>();
  //location for train and passengers
  public Map<Train, Station> trainLoc = new HashMap<>();
  public Map<Passenger, Station> passLoc = new HashMap<>();
  //train direction, 1 - forward, -1 - reverse
  public Map<Train, Integer> trainDir = new HashMap<>();
  //station states, t - full, null - empty
  public Map<Station, Train> sstates = new HashMap<>();
  //passenger states, null-in the station, train - on the train
  public Map<Passenger, Train> pstates = new HashMap<>();
  // Creates an initially empty simulation
  public MBTA() { }

  // Adds a new transit line with given name and stations
  public void addLine(String name, List<String> stations) {
    Train train = Train.make(name);
    List<Station> stationList = new LinkedList<>();
    for (String station : stations) {
      stationList.add(Station.make(station));
      sstates.put(Station.make(station), null);
    }
    lines.put(name, stations);
    line.put(train, stationList);
    trainLoc.put(train, stationList.get(0));
    trainDir.put(train, 1);
    sstates.put(stationList.get(0), train);
  }

  // Adds a new planned journey to the simulation
  public void addJourney(String name, List<String> stations) {
    Passenger p = Passenger.make(name);
    List<Station> stationList = new LinkedList<>();
    for (String station : stations) {
      stationList.add(Station.make(station));
    }
    trips.put(name, stations);
    trip.put(p, stationList);
    passLoc.put(p, stationList.get(0));
    pstates.put(p, null);
  }

  // Return normally if initial simulation conditions are satisfied, otherwise
  // raises an exception
  public void checkStart() {
    try {
      //check the trains are at first station
      for (Train train : line.keySet()) {
        Station firstStation = line.get(train).get(0);
        if (trainLoc.get(train) != firstStation) {
          throw new IllegalStateException("Start error");
        }
      }
      //check the passengers are at first station of their trip
      for (Passenger passenger : trip.keySet()) {
        Station firstStation = trip.get(passenger).get(0);
        if (passLoc.get(passenger) != firstStation) {
          throw new IllegalStateException("Start error");
        }
      }
      //check the trains are all forward
      for (Train train : trainDir.keySet()) {
        if (trainDir.get(train) != 1) {
          throw new IllegalStateException("Start error");
        }
      }
    } catch (Exception e) {
      throw e;
    }
  }

  // Return normally if final simulation conditions are satisfied, otherwise
  // raises an exception
  public void checkEnd() {
    for (Passenger p : trip.keySet()) {
      Station destination = trip.get(p).get(trip.get(p).size() - 1);
      if (destination != passLoc.get(p)) {
        throw new IllegalStateException("End error");
      }
    }
  }

  // reset to an empty simulation
  public void reset() {
    lines = new HashMap<>();
    trips = new HashMap<>();
    line = new HashMap<>();
    trip = new HashMap<>();
    trainLoc = new HashMap<>();
    passLoc = new HashMap<>();
    trainDir = new HashMap<>();
    sstates = new HashMap<>();
  }

  //return the next station based on the train and a start station
  public Station nextStation(Station start, Train train) {
    int index = line.get(train).lastIndexOf(start);
    int nextIndex = index + trainDir.get(train);
    if (nextIndex < 0 || nextIndex >= line.get(train).size()) {
      nextIndex = index - trainDir.get(train);
    }
    return line.get(train).get(nextIndex);
  }

  // adds simulation configuration from a file
  public void loadConfig(String filename) {
    String config = readFile(filename);
    Gson gson = new Gson();
    MBTA configMbta = gson.fromJson(config, MBTA.class);

    for (Map.Entry<String, List<String>> entry : configMbta.lines.entrySet()) {
      this.addLine(entry.getKey(), entry.getValue());
    }
    for (Map.Entry<String, List<String>> entry : configMbta.trips.entrySet()) {
      this.addJourney(entry.getKey(), entry.getValue());
    }
  }

  // read the config file to string
  private String readFile(String filename) {
    try {
      BufferedReader in = new BufferedReader
              (new FileReader(filename));
      String str;
      StringBuilder result = new StringBuilder();
      while ((str = in.readLine()) != null) {
        result.append(str);
      }
      return result.toString();
    } catch (IOException e) {
      return null;
    }
  }

  public static void main(String[] args) {
    MBTA mbta = new MBTA();
    mbta.loadConfig("sample.json");
    System.out.println(mbta.lines);
    System.out.println(mbta.trips);
    System.out.println(mbta.line);
    System.out.println(mbta.trip);
    System.out.println(mbta.trainLoc);
    System.out.println(mbta.passLoc);
    System.out.println(mbta.trainDir);
    Gson gson = new Gson();
    String s = gson.toJson(mbta);
    System.out.println(s);
    Train train = Train.make("red");
    Station station = Station.make("Harvard");
    System.out.println(mbta.line.get(train).lastIndexOf(station));
    System.out.println(mbta.sstates);
  }
}
