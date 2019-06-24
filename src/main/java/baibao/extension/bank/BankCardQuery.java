package baibao.extension.bank;

import java.io.Serializable;

public class BankCardQuery implements Serializable {
    private String bankCardNumber;
    private String language;

    public BankCardQuery(String bankCardNumber) {

        this.bankCardNumber = bankCardNumber;
    }

    public BankCardQuery() {

    }

    public String getBankCardNumber() {

        return bankCardNumber;
    }

    public void setBankCardNumber(String bankCardNumber) {

        this.bankCardNumber = bankCardNumber;
    }

    public String getLanguage() {

        return language;
    }

    public void setLanguage(String language) {

        this.language = language;
    }

}
