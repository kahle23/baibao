package misaka.pay;

public interface PayProvider {

    OrderPayResult payOrder(OrderPayModel orderPayModel);

    OrderQueryResult queryOrder(OrderQueryModel orderQueryModel);

    OrderCloseResult closeOrder(OrderCloseModel orderCloseModel);

    PayNotifyResult payNotify(PayNotifyModel payNotifyModel);

}
