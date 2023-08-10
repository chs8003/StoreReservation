package com.hyeonsik.boot.vo;

import javax.persistence.*;

import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name= "blacklist")
public class BlackList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_no")
    private UserVo userVo;

    @ManyToOne
    @JoinColumn(name = "admin_no")
    private Restaurant restaurant;
    
    @Column(name="content")
    private String content;
}