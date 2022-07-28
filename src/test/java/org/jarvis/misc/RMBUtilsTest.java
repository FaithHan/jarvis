package org.jarvis.misc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RMBUtilsTest {

    @Test
    void convertToBig() {
        assertEquals(RMBUtils.convertToBig(100L), "壹圆整");
        assertEquals(RMBUtils.convertToBig(1000L), "壹拾圆整");
        assertEquals(RMBUtils.convertToBig(2070L), "贰拾圆柒角整");
        assertEquals(RMBUtils.convertToBig(1070), "壹拾圆柒角整");
        assertEquals(RMBUtils.convertToBig(1850), "壹拾捌圆伍角整");
        assertEquals(RMBUtils.convertToBig(20050), "贰佰圆伍角整");
        assertEquals(RMBUtils.convertToBig(200000L), "贰仟圆整");
        assertEquals(RMBUtils.convertToBig(5000000L), "伍万圆整");
        assertEquals(RMBUtils.convertToBig(50000000L), "伍拾万圆整");
        assertEquals(RMBUtils.convertToBig(500000000L), "伍佰万圆整");
        assertEquals(RMBUtils.convertToBig(5000000000L), "伍仟万圆整");
        assertEquals(RMBUtils.convertToBig(50000000000L), "伍亿圆整");
        assertEquals(RMBUtils.convertToBig(500000000000L), "伍拾亿圆整");
        assertEquals(RMBUtils.convertToBig(500000000100L), "伍拾亿零壹圆整");
        assertEquals(RMBUtils.convertToBig(500000002100L), "伍拾亿零贰拾壹圆整");
        assertEquals(RMBUtils.convertToBig(500000042100L), "伍拾亿零肆佰贰拾壹圆整");
        assertEquals(RMBUtils.convertToBig(500000542100L), "伍拾亿零伍仟肆佰贰拾壹圆整");
        assertEquals(RMBUtils.convertToBig(500003542100L), "伍拾亿零叁万伍仟肆佰贰拾壹圆整");
        assertEquals(RMBUtils.convertToBig(500063542100L), "伍拾亿零陆拾叁万伍仟肆佰贰拾壹圆整");
        assertEquals(RMBUtils.convertToBig(500463542100L), "伍拾亿零肆佰陆拾叁万伍仟肆佰贰拾壹圆整");
        assertEquals(RMBUtils.convertToBig(502463542100L), "伍拾亿贰仟肆佰陆拾叁万伍仟肆佰贰拾壹圆整");
        assertEquals(RMBUtils.convertToBig(522463542100L), "伍拾贰亿贰仟肆佰陆拾叁万伍仟肆佰贰拾壹圆整");
        assertEquals(RMBUtils.convertToBig(1522463542100L), "壹佰伍拾贰亿贰仟肆佰陆拾叁万伍仟肆佰贰拾壹圆整");
        assertEquals(RMBUtils.convertToBig(21522463542100L), "贰仟壹佰伍拾贰亿贰仟肆佰陆拾叁万伍仟肆佰贰拾壹圆整");
        assertEquals(RMBUtils.convertToBig(50002100L), "伍拾万零贰拾壹圆整");
        assertEquals(RMBUtils.convertToBig(500082100L), "伍佰万零捌佰贰拾壹圆整");
        assertEquals(RMBUtils.convertToBig(505000650100L), "伍拾亿伍仟万陆仟伍佰零壹圆整");
        assertEquals(RMBUtils.convertToBig(55030000100L), "伍亿伍仟零叁拾万零壹圆整");
    }
}