/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mario.core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;
import mario.Game;
import mario.Main;
import mario.State;

/**
 *
 * @author danny
 */
public abstract class GameObject
{
    protected BufferedImage sprite;
    protected BufferedImage spritePart;
    protected int x, y, width, height;
    protected HashMap<String, Rectangle> frames = new HashMap<String, Rectangle>();
    protected String[] animation;
    protected int animationFrame = 0;
    protected State state;
    protected int frameSpeed = 50;
    protected long systemTime = System.currentTimeMillis();
    protected Game game;
    private static ArrayList<GameObject> collection = new ArrayList<GameObject>();
    private int x_last, y_last, width_last, height_last;
    protected Collision mapCollision = Collision.NONE;

    public GameObject(Game game, int x, int y, int width, int height, String fileName)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        sprite = loadImage(fileName);
        boolean add = collection.add(this);
        this.game = game;
    }

    private BufferedImage loadImage(String fileName)
    {
        URL imageUrl = Main.class.getResource(fileName);
        try
        {
            sprite = ImageIO.read(imageUrl);
        } catch (IOException e)
        {
            System.out.println("Error int GameObject/loadImage"
                    + "Error:");
            System.out.println(e);
        }
        return sprite;
    }

    public BufferedImage getSprite()
    {
        return sprite;
    }

    protected BufferedImage getImage()
    {
        //System.out.println((System.currentTimeMillis() - systemTime));
        if ((System.currentTimeMillis() - systemTime) > frameSpeed)
        {

            systemTime = System.currentTimeMillis();
            //System.out.println(animationFrame + " : " + animation.length);
            if (animationFrame == animation.length)
            {
                animationFrame = 0;
            }
            BufferedImage crop = sprite.getSubimage((int) frames.get(animation[animationFrame]).getX(),
                    (int) frames.get(animation[animationFrame]).getY(),
                    (int) frames.get(animation[animationFrame]).getWidth(),
                    (int) frames.get(animation[animationFrame]).getHeight());
            animationFrame++;
            spritePart = crop;

        }
        return spritePart;
    }

    public void draw(Graphics graphics)
    {
        graphics.setColor(Color.pink);
        graphics.fillRect(x, y, width, height);
        graphics.drawImage(getImage(), x, y, null);
    }

    public abstract void doLoopAction();

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getXLast()
    {
        return x_last;
    }

    public int getYLast()
    {
        return y_last;
    }

    public int getWidthLast()
    {
        return width_last;
    }

    public int getHeightLast()
    {
        return height_last;
    }

    public void setX(int x)
    {
        if (checkCollisionMap(x, y) == Collision.NONE)
        {
            x_last = this.x;
            this.x = x;
        }
    }

    public void setY(int y)
    {
        if (checkCollisionMap(x, y) == Collision.NONE)
        {
            y_last = this.y;
            System.out.println(y_last + " : " + this.y + " : " + y);
            this.y = y;
            System.out.println(y_last + " : " + this.y + " : " + y);
        }
    }

    public int getHeight()
    {
        return height;
    }

    public int getWidth()
    {
        return width;
    }

    public void setHeight(int height)
    {
        height_last = this.height;
        this.height = height;
    }

    public void setWidth(int width)
    {
        width_last = this.width;
        this.width = width;
    }

    public void setAnimation(String[] animation)
    {
        this.animation = animation;
        animationFrame = 0;
        //systemTime = System.currentTimeMillis() - frameSpeed;
    }

    public String[] getAnimation()
    {
        return animation;
    }

    public int getFrameSpeed()
    {
        return frameSpeed;
    }

    public void setState(State state)
    {
        this.state = state;

    }

    public Collision checkCollisionMap()
    {
        return checkCollisionMap(x, y, 2);
    }

    public Collision checkCollisionMap(int x, int y)
    {
        return checkCollisionMap(x, y, 1);
    }



    public Collision checkCollisionMap(int x, int y, int downSize)
    {
        Polygon mapPolygon = game.getBackground().getPolygon();

        Rectangle objectRectangle = new Rectangle(x, y, width, height + downSize);

        mapCollision = Collision.NONE;

        if(mapPolygon.intersects(objectRectangle))
        {
            mapCollision = Collision.COLLISION;
        }

        for (MapObject characterObjectLoop : game.getMapObjects())
        {
            if (this != characterObjectLoop)
            {
                if (!(characterObjectLoop instanceof NoClip))
                {

                    Rectangle mapObjectRectangle = new Rectangle(characterObjectLoop.getX(), characterObjectLoop.getY(), characterObjectLoop.getWidth(), characterObjectLoop.getHeight());
                    if(mapObjectRectangle.intersects(objectRectangle))
                    {
                        mapCollision = Collision.COLLISION;
                    }
                }
            }
        }
        return mapCollision;
    }
}