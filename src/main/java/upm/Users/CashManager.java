package upm.Users;

import java.util.*;

public class CashManager {
    public static ArrayList<Cash> cashList = new ArrayList<>();

    public CashManager() {
        cashList = new ArrayList<>();
    }



    public static ArrayList<Cash> getCashList() {
        return cashList;
    }

    public static boolean addCash(Cash cash) {
        if (cash == null) return false;
        for (Cash c : cashList) {
            if (c.getIdentifier().equals(cash.getIdentifier())) {
                return false;
            }
        }
        return cashList.add(cash);
    }

    public static boolean removeCashByIdentifier(String identifier) {
        Iterator<Cash> it = cashList.iterator();
        boolean removed = false;
        while (it.hasNext() && !removed) {
            if (it.next().getIdentifier().equals(identifier)) {
                it.remove();
                removed = true;
            }
        }
        return removed;
    }

    public static Cash getCashByIdentifier(String identifier) {
        for (Cash cash : cashList) {
            if (cash.getIdentifier().equals(identifier)) {
                return cash;
            }
        }
        return null;
    }
    public static boolean idExists (String cashIdentifier){
        boolean exists=false;
        for (Cash c: cashList){
            if(c.getIdentifier().equals(cashIdentifier)){
                exists=true;
            }
        }
        return exists;
    }
    public static String generateRandomIdentifier() {
        Random random = new Random();
        int number = 1_000_000 + random.nextInt(9_000_000);
        return "UW" + number;
    }
    public static StringBuilder showCashList() {
        StringBuilder sb = new StringBuilder();
        if (cashList.isEmpty()) {
            sb.append("No hay cajeros registrados en el sistema.");
            return sb;
        }
        Collections.sort(cashList, Comparator.comparing(Cash::getName, String.CASE_INSENSITIVE_ORDER));
        sb.append("Cash:\n");
        for (Cash c : cashList) {
            sb.append(c.toString()).append("\n");
        }
        return sb;
    }
}
