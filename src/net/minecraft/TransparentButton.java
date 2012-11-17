/*    */ package net.minecraft;
/*    */ 
/*    */ import javax.swing.JButton;
/*    */ 
/*    */ public class TransparentButton extends JButton
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */ 
/*    */   public TransparentButton(String paramString)
/*    */   {
/*  9 */     super(paramString);
/*    */   }
/*    */ 
/*    */   public boolean isOpaque()
/*    */   {
/* 14 */     return false;
/*    */   }
/*    */ }