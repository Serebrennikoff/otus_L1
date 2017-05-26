package ru.otus.java.atmDepartment;

import java.util.HashSet;
import java.util.Set;

/**
 * Logically represents an ATM department which have a number of different registered machines
 * and can count total remainder and initiate restoration of their original state. Represents
 * "subject" abstraction for ATMObserver.
 */
public class ATMDepartment {
    private final Set<ATMObserver> atmObservers = new HashSet<>();
    private int totalBalance = 0;

    public boolean registerATM(ATMObserver atm) {
        return atmObservers.add(atm);
    }

    public boolean unregisterATM(ATMObserver atm) {
        return atmObservers.remove(atm);
    }

    public void initStateRestoration() {
        for(ATMObserver atmObs: atmObservers) {
            atmObs.restoreState();
        }
    }

    public void updateTotalBalance() {
        totalBalance = 0;
        for(ATMObserver atmObs: atmObservers) {
            totalBalance += atmObs.reportBalance();
        }
    }

    public int getDepTotalBalance() {
        return totalBalance;
    }
}
