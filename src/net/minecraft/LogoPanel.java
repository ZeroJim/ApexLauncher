/*    */ package net.minecraft;
/*    */ 
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Image;
/*    */ import java.awt.image.BufferedImage;
/*    */ import java.io.IOException;
/*    */ import javax.imageio.ImageIO;
/*    */ import javax.swing.JPanel;
/*    */ 
/*    */ public class LogoPanel extends JPanel
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private Image bgImage;
/*    */ 
/*    */   public LogoPanel()
/*    */   {
/* 18 */     setOpaque(true);
/*    */     try
/*    */     {
/* 21 */       BufferedImage localBufferedImage = ImageIO.read(LoginForm.class.getResource("apexlogo.png"));
/* 22 */       int i = localBufferedImage.getWidth();
/* 23 */       int j = localBufferedImage.getHeight();
/* 24 */       this.bgImage = localBufferedImage.getScaledInstance(i, j, 16);
/* 25 */       setPreferredSize(new Dimension(i + 32, j + 32));
/*    */     } catch (IOException localIOException) {
/* 27 */       localIOException.printStackTrace();
/*    */     }
/*    */   }
/*    */ 
/*    */   public void update(Graphics paramGraphics)
/*    */   {
/* 33 */     paint(paramGraphics);
/*    */   }
/*    */ 
/*    */   public void paintComponent(Graphics paramGraphics)
/*    */   {
/* 38 */     paramGraphics.drawImage(this.bgImage, 24, 24, null);
/*    */   }
/*    */ }