package com.example.commonutils.utils;

import org.springframework.stereotype.Component;
import org.springframework.util.FastByteArrayOutputStream;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import java.util.UUID;

/**
 * code生成工具类
 */
@Component
public class CodeUtil {
    //生成验证码图片的宽度
    private int width = 100;
    //生成验证码图片的高度
    private int height = 30;
    //字符样式
    private String[] fontNames = {"宋体" , "楷体" , "隶书" , "微软雅黑"};
    //定义验证码图片的背景颜色为白色
    private Color bgColor = new Color(255 , 255 , 255);
    //生成随机数
    private Random random = new Random();
    //定义code字符
    private String codes = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    //记录随机字符串
    private String codeText;
    //获取一个随机颜色
    private Color randomColor(){
        int red = random.nextInt(150);  //生成0-150之间的整数
        int green = random.nextInt(150);
        int blue = random.nextInt(150);
        return new Color(red , green , blue);
    }

    //获取一个随机字体
    private Font randomFont(){
        String name = fontNames[random.nextInt(fontNames.length)];
        int style = random.nextInt(4);
        int size = random.nextInt(5) + 24;
        return new Font(name , style , size);  //new Font(String 字体 , int 风格 , int 字号);
    }

    //获取一个随机字符
    private char randomChar(){
        return codes.charAt(random.nextInt(codes.length()));
    }

    //创建一个空白的BufferedImage对象
    private BufferedImage createImage(){
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        //设置验证码图片的背景颜色
        g2.setColor(bgColor);
        g2.fillRect(0 , 0 , width , height);
        return image;
    }
    //绘制干扰线
    private void drawLine(BufferedImage image){
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        int num = 5;
        for (int i = 0; i < num; i++) {
            int x1 = random.nextInt(width);
            int y1 = random.nextInt(height);
            int x2 = random.nextInt(width);
            int y2 = random.nextInt(height);
            g2.setColor(randomColor());
            g2.setStroke(new BasicStroke(1.5f));  //设置干扰线的宽度
            g2.drawLine(x1 , y1 , x2 , y2);
        }
    }

    public BufferedImage getImage(){
        BufferedImage image = createImage();
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < 4; i++) {
            String s = randomChar() + "";
            stringBuffer.append(s);
            g2.setColor(randomColor());
            g2.setFont(randomFont());
            float x = i * width * 1.0f / 4;
            g2.drawString(s , x , height - 8 );
        }
        this.codeText = stringBuffer.toString();
        drawLine(image);
        return image;
    }

    public String getCodeText(){
        return codeText;
    }
    public static void output(BufferedImage image ,  FastByteArrayOutputStream os) throws IOException {
        ImageIO.write(image , "jpg" , os);
    }
}
