package misaka.bank;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

/**
 * Bank card tools.
 * @author Kahle
 */
public class BankCardUtils {
    private static Logger log = LoggerFactory.getLogger(BankCardUtils.class);
    private static BankCardIssuerProvider bankCardIssuerProvider;

    public static BankCardIssuerProvider getBankCardIssuerProvider() {

        return bankCardIssuerProvider;
    }

    public static void setBankCardIssuerProvider(BankCardIssuerProvider bankCardIssuerProvider) {
        Assert.notNull(bankCardIssuerProvider, "Parameter \"bankCardIssuerProvider\" must not null. ");
        log.info("Set bank card provider: {}", bankCardIssuerProvider.getClass().getName());
        BankCardUtils.bankCardIssuerProvider = bankCardIssuerProvider;
    }

    public static BankCardIssuer issuerInfo(String bankCardNumber) {

        return getBankCardIssuerProvider().issuerInfo(bankCardNumber);
    }

}
