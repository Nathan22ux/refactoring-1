package theater;

import theater.Constants;
import theater.Invoice;
import theater.Performance;
import theater.Play;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

/**
 * This class generates a statement for a given invoice of performances.
 */
public class StatementPrinter {
    private final Invoice invoice;
    private final Map<String, Play> plays;

    public StatementPrinter(Invoice invoice, Map<String, Play> plays) {
        this.invoice = invoice;
        this.plays = plays;
    }

    /**
     * Returns a formatted statement of the invoice associated with this printer.
     * @return the formatted statement
     * @throws RuntimeException if one of the play types is not known
     */
    public String statement() {
        int totalAmount = 0;
        int volumeCredits = 0;
        StringBuilder result =
                new StringBuilder("Statement for " + invoice.getCustomer() + System.lineSeparator());

        for (Performance performance : invoice.getPerformances()) {

            volumeCredits += volumeCreditsFor(performance, getPlay(performance));

            result.append(String.format(
                    "  %s: %s (%s seats)%n",
                    getPlay(performance).name,
                    NumberFormat.getCurrencyInstance(Locale.US).format(getAmount(performance) / 100),
                    performance.audience));

            totalAmount += getAmount(performance);
        }

        result.append(String.format("Amount owed is %s%n", getFormat(totalAmount)));
        result.append(String.format("You earned %s credits%n", volumeCredits));
        return result.toString();
    }

    private int getTotalAmount() {
        int totalAmount = 0;
        for (Performance performance : invoice.getPerformances()) {
            totalAmount += getAmount(performance);
        }
        return totalAmount;
    }

    private String getFormat(int amount) {
        return NumberFormat.getCurrencyInstance(Locale.US).format(amount / 100);
    }

    private Play getPlay(Performance performance) {
        return plays.get(performance.playID);
    }

    private int getAmount(Performance performance) {
        int result;
        Play play = getPlay(performance);
        switch (play.type) {
            case "tragedy":
                result = 40000;
                if (performance.audience > Constants.TRAGEDY_AUDIENCE_THRESHOLD) {
                    result += 1000 * (performance.audience - 30);
                }
                break;
            case "comedy":
                result = Constants.COMEDY_BASE_AMOUNT;
                if (performance.audience > Constants.COMEDY_AUDIENCE_THRESHOLD) {
                    result += Constants.COMEDY_OVER_BASE_CAPACITY_AMOUNT
                            + (Constants.COMEDY_OVER_BASE_CAPACITY_PER_PERSON
                            * (performance.audience - Constants.COMEDY_AUDIENCE_THRESHOLD));
                }
                result += Constants.COMEDY_AMOUNT_PER_AUDIENCE * performance.audience;
                break;
            default:
                throw new RuntimeException(String.format("unknown type: %s", play.type));
        }
        return result;
    }

    private int volumeCreditsFor(Performance performance, Play play) {
        int credits = Math.max(performance.audience - Constants.BASE_VOLUME_CREDIT_THRESHOLD, 0);
        // add extra credit for every five comedy attendees
        if ("comedy".equals(play.type)) {
            credits += performance.audience / Constants.COMEDY_EXTRA_VOLUME_FACTOR;
        }
        return credits;
    }
}
