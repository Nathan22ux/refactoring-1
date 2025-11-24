package theater;

import java.util.List;
import java.util.Map;


//Task 1.1
//Task 1.2
//Task 2.1
//Task 2.2
//Task 2.3
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

    public int getTotalAmount() {
        int result = 0;
        for (Performance performance : invoice.getPerformances()) {
            result += getAmount(performance);
        }
        return result;
    }

    private Play getPlay(Performance performance) {
        return plays.get(performance.playID);
    }

    public int getAmount(Performance performance) {
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

    private int volumeCreditsFor(Performance performance) {
        int credits = Math.max(performance.audience - Constants.BASE_VOLUME_CREDIT_THRESHOLD, 0);
        if ("comedy".equals(getPlay(performance).type)) {
            credits += performance.audience / Constants.COMEDY_EXTRA_VOLUME_FACTOR;
        }
        return credits;
    }

    public int getVolumeCredits() {
        int result = 0;
        for (Performance performance : invoice.getPerformances()) {
            result += volumeCreditsFor(performance);
        }
        return result;
    }


    public String getPlayName(Performance performance) {
        return getPlay(performance).getName();
    }


}
