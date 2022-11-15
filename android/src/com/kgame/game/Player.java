package com.kgame.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Player {
    final KGame game;
    Rectangle player;

    public Player(KGame game, Float xPos, Float yPos, Float width, Float height)
    {
        player = new Rectangle();
        player.width = width;
        player.height = height;
        player.x = xPos;
        player.y = yPos;

        this.game = game;
    }

    public void Draw(Texture texture)
    {
        game.batch.draw(texture, player.x, player.y, player.width, player.height);
    }

    public Boolean ApplyGravity(Float speed, Float topStage, Float bottomStage)
    {
        player.y += speed;
        if (player.y < bottomStage)
        {
            player.y = bottomStage;
            return false;
        }
        if (player.y > topStage)
        {
            player.y = topStage;
            return false;
        }
        return true;
    }

    public Boolean CheckCollision(Rectangle target)
    {
        return player.overlaps(target);
    }

    public Float GetX() {
        return player.x;
    }

    public Float GetY() {
        return player.y;
    }

    public Float GetWidth() {
        return player.width;
    }

    public Float GetHeight() {
        return player.height;
    }

    public void SetX(Float x) {
        player.x = x;
    }

    public void SetY(Float y) {
        player.y = y;
    }

    public void SetWidth(Float width) {
        player.width = width;
    }

    public void SetHeight(Float height) {
        player.height = height;
    }
}
