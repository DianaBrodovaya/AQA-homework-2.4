package ru.netology.web.test;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;
import ru.netology.web.page.TransferPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;

public class MoneyTransferTest {
    public DashboardPage dashboardPage;

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCode(authInfo);
        dashboardPage = verificationPage.validVerify(verificationCode);
    }

    @Test
    void shouldTransferMoneyFromFirstToSecondCard() {
        val firstCardInfo = DataHelper.getFirstCardInfo();
        firstCardInfo.setBalance(dashboardPage.getCardBalance(firstCardInfo.getNumber()));
        val secondCardInfo = DataHelper.getSecondCardInfo();
        secondCardInfo.setBalance(dashboardPage.getCardBalance(secondCardInfo.getNumber()));
        val amount = DataHelper.getRandomAmount(firstCardInfo.getBalance());
        val transferPage = dashboardPage.selectCard(secondCardInfo.getNumber());
        val updatedDashboardPage = transferPage.fillInfo(firstCardInfo.getNumber(), amount);
        DataHelper.transferMoney(firstCardInfo, secondCardInfo, amount);
        assertEquals(updatedDashboardPage.getCardBalance(firstCardInfo.getNumber()), firstCardInfo.getBalance());
        assertEquals(updatedDashboardPage.getCardBalance(secondCardInfo.getNumber()), secondCardInfo.getBalance());
    }

    @Test
    void shouldTransferMoneyFromSecondToFirstCard() {
        val firstCardInfo = DataHelper.getFirstCardInfo();
        firstCardInfo.setBalance(dashboardPage.getCardBalance(firstCardInfo.getNumber()));
        val secondCardInfo = DataHelper.getSecondCardInfo();
        secondCardInfo.setBalance(dashboardPage.getCardBalance(secondCardInfo.getNumber()));
        val amount = DataHelper.getRandomAmount(secondCardInfo.getBalance());
        val transferPage = dashboardPage.selectCard(firstCardInfo.getNumber());
        val updatedDashboardPage = transferPage.fillInfo(secondCardInfo.getNumber(), amount);
        DataHelper.transferMoney(secondCardInfo, firstCardInfo, amount);
        assertEquals(updatedDashboardPage.getCardBalance(firstCardInfo.getNumber()), firstCardInfo.getBalance());
        assertEquals(updatedDashboardPage.getCardBalance(secondCardInfo.getNumber()), secondCardInfo.getBalance());
    }

    @Test
    void shouldNotTransferMoneyWithEmptyFields() {
        val firstCardInfo = DataHelper.getFirstCardInfo();
        firstCardInfo.setBalance(dashboardPage.getCardBalance(firstCardInfo.getNumber()));
        val transferPage = dashboardPage.selectCard(firstCardInfo.getNumber());
        transferPage.fillInfo(null, null);
        TransferPage.checkError();
    }

    @Test
    void shouldNotTransferMoneyWithInvalidCardNumber() {
        val firstCardInfo = DataHelper.getFirstCardInfo();
        firstCardInfo.setBalance(dashboardPage.getCardBalance(firstCardInfo.getNumber()));
        val amount = DataHelper.getRandomAmount(firstCardInfo.getBalance());
        val transferPage = dashboardPage.selectCard(firstCardInfo.getNumber());
        transferPage.fillInfo(DataHelper.getRandomCardNumber(), amount);
        TransferPage.checkError();
    }

    @Test
    void shouldNotTransferMoneyFromFirstToSecondInvalidAmount() {
        val firstCardInfo = DataHelper.getFirstCardInfo();
        firstCardInfo.setBalance(dashboardPage.getCardBalance(firstCardInfo.getNumber()));
        val secondCardInfo = DataHelper.getSecondCardInfo();
        secondCardInfo.setBalance(dashboardPage.getCardBalance(secondCardInfo.getNumber()));
        val amount = DataHelper.getInvalidAmount(firstCardInfo.getBalance());
        val transferPage = dashboardPage.selectCard(secondCardInfo.getNumber());
        transferPage.fillInfo(firstCardInfo.getNumber(), amount);
        TransferPage.checkError();
    }

    @Test
    void shouldNotTransferMoneyFromSecondToFirstInvalidAmount() {
        val firstCardInfo = DataHelper.getFirstCardInfo();
        firstCardInfo.setBalance(dashboardPage.getCardBalance(firstCardInfo.getNumber()));
        val secondCardInfo = DataHelper.getSecondCardInfo();
        secondCardInfo.setBalance(dashboardPage.getCardBalance(secondCardInfo.getNumber()));
        val amount = DataHelper.getInvalidAmount(secondCardInfo.getBalance());
        val transferPage = dashboardPage.selectCard(firstCardInfo.getNumber());
        transferPage.fillInfo(secondCardInfo.getNumber(), amount);
        TransferPage.checkError();
    }

    @Test
    void shouldNotTransferMoneyFromFirstToSecondInvalidAmountZero() {
        val firstCardInfo = DataHelper.getFirstCardInfo();
        firstCardInfo.setBalance(dashboardPage.getCardBalance(firstCardInfo.getNumber()));
        val secondCardInfo = DataHelper.getSecondCardInfo();
        secondCardInfo.setBalance(dashboardPage.getCardBalance(secondCardInfo.getNumber()));
        val amount = 0;
        val transferPage = dashboardPage.selectCard(secondCardInfo.getNumber());
        transferPage.fillInfo(firstCardInfo.getNumber(), amount);
        TransferPage.checkError();
    }

    @Test
    void shouldNotTransferMoneyFromSecondToFirstInvalidAmountZero() {
        val firstCardInfo = DataHelper.getFirstCardInfo();
        firstCardInfo.setBalance(dashboardPage.getCardBalance(firstCardInfo.getNumber()));
        val secondCardInfo = DataHelper.getSecondCardInfo();
        secondCardInfo.setBalance(dashboardPage.getCardBalance(secondCardInfo.getNumber()));
        val amount = 0;
        val transferPage = dashboardPage.selectCard(firstCardInfo.getNumber());
        transferPage.fillInfo(secondCardInfo.getNumber(), amount);
        TransferPage.checkError();
    }

    @Test
    void shouldNotTransferMoneyFromTheSameCard() {
        val firstCardInfo = DataHelper.getFirstCardInfo();
        firstCardInfo.setBalance(dashboardPage.getCardBalance(firstCardInfo.getNumber()));
        val amount = DataHelper.getRandomAmount(firstCardInfo.getBalance());
        val transferPage = dashboardPage.selectCard(firstCardInfo.getNumber());
        transferPage.fillInfo(firstCardInfo.getNumber(), amount);
        TransferPage.checkError();
    }
}
