package com.hotspot.livfit.store.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Table(name = "item")
@Getter
@Setter
@NoArgsConstructor
public class Item {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id")
  private Category category;

  @Column(nullable = false)
  private String name;

  @Column(name = "point_price", nullable = false)
  private Long pointPrice;

  @Column(name = "cash_price", nullable = false)
  private Long cashPrice;
}
