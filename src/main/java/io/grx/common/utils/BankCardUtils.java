package io.grx.common.utils;

public class BankCardUtils {

    /**
     * 匹配银行卡
     * @param cardNo
     * @return
     */
    public static boolean matchLuhn(String cardNo) {
        try {
            int[] cardNoArr = new int[cardNo.length()];
            for (int i = 0; i < cardNo.length(); i++) {
                cardNoArr[i] = Integer.valueOf(String.valueOf(cardNo.charAt(i)));
            }
            for (int i = cardNoArr.length - 2; i >= 0; i -= 2) {
                cardNoArr[i] <<= 1;
                cardNoArr[i] = cardNoArr[i] / 10 + cardNoArr[i] % 10;
            }
            int sum = 0;
            for (int i = 0; i < cardNoArr.length; i++) {
                sum += cardNoArr[i];
            }
            return sum % 10 == 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println(matchLuhn("6226097809910681"));

        System.out.println(matchLuhn("370286000882347"));

        System.out.println(matchLuhn("370286000882348"));
    }
}
