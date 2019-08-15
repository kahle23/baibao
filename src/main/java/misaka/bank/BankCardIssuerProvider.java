package misaka.bank;

/**
 * Bank card issuer provider.
 * @author Kahle
 */
public interface BankCardIssuerProvider {

    /**
     * Query the bank card issuer according to the bank card number.
     * @param bankCardNumber Bank card number
     * @return Bank card issuer object
     */
    BankCardIssuer findBankCardIssuer(String bankCardNumber);

}
