package com.kgame.game;

import java.util.ArrayList;
import java.util.List;

public class Level {
    protected List<Integer> levelMap;
    protected Integer width, height;

    Level(Integer width, Integer height) {
        this.width = width;
        this.height = height;
        this.levelMap = new ArrayList<>();
    }

    public Integer GetWidth() {
        return width;
    }

    public Integer GetHeight() {
        return height;
    }

    public void AddLine(Integer value) {
        for (int w = 0; w < width; w++) levelMap.add(value);
    }

    public void AddCell(Integer value) {
        levelMap.add(value);
    }

    public void SetCell(Integer index, Integer value) {
        levelMap.set(index, value);
    }

    public Integer GetCell(Integer index) {
        return levelMap.get(index);
    }
}
