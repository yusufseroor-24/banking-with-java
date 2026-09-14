package com.ga.project1.banking;

public enum CardType {
    STANDARD_MASTERCARD(5000, 10000, 20000, 100000, 200000),
    TITANIUM(10000, 20000, 40000, 100000, 200000),
    PLATINUM(20000, 40000, 80000, 100000, 200000);

    private final double withdrawLimit;
    private final double transferLimit;
    private final double transferLimitOwn;
    private final double DepositLimit;
    private final double DepositLimitOwn;

    CardType(double withdrawLimit, double transferLimit, double transferLimitOwn, double depositLimit, double depositLimitOwn) {
        this.withdrawLimit = withdrawLimit;
        this.transferLimit = transferLimit;
        this.transferLimitOwn = transferLimitOwn;
        DepositLimit = depositLimit;
        DepositLimitOwn = depositLimitOwn;
    }

    public double getDepositLimitOwn() {
        return DepositLimitOwn;
    }

    public double getDepositLimit() {
        return DepositLimit;
    }

    public double getTransferLimit() {
        return transferLimit;
    }

    public double getTransferLimitOwn() {
        return transferLimitOwn;
    }

    public double getWithdrawLimit() {
        return withdrawLimit;
    }
}
