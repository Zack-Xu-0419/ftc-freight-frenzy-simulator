import StationaryElements.AllianceHub;
import StationaryElements.Carosel;
import StationaryElements.SharedHub;
import StationaryElements.StationaryElement;

public class Scoring {
    //count the scoring for given stationary element
    public static int score(StationaryElement stationaryElement) {
        int score = 0;
        // Checks the type of the stationary element, and scores accordingly
        if (stationaryElement instanceof Carosel) {
            score = (10 - ((Carosel) stationaryElement).getDucks()) * 6;
        }
        else if (stationaryElement instanceof SharedHub) {
            score += ((SharedHub) stationaryElement).red.size() * 4;
            score += ((SharedHub) stationaryElement).blue.size() * 4;
        }
        else if (stationaryElement instanceof AllianceHub) {
            score += ((AllianceHub) stationaryElement).levelOne.size() * 2;
            score += ((AllianceHub) stationaryElement).levelTwo.size() * 4;
            score += ((AllianceHub) stationaryElement).levelThree.size() * 6;
        }
        return score;
    }

}
