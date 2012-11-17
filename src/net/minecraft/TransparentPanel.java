/*    */ package net.minecraft;
/*    */ 
/*    */ import java.awt.Insets;
/*    */ import java.awt.LayoutManager;
/*    */ import javax.swing.JPanel;
/*    */ 
/*    */ public class TransparentPanel extends JPanel
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private Insets insets;
/*    */ 
/*    */   public TransparentPanel()
/*    */   {
/*    */   }
/*    */ 
/*    */   public TransparentPanel(LayoutManager paramLayoutManager)
/*    */   {
/* 16 */     setLayout(paramLayoutManager);
/*    */   }
/*    */ 
/*    */   public boolean isOpaque()
/*    */   {
/* 21 */     return false;
/*    */   }
/*    */ 
/*    */   public void setInsets(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 25 */     this.insets = new Insets(paramInt1, paramInt2, paramInt3, paramInt4);
/*    */   }
/*    */ 
/*    */   public Insets getInsets()
/*    */   {
/* 30 */     if (this.insets == null) return super.getInsets();
/* 31 */     return this.insets;
/*    */   }
/*    */ }