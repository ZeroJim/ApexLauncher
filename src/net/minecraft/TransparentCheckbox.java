/*    */ package net.minecraft;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import javax.swing.JCheckBox;
/*    */ 
/*    */ public class TransparentCheckbox extends JCheckBox
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */ 
/*    */   public TransparentCheckbox(String paramString)
/*    */   {
/* 11 */     super(paramString);
/* 12 */     setForeground(Color.WHITE);
/*    */   }
/*    */ 
/*    */   public boolean isOpaque()
/*    */   {
/* 17 */     return false;
/*    */   }
/*    */ }