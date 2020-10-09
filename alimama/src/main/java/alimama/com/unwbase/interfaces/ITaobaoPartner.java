package alimama.com.unwbase.interfaces;

public interface ITaobaoPartner {
    boolean isDetailJumpTaobao();

    boolean isOrderJumpTaobao();

    boolean isTaobaoCartLink(String str);

    boolean isTaobaoCreateOrderLink(String str);

    boolean isTaobaoDetailLink(String str);

    boolean isTaobaoOrderListLink(String str);

    boolean isTaobaoShopLink(String str);
}
