package com.company;

public class Account {
    private int balance;
    private int preferredNum;
    private final Object transferLock;

    public Account() {
        this.transferLock = new Object();
        balance = 0;
        preferredNum = 0;
    }

    public Account(int k) {
        this.transferLock = new Object();
        balance = k;
        preferredNum = 0;
    }

    public int getBalance() {
        return balance;
    }

    public void deposit(int k) {
        synchronized (this) {
            balance += k;
            this.notifyAll();
            //System.out.println("Deposit " + k);
        }
    }

    public void  withdraw(int k) {
        synchronized (this) {
            while (balance < k || preferredNum > 0) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            balance -= k;
            //System.out.println("Withdraw " + k);
        }
    }

    public void  preferredWithdraw(int k) {
        synchronized (this) {
            preferredNum++;
            while (balance < k) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            balance -= k;
            preferredNum--;
            this.notifyAll();
        }
    }

    public void transfer(int k, Account reserve, boolean isPreferred) {
        synchronized (transferLock) {
            if (isPreferred) {
                reserve.preferredWithdraw(k);
            } else {
                reserve.withdraw(k);
            }
            deposit(k);
            System.out.println("Transfer " + k);
        }
    }
}
