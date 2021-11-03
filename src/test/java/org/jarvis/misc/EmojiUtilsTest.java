package org.jarvis.misc;

import com.vdurmont.emoji.EmojiParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmojiUtilsTest {

    @Test
    void isEmoji() {
        System.out.println(EmojiUtils.isEmoji("🐔"));
    }

    @Test
    void containsEmoji() {
        System.out.println(EmojiUtils.containsEmoji("🐔11"));
    }

    @Test
    void toAlias() {
        System.out.println(EmojiUtils.toAlias("👧11"));
        System.out.println(EmojiUtils.toAlias("🐔11"));
    }

    @Test
    void toUnicode() {
        System.out.println(EmojiUtils.toHtmlHex("🐔11"));
    }

    @Test
    public void removeAllEmojis() {
        String x = EmojiParser.removeAllEmojis("👧11");
        assertEquals("11", x);
        System.out.println(x);
    }
}