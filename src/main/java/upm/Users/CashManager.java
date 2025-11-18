package upm.Users;

import java.util.*;

public class CashManager {
    public static ArrayList<Cash> cashList = new ArrayList<>();

    public CashManager() {
        cashList = new ArrayList<>();
    }

    public ArrayList<Cash> getCashList() {
        return cashList;
    }

    public static boolean addCash(Cash cash) {
        if (cash == null) return false;
        for (Cash c : cashList) {
            if (c.getId().equals(cash.getId())) {
                return false;
            }
        }
        return cashList.add(cash);
    }

    public static boolean removeCashById(String id) {
        Iterator<Cash> it = cashList.iterator();
        boolean removed = false;
        while (it.hasNext() && !removed) {
            if (it.next().getId().equals(id)) {
                it.remove();
                removed = true;
            }
        }
        return removed;
    }

    public static Cash getCashById(String id) {
        for (Cash c : cashList) {
            if (c.getId().equals(id)) {
                return c;
            }
        }
        return null;
    }
    private static final Random random = new Random();

    public static String generateRandomId() {
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
