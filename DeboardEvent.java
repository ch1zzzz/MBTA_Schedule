import java.util.*;

public class DeboardEvent implements Event {

  public final Passenger p; public final Train t; public final Station s;
  public DeboardEvent(Passenger p, Train t, Station s) {
    this.p = p; this.t = t; this.s = s;
  }
  public boolean equals(Object o) {
    if (o instanceof DeboardEvent e) {
      return p.equals(e.p) && t.equals(e.t) && s.equals(e.s);
    }
    return false;
  }
  public int hashCode() {
    return Objects.hash(p, t, s);
  }
  public String toString() {
    return "Passenger " + p + " deboards " + t + " at " + s;
  }
  public List<String> toStringList() {
    return List.of(p.toString(), t.toString(), s.toString());
  }
  public void replayAndCheck(MBTA mbta) {
    try {
      replay(mbta);
      // change the mbta to the state after event (pstates - off the train, passloc - s)
      mbta.passLoc.put(p, s);
      mbta.pstates.put(p, null);
    } catch (Exception e) {
      throw e;
    }
  }
  public void replay(MBTA mbta) {
    // 1. check the station s is in trip list of that passenger
    if (!mbta.trip.get(p).contains(s)) {
      throw new IllegalStateException("deboard event error1");
    }
    // 2. check that train is at station s
    if (mbta.trainLoc.get(t) != s) {
      throw new IllegalStateException("deboard event error2");
    }
    // 3. check the p is on the t
    if (mbta.pstates.get(p) != t) {
      throw new IllegalStateException("deboard event error3");
    }
    //4. check the s is not same to p's location(it means where p board last time)
    if (s == mbta.passLoc.get(p)) {
      throw new IllegalStateException("deboard event error4");
    }
  }
}
