package ru.otus.java;

import ru.otus.java.atm.*;
import ru.otus.java.atmDepartment.*;

import java.util.Arrays;
import java.util.List;

/**
 * ...
 */
public class Main {
    public static void main(String[] args) {
        List<Cell> config1 = Arrays.asList(new Cell(5, 50), new Cell(100, 20),
                new Cell(1000, 20), new Cell(1,200));
        List<Cell> config2 = Arrays.asList(new Cell(1, 350),
                new Cell(1000, 20), new Cell(50,200));
        List<Cell> config3 = Arrays.asList(new Cell(5, 550), new Cell(100, 20),
                new Cell(50, 200), new Cell(1,200));

        ATM atm1 = new ATM(config1);
        ATM atm2 = new ATM(config2);
        ATM atm3 = new ATM(config3);
        ATM atm4 = new ATM(config1);
        ATM atm5 = new ATM(config2);
        ATM atm6 = new ATM(config3);

        ATMDepartment dep1 = new ATMDepartment();
        ATMDepartment dep2 = new ATMDepartment();

        ATMObserver atmObs1 = new ATMObserver(atm1, dep1);
        ATMObserver atmObs2 = new ATMObserver(atm2, dep1);
        ATMObserver atmObs3 = new ATMObserver(atm3, dep1);
        ATMObserver atmObs4 = new ATMObserver(atm4, dep2);
        ATMObserver atmObs5 = new ATMObserver(atm5, dep1);
        ATMObserver atmObs6 = new ATMObserver(atm6, dep2);

        dep1.updateTotalBalance();
        dep2.updateTotalBalance();

        System.out.println("ATM department No.1 initial balance: " + dep1.getDepTotalBalance());
        atm1.withdraw(1389);
        dep1.updateTotalBalance();
        System.out.println("ATM department No.1 current balance: " + dep1.getDepTotalBalance());
        atm2.withdraw(659);
        dep1.updateTotalBalance();
        System.out.println("ATM department No.1 current balance: " + dep1.getDepTotalBalance());
        atm3.withdraw(5452);
        dep1.updateTotalBalance();
        System.out.println("ATM department No.1 current balance: " + dep1.getDepTotalBalance());
        atm5.withdraw(3089);
        dep1.updateTotalBalance();
        System.out.println("ATM department No.1 current balance: " + dep1.getDepTotalBalance());
        dep1.initStateRestoration();
        dep1.updateTotalBalance();
        System.out.println("ATM department No.1 balance after restoration: " + dep1.getDepTotalBalance());

        System.out.println("===========================================================================");

        System.out.println("ATM department No.2 initial balance: " + dep2.getDepTotalBalance());
        atm4.withdraw(10389);
        dep2.updateTotalBalance();
        System.out.println("ATM department No.2 current balance: " + dep2.getDepTotalBalance());
        atm6.withdraw(4659);
        dep2.updateTotalBalance();
        System.out.println("ATM department No.2 current balance: " + dep2.getDepTotalBalance());
        dep2.initStateRestoration();
        dep2.updateTotalBalance();
        System.out.println("ATM department No.2 balance after restoration: " + dep2.getDepTotalBalance());
    }
}
