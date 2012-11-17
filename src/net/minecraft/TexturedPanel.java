/*    */ package net.minecraft;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.GradientPaint;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Graphics2D;
/*    */ import java.awt.Image;
import java.awt.geom.Point2D;
/*    */ import java.awt.geom.Point2D.Float;
/*    */ import java.awt.image.BufferedImage;
/*    */ import java.io.IOException;
/*    */ import javax.imageio.ImageIO;
/*    */ import javax.swing.JPanel;
/*    */ 
/*    */ public class TexturedPanel extends JPanel
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private Image img;
/*    */   private Image bgImage;
/*    */ 
/*    */   public TexturedPanel()
/*    */   {
/* 21 */     setOpaque(true);
/*    */     try
/*    */     {
/* 24 */       this.bgImage = ImageIO.read(LoginForm.class.getResource("dirt.png")).getScaledInstance(32, 32, 16);
/*    */     } catch (IOException localIOException) {
/* 26 */       localIOException.printStackTrace();
/*    */     }
/*    */   }
/*    */ 
/*    */   public void update(Graphics paramGraphics)
/*    */   {
/* 32 */     paint(paramGraphics);
/*    */   }
/*    */ 
/*    */   public void paintComponent(Graphics paramGraphics)
/*    */   {
/* 37 */     int i = getWidth() / 2 + 1;
/* 38 */     int j = getHeight() / 2 + 1;
/* 39 */     if ((this.img == null) || (this.img.getWidth(null) != i) || (this.img.getHeight(null) != j)) {
/* 40 */       this.img = createImage(i, j);
/*    */ 
/* 42 */       Graphics localGraphics = this.img.getGraphics();
/*    */       int m;
/* 43 */       for (int k = 0; k <= i / 32; k++) {
/* 44 */         for (m = 0; m <= j / 32; m++)
/* 45 */           localGraphics.drawImage(this.bgImage, k * 32, m * 32, null);
/*    */       }
/* 47 */       if ((localGraphics instanceof Graphics2D)) {
/* 48 */         Graphics2D localGraphics2D = (Graphics2D)localGraphics;
/* 49 */         m = 1;
/* 50 */         localGraphics2D.setPaint(new GradientPaint(new Point2D.Float(0.0F, 0.0F), new Color(553648127, true), new Point2D.Float(0.0F, m), new Color(0, true)));
/* 51 */         localGraphics2D.fillRect(0, 0, i, m);
/*    */ 
/* 53 */         m = j;
/* 54 */         localGraphics2D.setPaint(new GradientPaint(new Point2D.Float(0.0F, 0.0F), new Color(0, true), new Point2D.Float(0.0F, m), new Color(1610612736, true)));
/* 55 */         localGraphics2D.fillRect(0, 0, i, m);
/*    */       }
/* 57 */       localGraphics.dispose();
/*    */     }
/* 59 */     paramGraphics.drawImage(this.img, 0, 0, i * 2, j * 2, null);
/*    */   }
/*    */ }