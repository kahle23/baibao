package misaka.bank;

/**
 * The bank card issuer provider.
 * @author Kahle
 */
public interface BankCardIssuerProvider {

    /**
     * Query the bank card issuer according to the bank card number.
     * @param bankCardNumber The bank card number
     * @return The bank card issuer
     */
    BankCardIssuer issuerInfo(String bankCardNumber);

}
