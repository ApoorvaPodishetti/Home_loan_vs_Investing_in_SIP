import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the principal amount of the home loan:");
        double Loanamount = scanner.nextDouble();
        System.out.println("Enter the Interest rate of the home loan:");
        double Intrestrate = scanner.nextDouble();
        System.out.println("Enter the Tenure of the home loan in years:");
        double Tenureinyears = scanner.nextByte();
        System.out.println("Enter the annual appreciation rate of the home price in percentage:");
        double Houseapperciation = scanner.nextDouble();
        System.out.println("Enter the annual depreciation rate of the home in percentage: ");
        double Housedeprecsiation = scanner.nextDouble();
        System.out.println("Enter the monthly SIP investment amount : ");
        double Sipinvestmentamount= scanner.nextDouble();
        System.out.println("Enter the annual return on SIP investments (in percentage): ");
        double Sipannualreturns = scanner.nextDouble();

        double emi = calculateemi(Loanamount, Intrestrate, Tenureinyears);
        System.out.println("Your monthly EMI is " + emi);
        Map<Integer, Double> houseValueMap = calculateHouseValueOverTime(Tenureinyears, Loanamount, Houseapperciation, Housedeprecsiation);

        Map<Integer, Double> sipReturnsMap = calculateSIPReturns(Tenureinyears, Sipinvestmentamount, Sipannualreturns);

        Summary(Tenureinyears, houseValueMap, sipReturnsMap);
    }

    private static void Summary(double tenureinyears, Map<Integer, Double> houseValueMap, Map<Integer, Double> sipReturnsMap) {
        System.out.println("\nSummary:");
        for (int year = 1; year <= tenureinyears; year++) {
            Double houseValue = houseValueMap.get(year);
            Double sipValue = sipReturnsMap.get(year);
            if (houseValue == null || sipValue == null) {
                System.out.printf("Year %4d | No data available\n", year);
            } else {
                System.out.printf("Year %4d | House Value ₹%12.2f | SIP Investment Value ₹%12.2f\n", year, houseValue, sipValue);
            }
        }

        Double finalHouseValue = houseValueMap.get((int) tenureinyears);
        Double finalSIPValue = sipReturnsMap.get((int) tenureinyears);

        if (finalHouseValue != null && finalSIPValue != null) {
            System.out.printf("Final House Value (after %d years): ₹%.2f\n", (int) tenureinyears, finalHouseValue);
            System.out.printf("Final SIP Investment Value (after %d years): ₹%.2f\n", (int) tenureinyears, finalSIPValue);

            if (finalHouseValue > finalSIPValue) {
                System.out.println("The house investment outperforms the SIP plan.");
            } else if (finalHouseValue < finalSIPValue) {
                System.out.println("The SIP plan outperforms the house investment.");
            } else {
                System.out.println("Both investments are equal in terms of returns.");
            }
        } else {
            System.out.println("Insufficient data to compare final values.");
        }
    }

    private static Map<Integer, Double> calculateHouseValueOverTime(double tenureinyears, double loanamount, double houseapperciation, double housedeprecsiation) {
        Map<Integer, Double> housevalue = new HashMap<>();
        double currentHouseValue = loanamount;

        for (int year = 1; year <= tenureinyears; year++) {
            double depreciationFactor = (1 - housedeprecsiation / 100);
            double appreciationFactor = (1 + houseapperciation / 100);
            currentHouseValue *= appreciationFactor * depreciationFactor;
            housevalue.put(year, currentHouseValue);
        }

        return housevalue;
    }

    private static Map<Integer, Double> calculateSIPReturns(double tenureinyears, double sipinvestmentamount, double sipannualreturns) {
        Map<Integer, Double> sipreturns = new HashMap<>();

        double totalInvestment = 0;
        double totalSIPValue = 0;
        for (int year = 1; year <= tenureinyears; year++) {
            totalInvestment += sipinvestmentamount * 12;
            totalSIPValue += sipinvestmentamount * 12 * Math.pow(1 + sipannualreturns / 100, year);
            sipreturns.put(year, totalSIPValue);
        }
        return sipreturns;
    }


    private static double calculateemi(double loanamount, double intrestrate, double tenureinyears) {
        double monthlyrate = intrestrate / 100 / 12;
        int months = (int) (tenureinyears * 12);
        return (loanamount * monthlyrate * Math.pow(1 + monthlyrate, months)) / (Math.pow(1 + monthlyrate, months) - 1);
    }
}
