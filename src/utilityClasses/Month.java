package utilityClasses;
import lombok.*;

/**
 * Legacy code, not used in program. Creates enums for months
 */

@Getter
public enum Month {

    JANUAR(0), FEBRUAR(1), MAERZ(2), APRIL(3), MAI(4), JUNI(5), JULI(6), AUGUST(7), SEPTEMBER(8), OKTOBER(9), NOVEMBER(10), DEZEMBER(11);

    private final int monthNumber;

    Month(int monthNumber){
        this.monthNumber = monthNumber;
    }
}
