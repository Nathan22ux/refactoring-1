package theater;

import java.util.Map;

/**
 * Generates HTML-formatted statements for theater invoices.
 */
public class HTMLStatementPrinter extends StatementPrinter {

    public HTMLStatementPrinter(Invoice invoice, Map<String, Play> plays) {
        super(invoice, plays);
    }

    @Override
    public String statement() {
        final StatementData statementData = new StatementData(getInvoice(), getPlays());  // Add 'final'
        final StringBuilder result = new StringBuilder(String.format("<h1>Statement for %s</h1>%n",
                statementData.getCustomer()));
        result.append("<table>").append(System.lineSeparator());
        result.append(String.format(" <caption>Statement for %s</caption>%n", statementData.getCustomer()));
        result.append(" <tr><th>play</th><th>seats</th><th>cost</th></tr>").append(System.lineSeparator());
        for (Performance perfData : statementData.getPerformances()) {
            result.append(String.format(" <tr><td>%s</td><td>%s</td><td>%s</td></tr>%n",
                    statementData.getPlayName(perfData),
                    perfData.getAudience(),
                    usd(statementData.getAmount(perfData))));
        }
        result.append("</table>").append(System.lineSeparator());
        result.append(String.format("<p>Amount owed is <em>%s</em></p>%n", usd(statementData.getTotalAmount())));
        result.append(String.format("<p>You earned <em>%s</em> credits</p>%n", statementData.getVolumeCredits()));
        return result.toString();
    }
}
