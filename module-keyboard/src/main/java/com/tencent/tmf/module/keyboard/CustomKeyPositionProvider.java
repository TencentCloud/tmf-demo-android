package com.tencent.tmf.module.keyboard;

import android.content.Context;

import com.tencent.tmf.keyboard.common.KeyPosition;
import com.tencent.tmf.keyboard.common.PrimaryKeyCode;
import com.tencent.tmf.keyboard.config.AbstractKeyPositionProvider;
import com.tencent.tmf.keyboard.utils.ShuffleUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomKeyPositionProvider extends AbstractKeyPositionProvider {
    @Override
    public List<KeyPosition> provideLetterKeyBoardPositions(Context context) {
        boolean needRefresh = false;
        synchronized (mLetters) {
            if (mLetters.isEmpty()) {
                needRefresh = true;
            }
        }

        if (needRefresh) {
            List<KeyPosition> ret = new ArrayList<>(internalLetterKeyboardData());
            if (mShuffleLetterKeyPosition) {
                ShuffleUtil.shuffle(ret);
            }
            appFnKeyItemForLetterKeyboard(ret, context);
            synchronized (mLetters) {
                mLetters.addAll(ret);
            }
        }
        return new ArrayList<>(mLetters);
    }

    @Override
    public List<KeyPosition> provideNumberKeyBoardPositions(Context context) {
        boolean needRefresh = false;
        synchronized (mNumbers) {
            if (mNumbers.isEmpty()) {
                needRefresh = true;
            }
        }

        if (needRefresh) {
            List<KeyPosition> ret = new ArrayList<>(internalNumberKeyboardData());
            if (mShuffleNumberKeyPosition) {
                ShuffleUtil.shuffle(ret);
            }
            appFnKeyItemForNumberKeyboard(ret, context);
            synchronized (mNumbers) {
                mNumbers.addAll(ret);
            }
        }
        return new ArrayList<>(mNumbers);
    }

    @Override
    public List<KeyPosition> provideSymbolKeyBoardPositions(Context context) {
        boolean needRefresh = false;
        synchronized (mSymbols) {
            if (mSymbols.isEmpty()) {
                needRefresh = true;
            }
        }

        if (needRefresh) {
            List<KeyPosition> ret = new ArrayList<>(internalSymbolKeyboardData());
            if (mShuffleSymbolKeyPosition) {
                ShuffleUtil.shuffle(ret);
            }
            appFnKeyItemForSymbolKeyboard(ret, context);
            synchronized (mSymbols) {
                mSymbols.addAll(ret);
            }
        }
        return new ArrayList<>(mSymbols);
    }

    /**
     * 默认为qwerty键位布局
     */
    private List<KeyPosition> internalLetterKeyboardData() {
        final String letters = "qwertyuiopasdfghjklzxcvbnm";
        List<KeyPosition> keys = new ArrayList<>();
        KeyPosition.KeyPositionBuilder builder = null;
        AtomicInteger indexer = new AtomicInteger(0);
        char[] chars = letters.toCharArray();
        char[] values = genMagicCodeArrayAndShuffle(chars, true);
        char magicChar;
        for (char letter : chars) {
            int index = indexer.getAndIncrement();
            magicChar = values[index];
            builder = new KeyPosition.KeyPositionBuilder(PrimaryKeyCode.KEYCODE_INPUT);
            builder.setDisplayContent(String.valueOf(letter), -1, -1)
                    .setMagicChar(magicChar, Character.toUpperCase(magicChar));
            keys.add(builder.build());
        }
        return keys;
    }

    private char[] genMagicCodeArrayAndShuffle(char[] chars, boolean shuffle) {
        if (null == chars || chars.length <= 0) {
            return null;
        }
        char[] values = new char[chars.length];
        System.arraycopy(chars, 0, values, 0, chars.length);
        if (shuffle) {
            ShuffleUtil.shuffleCharArray(values);
        }
        return values;
    }


    private List<KeyPosition> internalNumberKeyboardData() {
        final String numberText = "0123456789";
        List<KeyPosition> keys = new ArrayList<>();
        KeyPosition.KeyPositionBuilder builder = null;
        char[] chars = numberText.toCharArray();
        AtomicInteger indexer = new AtomicInteger(0);
        char[] values = genMagicCodeArrayAndShuffle(chars, true);
        for (char text : chars) {
            int index = indexer.getAndIncrement();
            builder = new KeyPosition.KeyPositionBuilder(PrimaryKeyCode.KEYCODE_INPUT);
            builder.setDisplayContent(String.valueOf(text), -1, -1).setMagicChar(values[index], values[index]);
            keys.add(builder.build());
        }
        return keys;
    }

    private List<KeyPosition> internalSymbolKeyboardData() {
        final String symbols = "_\\{}[]^*+=-/:;()$&@\"#%.,<>?!'";
        List<KeyPosition> keys = new ArrayList<>();
        KeyPosition.KeyPositionBuilder builder = null;
        AtomicInteger indexer = new AtomicInteger(0);
        char[] chars = symbols.toCharArray();
        char[] values = genMagicCodeArrayAndShuffle(chars, true);
        for (char letter : chars) {
            int index = indexer.getAndIncrement();
            builder = new KeyPosition.KeyPositionBuilder(PrimaryKeyCode.KEYCODE_INPUT);
            builder.setDisplayContent(String.valueOf(letter), -1, -1);
            builder.setMagicChar(values[index], values[index]);
            keys.add(builder.build());
        }
        return keys;
    }
}
