package com.morewen.gateway;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
@Slf4j
public class test {
    @Test
    public void run() {
        Integer b = 128;
        int temp = b;
        log.info(String.valueOf(b == temp));
        // System.out.println(b == temp);
        String a = "a" + "b" + "c";
        String c = new String("ab") + "c";
        log.info(String.valueOf(a == c));
        // System.out.println(a == c);
        log.info(String.valueOf(a.equals(c)));
        // System.out.println(a.equals(c));
    }
}
