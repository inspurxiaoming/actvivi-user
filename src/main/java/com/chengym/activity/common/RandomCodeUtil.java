package com.chengym.activity.common;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * 编码生成器
 * @author xushiqiang
 * @version 1.0
 */
public class RandomCodeUtil
{
     private static DecimalFormat df = new DecimalFormat("#0.00");
    /**
     * 生成请求流水号[长度为20]
     *[当前毫秒级时间+三位随机码]
     * @return
     */
    public static String getSerialsNumber()
    {
        // 获取当前时间[毫秒级]
        String head = getCurrentSSSTime();

        // 三位随机码
        String body = String.valueOf((int) (Math.random() * 900) + 100);

        return head + body;
    }

    /**
     * 返回当前时间[毫秒级]
     *
     * @return mmssSSS 格式时间
     */
    public static String getCurrentSSSTime()
    {
        return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
    }

    /**
     * 生成UUID序列
     *
     * @return
     */
    public static String getUUID()
    {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 生成6位的随机码
     * [生成机制：随机生成UUID序列，并随机截取6位连续的字符串]
     *
     * @return
     */
    public static String getValidateCode()
    {
        // 先生成一个32位的随机数
        String randomCode = getUUID();
        // 从这个32位数中，随机截取6位连续的字符串
        int begin = (int) (Math.random() * 26) + 1;
        randomCode = randomCode.substring(begin, begin + 6);

        return randomCode;
    }

    /**
     * 生成6位随机数字
     * @return
     */
    public static String getRandomNum()
    {
        return String.valueOf((int) (Math.random() * 900000) + 100000);
    }

    /**
     * 将金额转换成字符串型
     * [小数点保留两位]
     * @param money 金额[单位：分]
     * @return 返回两位小数的金额字符串
     */
    public static String moneyToString(int money)
    {
        double a = (double)money/ 100;

        return df.format(a);
    }

    /**
     * 生成不等于0的随机金额
     *
     * @return 返回随机金额【int型整数】
     */
    public static int getRandomMoney()
    {
        Random random = new Random();
        int money = random.nextInt(100);
        if(money==0)
        {
            money++;
        }

        return money;
    }
}
