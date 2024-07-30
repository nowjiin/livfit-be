package com.hotspot.livfit.today_exercise.dto;

import java.time.DayOfWeek;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TodayExerciseDTO {
  private String loginId;
  private DayOfWeek dayOfWeek;
  private String success;
}
