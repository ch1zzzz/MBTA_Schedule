import java.util.*;

public class BoardEvent implements Event {

  public final Passenger p; public final Train t; public final Station s;
  public BoardEvent(Passenger p, Train t, Station s) {
    this.p = p; this.t = t; this.s = s;
  }
  public boolean equals(Object o) {
    if (o instanceof BoardEvent e) {
      return p.equals(e.p) && t.equals(e.t) && s.equals(e.s);
    }
    return false;
  }
  public int hashCode() {
    return Objects.hash(p, t, s);
  }
  public String toString() {
    return "Passenger " + p + " boards " + t + " at " + s;
  }
  public List<String> toStringList() {
    return List.of(p.toString(), t.toString(), s.toString());
  }
  public void replayAndCheck(MBTA mbta) {
    try {
      replay(mbta);
      // change the mbta to the state after event (pstates - on the train)
      mbta.pstates.put(p, t);
    } catch (Exception e) {
      throw e;
    }
  }
  public void replay(MBTA mbta) {
    if (!mbta.trip.get(p).contains(s)) {
      throw new IllegalStateException("board event error1");
    }
    // 2. check that train and passenger is at station s
    if (mbta.trainLoc.get(t) != s || mbta.passLoc.get(p) != s) {
      throw new IllegalStateException("board event error2");
    }
    // 3. check that the train is the right choice, the train can go to the destination, p not in des
    // the trip of p only included every necessary stops to change lines, not every station
    int index = mbta.trip.get(p).lastIndexOf(s);
    if (index == mbta.trip.get(p).size() - 1) {
      throw new IllegalStateException("board event error, last station!");
    }
    Station destination = mbta.trip.get(p).get(index + 1);
    if (!mbta.line.get(t).contains(destination)) {
      throw new IllegalStateException("board event error3");
    }
    //4. check that p is not on a train
    if (mbta.pstates.get(p) != null) {
      throw new IllegalStateException("board event error4");
    }
  }
}
