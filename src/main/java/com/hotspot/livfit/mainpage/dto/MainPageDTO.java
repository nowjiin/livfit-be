package com.hotspot.livfit.mainpage.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.hotspot.livfit.today_exercise.dto.TodayExerciseDTO;

@Getter
@Setter
@AllArgsConstructor
public class MainPageDTO {
  private String loginId;
  private List<TodayExerciseDTO> exercises;
}
