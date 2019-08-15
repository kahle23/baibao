package misaka.bank;

/**
 * Bank card issuer object.
 * @see <a href="https://en.wikipedia.org/wiki/Bank_card">Bank card</a>
 * @see <a href="https://en.wikipedia.org/wiki/Issuing_bank">Issuing bank</a>
 * @author Kahle
 */
public class BankCardIssuer {
    /**
     * The bank name of the card.
     */
    private String bankName;
    /**
     * The organization code of the bank of the card.
     */
    private String organizationCode;
    /**
     * Bank card name.
     */
    private String bankCardName;
    /**
     * Bank card type.
     */
    private String bankCardType;
    /**
     * Issuer identification number.
     * @see <a href="https://en.wikipedia.org/wiki/Payment_card_number">Payment card number</a>
     */
    private String issuerIdentificationNumber;
    /**
     * The country where the card is located.
     */
    private String country;
    /**
     * Bank card number.
     */
    private String bankCardNumber;
    /**
     * Bank card number length.
     */
    private String bankCardNumberLength;

    public String getBankName() {

        return bankName;
    }

    public void setBankName(String bankName) {

        this.bankName = bankName;
    }

    public String getOrganizationCode() {

        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {

        this.organizationCode = organizationCode;
    }

    public String getBankCardName() {

        return bankCardName;
    }

    public void setBankCardName(String bankCardName) {

        this.bankCardName = bankCardName;
    }

    public String getBankCardType() {

        return bankCardType;
    }

    public void setBankCardType(String bankCardType) {

        this.bankCardType = bankCardType;
    }

    public String getIssuerIdentificationNumber() {

        return issuerIdentificationNumber;
    }

    public void setIssuerIdentificationNumber(String issuerIdentificationNumber) {

        this.issuerIdentificationNumber = issuerIdentificationNumber;
    }

    public String getCountry() {

        return country;
    }

    public void setCountry(String country) {

        this.country = country;
    }

    public String getBankCardNumber() {

        return bankCardNumber;
    }

    public void setBankCardNumber(String bankCardNumber) {

        this.bankCardNumber = bankCardNumber;
    }

    public String getBankCardNumberLength() {

        return bankCardNumberLength;
    }

    public void setBankCardNumberLength(String bankCardNumberLength) {

        this.bankCardNumberLength = bankCardNumberLength;
    }

}
