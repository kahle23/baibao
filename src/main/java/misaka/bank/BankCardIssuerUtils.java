package misaka.bank;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

/**
 * Bank card issuer tools.
 * @author Kahle
 */
public class BankCardIssuerUtils {
    private static Logger log = LoggerFactory.getLogger(BankCardIssuerUtils.class);
    private static BankCardIssuerProvider bankCardIssuerProvider;

    public static BankCardIssuerProvider getBankCardIssuerProvider() {

        return bankCardIssuerProvider;
    }

    public static void setBankCardIssuerProvider(BankCardIssuerProvider bankCardIssuerProvider) {
        Assert.notNull(bankCardIssuerProvider, "Parameter \"bankCardIssuerProvider\" must not null. ");
        log.info("Set bank card issuer provider: {}", bankCardIssuerProvider.getClass().getName());
        BankCardIssuerUtils.bankCardIssuerProvider = bankCardIssuerProvider;
    }

    public static BankCardIssuer findBankCardIssuer(String bankCardNumber) {

        return getBankCardIssuerProvider().findBankCardIssuer(bankCardNumber);
    }

}
