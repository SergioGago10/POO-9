package upm.Users;

import java.util.*;

public final class CashManager {

    private static final List<Cash> cashList = new ArrayList<>();

    private CashManager() { }

    public static ArrayList<Cash> getCashList() {
        return new ArrayList<>(cashList);
    }

    public static boolean addCash(Cash cash) {
        if (cash == null) return false;

        for (Cash c : cashList) {
            if (Objects.equals(c.getIdentifier(), cash.getIdentifier())) {
                return false;
            }
        }
        return cashList.add(cash);
    }

    public static boolean removeCashByIdentifier(String identifier) {
        if (identifier == null) return false;

        Iterator<Cash> it = cashList.iterator();
        while (it.hasNext()) {
            Cash current = it.next();
            if (Objects.equals(current.getIdentifier(), identifier)) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    public static Cash getCashByIdentifier(String identifier) {
        if (identifier == null) return null;

        for (Cash cash : cashList) {
            if (Objects.equals(cash.getIdentifier(), identifier)) {
                return cash;
            }
        }
        return null;
    }

    public static boolean idExists(String cashIdentifier) {
        return getCashByIdentifier(cashIdentifier) != null;
    }

    public static String generateRandomIdentifier() {
        Random random = new Random();
        int number = 1_000_000 + random.nextInt(9_000_000);
        return "UW" + number;
    }
}
