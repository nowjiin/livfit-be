package com.hotspot.livfit.user.util;

import java.util.Random;

public class NicknameGenerator {
  private static final String[] ACTIONS = {
    "용감한", "똑똑한", "날쌘", "젠틀한", "대담한",
    "사나운", "조용한", "행복한", "열정적인", "강력한",
    "현명한", "신비로운", "빠른", "침착한", "힘찬",
    "활기찬", "차분한", "긍정적인", "정의로운"
  };

  private static final String[] ANIMALS = {
    "사자", "호랑이", "곰", "늑대", "독수리",
    "여우", "표범", "돌고래", "매", "용",
    "코끼리", "사슴", "판다", "고양이", "강아지",
    "토끼", "올빼미", "독수리", "원숭이", "거북이"
  };

  public static String generateNickname() {
    Random random = new Random();
    String action = ACTIONS[random.nextInt(ACTIONS.length)];
    String animal = ANIMALS[random.nextInt(ANIMALS.length)];
    return action + " " + animal;
  }
}
