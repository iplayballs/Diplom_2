package data;

import org.apache.commons.lang3.RandomStringUtils;

public class UserDataGenerate {

    public static String generateName(){
        return "name" + RandomStringUtils.randomAlphabetic(4);
    }

    public static String generatePassword(){
        return "password" + RandomStringUtils.randomAlphabetic(4);
    }
    public static String generateEmail(){
        return RandomStringUtils.randomAlphabetic(4) + "@" + RandomStringUtils.randomAlphabetic(4) + ".ru";
    }
}
