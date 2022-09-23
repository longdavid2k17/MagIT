package pl.kantoch.dawid.magit.utils;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomStringGenerator
{
    public static String generateRandomString(int length)
    {
        boolean useLetters = true;
        boolean useNumbers = false;
        return RandomStringUtils.random(length, useLetters, useNumbers);
    }
}
