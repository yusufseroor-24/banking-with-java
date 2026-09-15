package com.ga.project1.banking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.LocalDate;

public class AccountTest {

    @Test
    @DisplayName("rest the daily limit on new day - reset when date has changed")
    public final void restTheDailyLimitNewDay_ResetWhenDateHasChanged(){
        Account account = new Account(2, 100.0, "checking", "12345",
                true, 0, CardType.STANDARD_MASTERCARD,
                500.0, 200.0, 100.0, 300.0, 100.0,
                LocalDate.now().minusDays(1), "APPROVED"); //YESTERDAY

        account.resetDailyLimitNewDay();
        assertEquals(0, account.getWithdrawnToday());
        assertEquals(0, account.getTransferredToday());
        assertEquals(0, account.getTransferredOwnToday());
        assertEquals(0, account.getDepositedToday());
        assertEquals(0, account.getDepositedOwnToday());
        assertEquals(LocalDate.now(), account.getLastResetDate());
    }

    @Test
    @DisplayName("rest the daily limit on new day - does not reset when date is the same")
    public final void resetTheDailyLimitNewDay_DoesntResetWhenDateIsSame(){
        Account account = new Account(2, 100.0, "checking", "12345",
                true, 0, CardType.STANDARD_MASTERCARD,
                500.0, 200.0, 100.0, 300.0, 100.0,
                LocalDate.now(), "APPROVED");

        account.resetDailyLimitNewDay();
        assertEquals(500.0, account.getWithdrawnToday());
        assertEquals(200.0, account.getTransferredToday());
    }

    @Test
    @DisplayName("set balance to update balance")
    public final void setBalanceToUpdateBalance(){
        Account account = new Account(2, 100.0, "checking", "12345", true,
                0, CardType.STANDARD_MASTERCARD);

        account.setBalance(250.0);
        assertEquals(250.0, account.getBalance());
    }

    @Test
    @DisplayName("upgrade card upgrades from mastercard to titanium by setting card type")
    public final void upgradeCardTypeFromMastercardToTitaniumBySettingCardType(){
        Account account = new Account(2, 100.0, "checking", "12345", true,
                0, CardType.STANDARD_MASTERCARD);

        account.upgradeCard(account, "TITANIUM");
        assertEquals(CardType.TITANIUM, account.getCardType());
    }




}