package com.company;

import java.util.Random;

public class Main {

    private static Account[] prepareAccounts(int num, Random rand) {
        final Account[] accounts = new Account[num];
        for (int i = 0; i < num; i++) {
            int randomBalance = rand.nextInt(10);
            accounts[i] = new Account(randomBalance);
            System.out.println("Account " + i + ": " + accounts[i].getBalance());
        }
        System.out.println();
        return accounts;
    }

    private static void tryToDoTransferOperations(int num, Random rand, Account[] accounts, int amount){
        for (int i = 0; i < num; i++) {
            final int curThread = i;
            new Thread(() -> {
                int randomAccountNum = rand.nextInt(num);
                if (randomAccountNum == curThread) {
                    randomAccountNum = (randomAccountNum + 1) % num;
                }
                System.out.println("Try transfer 100 from account " + randomAccountNum + " with sum = " +
                        accounts[randomAccountNum].getBalance() + "  to account " + curThread);
                accounts[curThread].transfer(amount, accounts[randomAccountNum], rand.nextBoolean());
            }).start();
        }
        System.out.println();
    }

    private static void doDepositOperations(int num, Account[] accounts, int amount){
        for (int i = 0; i < num; i++) {
            System.out.println("Deposit 1000 to account " + i);
            accounts[i].deposit(amount);
        }
    }

    public static void main(String[] args) {
        int num = 10;
        final Random rand = new Random();
        Account[] accounts = prepareAccounts(num, rand);
        tryToDoTransferOperations(num, rand, accounts, 100);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        doDepositOperations(num, accounts, 1000);
    }
}
