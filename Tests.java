import static org.junit.Assert.*;
import org.junit.*;

import java.util.Arrays;
import java.util.List;

public class Tests {
  @Test public void testPass() {
    assertTrue("true should be true", true);
  }

  @Test
  public void testVerify1() {
    MBTA mbta = new MBTA();
    Log log = new Log();
    mbta.addLine("greenLine", Arrays.asList("a", "b", "c", "f", "e"));
    Train greenLine = Train.make("greenLine");
    Station a = Station.make("a");
    Station b = Station.make("b");
    Station c = Station.make("c");
    Station d = Station.make("d");
    Station e = Station.make("e");
    Station f = Station.make("f");
    mbta.addJourney("Yongqiang", Arrays.asList("b", "f"));
    Passenger Yongqiang = Passenger.make("Yongqiang");
    log.train_moves(greenLine, a, b);
    log.passenger_boards(Yongqiang, greenLine, b);
    log.train_moves(greenLine, b, c);
    log.train_moves(greenLine, c, f);
    log.passenger_deboards(Yongqiang, greenLine, f);
    log.train_moves(greenLine, f, e);
    Verify.verify(mbta, log);
  }

  @Test
  public void testVerify2() {
    //Passenger Alice boards red at Davis
    //Passenger Bob boards green at Park
    //Train green moves from Park to Government Center
    //Train red moves from Davis to Harvard
    //Train red moves from Harvard to Kendall
    //Train green moves from Government Center to North Station
    //Train green moves from North Station to Lechmere
    //Train green moves from Lechmere to East Sommerville
    //Passenger Alice deboards red at Kendall
    //Train green moves from East Sommerville to Tufts
    //Passenger Bob deboards green at Tufts
    MBTA mbta = new MBTA();
    mbta.loadConfig("sample.json");
    Passenger Alice = Passenger.make("Alice");
    Passenger Bob = Passenger.make("Bob");
    Train green = Train.make("green");
    Train red = Train.make("red");
  }

  public static void main(String[] args) {
    Passenger p = Passenger.make("Alice");
    Train red = Train.make("red");
    Station Davis = Station.make("Davis");
    Station Harvard = Station.make("Harvard");
    MBTA mbta = new MBTA();
    mbta.loadConfig("sample.json");
    Station now = mbta.passLoc.get(p);
    Train pstate = mbta.pstates.get(p);
    Train sstate = mbta.sstates.get(now);
    //if p in a station, if the train in that station is a right train
    boolean rightTrain;
    int index = mbta.trip.get(p).lastIndexOf(now);
    Station destination = mbta.trip.get(p).get(index + 1);
    if (sstate == null) {
      rightTrain = false;
    } else {
      List stations = mbta.line.get(sstate);
      if (stations.contains(destination)) {
        rightTrain = true;
      } else {
        rightTrain = false;
      }
    }
    //if p in a train, if the train in the destination of p
    boolean inDes = false;
    if (pstate != null) {
      if (mbta.trainLoc.get(pstate) == destination) {
        inDes = true;
      }
    }
    System.out.println(now);
    System.out.println(pstate);
    System.out.println(sstate);
    System.out.println(rightTrain);
    System.out.println(destination);
    System.out.println(inDes);
    System.out.println((pstate == null && (sstate == null || !rightTrain)) || (pstate != null && !inDes));
    BoardEvent b1 = new BoardEvent(p, red, Davis);
    b1.replayAndCheck(mbta);
    MoveEvent m1 = new MoveEvent(red, Davis, Harvard);
    m1.replayAndCheck(mbta);
    DeboardEvent b2 = new DeboardEvent(p, red, Harvard);
    b2.replayAndCheck(mbta);
    //
    now = mbta.passLoc.get(p);
    pstate = mbta.pstates.get(p);
    sstate = mbta.sstates.get(now);
    //if p in a station, if the train in that station is a right train

    index = mbta.trip.get(p).lastIndexOf(now);
    destination = mbta.trip.get(p).get(index + 1);
    if (sstate == null) {
      rightTrain = false;
    } else {
      List stations = mbta.line.get(sstate);
      if (stations.contains(destination)) {
        rightTrain = true;
      } else {
        rightTrain = false;
      }
    }
    //if p in a train, if the train in the destination of p

    if (pstate != null) {
      if (mbta.trainLoc.get(pstate) == destination) {
        inDes = true;
      }
    }
    System.out.println(now);
    System.out.println(pstate);
    System.out.println(sstate);
    System.out.println(rightTrain);
    System.out.println(destination);
    System.out.println(inDes);
    System.out.println((pstate == null && (sstate == null || !rightTrain)) || (pstate != null && !inDes));
    System.out.println(mbta.trainLoc.get(pstate));
  }
}
