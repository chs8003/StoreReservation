package com.hyeonsik.boot.vo;

import java.util.List;

import lombok.Data;

@Data
public class MenuCategory {
    private String categoryName;
    private List<MenuVo> menuList;
}
