package com.mmall.test;

import com.google.common.base.Splitter;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: lcx
 * @Date: 2018/12/19 14:25
 * @Description:
 */

public class BigDecimalTest {

    @Test
    public void test1() {
        System.out.println(0.05 + 0.01);
        System.out.println(1.0 - 0.42);
        System.out.println(4.015 * 100);
        System.out.println(123.3 / 100);
    }
    @Test
    public void test2() {
        BigDecimal b1 = new BigDecimal("0.05");
        BigDecimal b2 = new BigDecimal("0.01");
        System.out.println(b1.add(b2));

        List<String> baridList = Splitter.on("|").splitToList("12229999");
        System.out.println(baridList);

    }

}
