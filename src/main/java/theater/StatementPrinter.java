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
        StatementData statementData = new StatementData(invoice, plays);
        return renderPlainText(statementData);
    }

    private String renderPlainText(StatementData statementData) {
        StringBuilder result =
                new StringBuilder("Statement for " + statementData.getCustomer() + System.lineSeparator());

        for (Performance performance : statementData.getPerformances()) {
            result.append(String.format(
                    "  %s: %s (%s seats)%n",
                    statementData.getPlayName(performance),
                    usd(statementData.getAmount(performance)),
                    performance.audience));
        }

        int totalAmount = statementData.getTotalAmount();

        int volumeCredits = statementData.getVolumeCredits();

        result.append(String.format("Amount owed is %s%n", usd(totalAmount)));
        result.append(String.format("You earned %s credits%n", volumeCredits));
        return result.toString();
    }


    private String usd(int amount) {
        return NumberFormat.getCurrencyInstance(Locale.US).format(amount / 100);
    }




}
