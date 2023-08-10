package com.hyeonsik.boot.vo;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name= "name")
    private String name; 

    // 선택한 가게에 맞게 리뷰 작성하기 위한 가게 번호
    @JoinColumn(name = "admin_no")
    private Integer adminNo;

    // 추후 가게에 별점을 나타내기 위한 별점 컬럼
    private double rating;
    
    private String imageNamed;

    // 리뷰 작성 내용
    private String content;

    @Column(name = "date")
    private LocalDate date;


}