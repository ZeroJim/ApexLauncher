/*    */ package net.minecraft;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import javax.swing.JLabel;
/*    */ 
/*    */ public class TransparentLabel extends JLabel
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */ 
/*    */   public TransparentLabel(String paramString, int paramInt)
/*    */   {
/* 11 */     super(paramString, paramInt);
/* 12 */     setForeground(Color.WHITE);
/*    */   }
/*    */ 
/*    */   public TransparentLabel(String paramString) {
/* 16 */     super(paramString);
/* 17 */     setForeground(Color.WHITE);
/*    */   }
/*    */ 
/*    */   public boolean isOpaque()
/*    */   {
/* 22 */     return false;
/*    */   }
/*    */ }