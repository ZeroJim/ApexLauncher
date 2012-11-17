/*     */ package net.minecraft;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Cursor;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Frame;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.GridLayout;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.io.File;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.border.EmptyBorder;
/*     */ 
/*     */ public class OptionsPanel extends JDialog
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */ 
/*     */   public OptionsPanel(Frame paramFrame)
/*     */   {
/*  26 */     super(paramFrame);
/*     */ 
/*  28 */     setModal(true);
/*     */ 
/*  30 */     JPanel localJPanel1 = new JPanel(new BorderLayout());
/*  31 */     JLabel localJLabel = new JLabel("Launcher options", 0);
/*  32 */     localJLabel.setBorder(new EmptyBorder(0, 0, 16, 0));
/*  33 */     localJLabel.setFont(new Font("Default", 1, 16));
/*  34 */     localJPanel1.add(localJLabel, "North");
/*     */ 
/*  36 */     JPanel localJPanel2 = new JPanel(new BorderLayout());
/*  37 */     JPanel localJPanel3 = new JPanel(new GridLayout(0, 1));
/*  38 */     JPanel localJPanel4 = new JPanel(new GridLayout(0, 1));
/*  39 */     localJPanel2.add(localJPanel3, "West");
/*  40 */     localJPanel2.add(localJPanel4, "Center");
/*     */ 
/*  42 */     final JButton localJButton1 = new JButton("Force update!");
/*  43 */     localJButton1.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent paramActionEvent) {
/*  45 */         GameUpdater.forceUpdate = true;
/*  46 */         localJButton1.setText("Will force!");
/*  47 */         localJButton1.setEnabled(false);
/*     */       }
/*     */     });
/*  51 */     localJPanel3.add(new JLabel("Force game update: ", 4));
/*  52 */     localJPanel4.add(localJButton1);
/*     */ 
/*  54 */     localJPanel3.add(new JLabel("Game location on disk: ", 4));
/*  55 */     TransparentLabel local2 = new TransparentLabel(Util.getWorkingDirectory().toString()) {
/*     */       private static final long serialVersionUID = 0L;
/*     */ 
/*     */       public void paint(Graphics paramGraphics) {
/*  60 */         super.paint(paramGraphics);
/*     */ 
/*  62 */         int i = 0;
/*  63 */         int j = 0;
/*     */ 
/*  67 */         FontMetrics localFontMetrics = paramGraphics.getFontMetrics();
/*  68 */         int k = localFontMetrics.stringWidth(getText());
/*  69 */         int m = localFontMetrics.getHeight();
/*     */ 
/*  71 */         if (getAlignmentX() == 2.0F) i = 0;
/*  72 */         else if (getAlignmentX() == 0.0F) i = getBounds().width / 2 - k / 2;
/*  73 */         else if (getAlignmentX() == 4.0F) i = getBounds().width - k;
/*  74 */         j = getBounds().height / 2 + m / 2 - 1;
/*     */ 
/*  76 */         paramGraphics.drawLine(i + 2, j, i + k - 2, j);
/*     */       }
/*     */ 
/*     */       public void update(Graphics paramGraphics)
/*     */       {
/*  81 */         paint(paramGraphics);
/*     */       }
/*     */     };
/*  84 */     local2.setCursor(Cursor.getPredefinedCursor(12));
/*  85 */     local2.addMouseListener(new MouseAdapter()
/*     */     {
/*     */       public void mousePressed(MouseEvent paramMouseEvent) {
/*     */         try {
/*  89 */           Util.openLink(Util.getWorkingDirectory().toURI());
/*     */         } catch (Exception localException) {
/*  91 */           localException.printStackTrace();
/*     */         }
/*     */       }
/*     */     });
/*  95 */     local2.setForeground(new Color(2105599));
/*     */ 
/*  97 */     localJPanel4.add(local2);
/*     */ 
/*  99 */     localJPanel1.add(localJPanel2, "Center");
/*     */ 
/* 101 */     JPanel localJPanel5 = new JPanel(new BorderLayout());
/* 102 */     localJPanel5.add(new JPanel(), "Center");
/* 103 */     JButton localJButton2 = new JButton("Done");
/* 104 */     localJButton2.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent paramActionEvent) {
/* 106 */         OptionsPanel.this.setVisible(false);
/*     */       }
/*     */     });
/* 109 */     localJPanel5.add(localJButton2, "East");
/* 110 */     localJPanel5.setBorder(new EmptyBorder(16, 0, 0, 0));
/*     */ 
/* 112 */     localJPanel1.add(localJPanel5, "South");
/*     */ 
/* 114 */     add(localJPanel1);
/* 115 */     localJPanel1.setBorder(new EmptyBorder(16, 24, 24, 24));
/* 116 */     pack();
/* 117 */     setLocationRelativeTo(paramFrame);
/*     */   }
/*     */ }