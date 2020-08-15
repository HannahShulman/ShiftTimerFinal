package com.shift.timer.animations;


import java.io.Serializable;

public class RevealAnimationSetting implements Serializable {
  public int centerX;
  public  int centerY;
  public  int width;
  public int height;

  public RevealAnimationSetting(int centerX, int centerY, int width, int height) {
    this.centerX = centerX;
    this.centerY = centerY;
    this.width = width;
    this.height = height;
  }

  public static RevealAnimationSetting with(int centerX, int centerY, int width, int height) {
    return new RevealAnimationSetting(centerX, centerY, width, height);
  }
}