package ru.otus.java.atmDepartment;

import ru.otus.java.atm.ATM;

/**
 * An observer wrapper for ATM dependent of ATMDepartment which acts as a subject.
 */
public class ATMObserver {
    private final ATM atm;
    private final ATMDepartment department;

    public ATMObserver(ATM atm, ATMDepartment department) {
        this.atm = atm;
        this.department = department;
        this.department.registerATM(this);
    }

    void restoreState() {
        atm.restoreState();
    }

    int reportBalance() {
        return atm.getBalance();
    }
}
