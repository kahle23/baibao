package baibao.extension.bank.support.supfree;

import artoria.action.handler.AbstractClassicActionHandler;
import artoria.net.HttpMethod;
import artoria.net.HttpRequest;
import artoria.net.HttpResponse;
import artoria.net.HttpUtils;
import artoria.util.CollectionUtils;
import artoria.util.ObjectUtils;
import artoria.util.StringUtils;
import baibao.extension.bank.BankCard;
import baibao.extension.bank.BankCardQuery;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static artoria.common.Constants.*;

/**
 * Bank card information provider based on website "bankcard.supfree.net".
 * @author Kahle
 */
public class SupfreeBankCardActionHandler extends AbstractClassicActionHandler {
    private static final Logger log = LoggerFactory.getLogger(SupfreeBankCardActionHandler.class);

    private String cutoutValue(String data) {
        if (StringUtils.isBlank(data)) { return null; }
        int indexOf = data.indexOf("：");
        if (indexOf != -1 && indexOf < data.length()) {
            data = data.substring(indexOf + 1);
        }
        return data.trim();
    }

    @Override
    public <T> T execute(Object input, Class<T> clazz) {
        String bankCardNumber = null;
        try {
            isSupport(new Class[]{BankCard.class}, clazz);
            BankCardQuery bankCardIssuerQuery = (BankCardQuery) input;
            bankCardNumber = bankCardIssuerQuery.getBankCardNumber();
            HttpRequest request = new HttpRequest();
            request.setMethod(HttpMethod.GET);
            request.setUrl("https://bankcard.supfree.net/tongku.asp?cardno=" + bankCardNumber);
            HttpResponse response = HttpUtils.getHttpClient().execute(request);
            String html = response.getBodyAsString("GB2312");
            Document document = Jsoup.parse(html);

            Elements cdivElements = document.getElementsByClass("cdiv");
            if (CollectionUtils.isEmpty(cdivElements)) { return null; }
            if (cdivElements.size() < TWO) { return null; }
            Element cdivElement = cdivElements.get(ONE);
            log.info(
                    "Find \"{}\" in \"bankcard.supfree.net\", and result is \"{}\". "
                    , bankCardNumber
                    , cdivElement.text()
            );
            Elements pElements = cdivElement.getElementsByTag("p");
            if (CollectionUtils.isEmpty(pElements)) { return null; }
            if (pElements.size() < SEVEN) { return null; }

            String issuerIdentificationNumber = cutoutValue(pElements.get(ZERO).text());
            String bankName = cutoutValue(pElements.get(ONE).text());
            String organizationCode = cutoutValue(pElements.get(TWO).text());
            String bankCardName = cutoutValue(pElements.get(THREE).text());
            String bankCardType = cutoutValue(pElements.get(FOUR).text());
            String bankCardNumberLength = cutoutValue(pElements.get(SIX).text());
            if (bankCardNumberLength != null && bankCardNumberLength.contains("位")) {
                int endIndex = bankCardNumberLength.length() - ONE;
                bankCardNumberLength = bankCardNumberLength.substring(ZERO, endIndex);
            }

            BankCard bankCard = new BankCard();
            bankCard.setBankCardNumber(bankCardNumber);
            bankCard.setBankName(bankName);
            bankCard.setOrganizationCode(organizationCode);
            bankCard.setBankCardName(bankCardName);
            bankCard.setBankCardType(bankCardType);
            bankCard.setIssuerIdentificationNumber(issuerIdentificationNumber);
            bankCard.setBankCardNumberLength(bankCardNumberLength);
            return ObjectUtils.cast(bankCard);
        }
        catch (Exception e) {
            log.info("Failed to find \"{}\" in \"bankcard.supfree.net\". ", bankCardNumber, e);
            return null;
        }
    }

}
