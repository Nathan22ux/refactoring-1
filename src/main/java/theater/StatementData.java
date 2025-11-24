package theater;

import theater.Constants;
import theater.Invoice;
import theater.Performance;
import theater.Play;

import java.util.List;
import java.util.Map;

// Task 1.1
// Task 1.2
// Task 2.1
// Task 2.2
// Task 2.3

/**
 * Contains data needed to generate a statement for an invoice.
 */
public class StatementData {
    private final Map<String, Play> plays;
    private final Invoice invoice;

    public StatementData(Invoice invoice, Map<String, Play> plays) {
        this.plays = plays;
        this.invoice = invoice;
    }

    public String getCustomer() {
        return invoice.getCustomer();
    }

    public List<Performance> getPerformances() {
        return invoice.getPerformances();
    }

    /**
     * Calculates the total amount owed for all performances.
     * @return total amount in cents
     */
    public int getTotalAmount() {
        int result = 0;
        for (Performance performance : invoice.getPerformances()) {
            result += getAmount(performance);
        }
        return result;
    }

    private Play getPlay(Performance performance) {
        return plays.get(performance.getPlayID());
    }

    /**
     * Calculates the amount for a single performance.
     * @param performance the performance
     * @return amount in cents
     */
    public int getAmount(Performance performance) {
        int result;
        final Play play = getPlay(performance);
        switch (play.getType()) {
            case "tragedy":
                result = Constants.TRAGEDY_BASE_AMOUNT;  // Define this constant as 40000
                if (performance.getAudience() > Constants.TRAGEDY_AUDIENCE_THRESHOLD) {
                    result += Constants.TRAGEDY_OVER_THRESHOLD_AMOUNT  // Define as 1000
                            * (performance.getAudience() - Constants.TRAGEDY_AUDIENCE_THRESHOLD);
                }
                break;
            case "comedy":
                result = Constants.COMEDY_BASE_AMOUNT;
                if (performance.getAudience() > Constants.COMEDY_AUDIENCE_THRESHOLD) {
                    result += Constants.COMEDY_OVER_BASE_CAPACITY_AMOUNT
                            + (Constants.COMEDY_OVER_BASE_CAPACITY_PER_PERSON
                            * (performance.getAudience() - Constants.COMEDY_AUDIENCE_THRESHOLD));
                }
                result += Constants.COMEDY_AMOUNT_PER_AUDIENCE * performance.getAudience();
                break;
            default:
                throw new RuntimeException(String.format("unknown type: %s", play.getType()));
        }
        return result;
    }

    /**
     * Calculates volume credits for a single performance.
     * @param performance the performance
     * @return volume credits earned
     */
    private int volumeCreditsFor(Performance performance) {
        int credits = Math.max(performance.getAudience() - Constants.BASE_VOLUME_CREDIT_THRESHOLD, 0);
        if ("comedy".equals(getPlay(performance).getType())) {
            credits += performance.getAudience() / Constants.COMEDY_EXTRA_VOLUME_FACTOR;
        }
        return credits;
    }

    /**
     * Calculates total volume credits for all performances.
     * @return total volume credits
     */
    public int getVolumeCredits() {
        int result = 0;
        for (Performance performance : invoice.getPerformances()) {
            result += volumeCreditsFor(performance);
        }
        return result;
    }

    /**
     * Gets the play name for a performance.
     * @param performance the performance
     * @return play name
     */
    public String getPlayName(Performance performance) {
        return getPlay(performance).getName();
    }
}
