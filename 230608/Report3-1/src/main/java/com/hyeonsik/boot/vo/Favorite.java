package com.hyeonsik.boot.vo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.security.core.userdetails.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "favorites")
@Getter
@Setter
@NoArgsConstructor
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_id")
    private Long id;

    @Column(name = "user_no", nullable = false)
    private Long userNo;

    @ManyToOne
    @JoinColumn(name = "admin_no", nullable = false)
    private Restaurant restaurant;

    public Favorite(Long userNo, Restaurant restaurant) {
        this.userNo = userNo;
        this.restaurant = restaurant;
    }
}