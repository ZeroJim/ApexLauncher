/*     */ package net.minecraft;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Cursor;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.GridLayout;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.net.URL;
/*     */ import java.util.Random;
/*     */ import javax.crypto.Cipher;
/*     */ import javax.crypto.CipherInputStream;
/*     */ import javax.crypto.CipherOutputStream;
/*     */ import javax.crypto.SecretKey;
/*     */ import javax.crypto.SecretKeyFactory;
/*     */ import javax.crypto.spec.PBEKeySpec;
/*     */ import javax.crypto.spec.PBEParameterSpec;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JPasswordField;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.JTextPane;
/*     */ import javax.swing.border.MatteBorder;
/*     */ import javax.swing.event.HyperlinkEvent;
/*     */ import javax.swing.event.HyperlinkEvent.EventType;
/*     */ import javax.swing.event.HyperlinkListener;
/*     */ 
/*     */ public class LoginForm extends TransparentPanel
/*     */ {
/*  17 */   private static final HyperlinkListener EXTERNAL_HYPERLINK_LISTENER = new HyperlinkListener() {
/*     */     public void hyperlinkUpdate(HyperlinkEvent paramHyperlinkEvent) {
/*  19 */       if (paramHyperlinkEvent.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
/*     */         try {
/*  21 */           Util.openLink(paramHyperlinkEvent.getURL().toURI());
/*     */         } catch (Exception localException) {
/*  23 */           localException.printStackTrace();
/*     */         }
/*     */     }
/*  17 */   };
/*     */   private static final int PANEL_SIZE = 100;
/*     */   private static final long serialVersionUID = 1L;
/*  31 */   private static final Color LINK_COLOR = new Color(8421631);
/*     */ 
/*  33 */   public JTextField userName = new JTextField(20);
/*  34 */   public JPasswordField password = new JPasswordField(20);
/*     */   private JScrollPane scrollPane;
/*  36 */   private TransparentCheckbox rememberBox = new TransparentCheckbox("Remember password");
/*  37 */   private TransparentButton launchButton = new TransparentButton("Login");
/*  38 */   private TransparentButton optionsButton = new TransparentButton("Options");
/*  39 */   private TransparentButton retryButton = new TransparentButton("Try again");
/*  40 */   private TransparentButton offlineButton = new TransparentButton("Play offline");
/*  41 */   private TransparentLabel errorLabel = new TransparentLabel("", 0);
/*     */   private LauncherFrame launcherFrame;
/*  43 */   private boolean outdated = false;
/*     */ 
/*  45 */   private boolean playOfflineAsDemo = false;
/*     */ 
/*     */   public LoginForm(LauncherFrame paramLauncherFrame) {
/*  48 */     this.launcherFrame = paramLauncherFrame;
/*     */ 
/*  50 */     BorderLayout localBorderLayout = new BorderLayout();
/*  51 */     setLayout(localBorderLayout);
/*     */ 
/*  53 */     add(buildMainLoginPanel(), "Center");
/*     */ 
/*  55 */     readUsername();
/*     */ 
/*  57 */     ActionListener local2 = new ActionListener() {
/*     */       public void actionPerformed(ActionEvent paramActionEvent) {
/*  59 */         LoginForm.this.doLogin();
/*     */       }
/*     */     };
/*  63 */     this.userName.addActionListener(local2);
/*  64 */     this.password.addActionListener(local2);
/*     */ 
/*  66 */     this.retryButton.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent paramActionEvent) {
/*  68 */         LoginForm.this.errorLabel.setText("");
/*  69 */         LoginForm.this.removeAll();
/*  70 */         LoginForm.this.add(LoginForm.this.buildMainLoginPanel(), "Center");
/*  71 */         LoginForm.this.validate();
/*     */       }
/*     */     });
/*  75 */     this.offlineButton.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent paramActionEvent) {
/*  77 */         launcherFrame.playCached(LoginForm.this.userName.getText(), LoginForm.this.playOfflineAsDemo);
/*     */       }
/*     */     });
/*  81 */     this.launchButton.addActionListener(local2);
/*     */ 
/*  83 */     this.optionsButton.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent paramActionEvent) {
/*  85 */         new OptionsPanel(launcherFrame).setVisible(true);
/*     */       } } );
/*     */   }
/*     */ 
/*     */   public void doLogin() {
/*  91 */     setLoggingIn();
/*  92 */     new Thread()
/*     */     {
/*     */       public void run() {
/*     */         try {
/*  96 */           LoginForm.this.launcherFrame.login(LoginForm.this.userName.getText(), new String(LoginForm.this.password.getPassword()));
/*     */         } catch (Exception localException) {
/*  98 */           LoginForm.this.setError(localException.toString());
/*     */         }
/*     */       }
/*     */     }
/*  92 */     .start();
/*     */   }
/*     */ 
/*     */   private void readUsername()
/*     */   {
/*     */     try
/*     */     {
/* 106 */       File localFile = new File(Util.getWorkingDirectory(), "lastlogin");
/*     */ 
/* 108 */       Cipher localCipher = getCipher(2, "passwordfile");
/*     */       DataInputStream localDataInputStream;
/* 109 */       if (localCipher != null)
/* 110 */         localDataInputStream = new DataInputStream(new CipherInputStream(new FileInputStream(localFile), localCipher));
/*     */       else {
/* 112 */         localDataInputStream = new DataInputStream(new FileInputStream(localFile));
/*     */       }
/* 114 */       this.userName.setText(localDataInputStream.readUTF());
/* 115 */       this.password.setText(localDataInputStream.readUTF());
/* 116 */       this.rememberBox.setSelected(this.password.getPassword().length > 0);
/* 117 */       localDataInputStream.close();
/*     */     } catch (Exception localException) {
/* 119 */       localException.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeUsername() {
/*     */     try {
/* 125 */       File localFile = new File(Util.getWorkingDirectory(), "lastlogin");
/*     */ 
/* 127 */       Cipher localCipher = getCipher(1, "passwordfile");
/*     */       DataOutputStream localDataOutputStream;
/* 128 */       if (localCipher != null)
/* 129 */         localDataOutputStream = new DataOutputStream(new CipherOutputStream(new FileOutputStream(localFile), localCipher));
/*     */       else {
/* 131 */         localDataOutputStream = new DataOutputStream(new FileOutputStream(localFile));
/*     */       }
/* 133 */       localDataOutputStream.writeUTF(this.userName.getText());
/* 134 */       localDataOutputStream.writeUTF(this.rememberBox.isSelected() ? new String(this.password.getPassword()) : "");
/* 135 */       localDataOutputStream.close();
/*     */     } catch (Exception localException) {
/* 137 */       localException.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private Cipher getCipher(int paramInt, String paramString) throws Exception {
/* 142 */     Random localRandom = new Random(43287234L);
/* 143 */     byte[] arrayOfByte = new byte[8];
/* 144 */     localRandom.nextBytes(arrayOfByte);
/* 145 */     PBEParameterSpec localPBEParameterSpec = new PBEParameterSpec(arrayOfByte, 5);
/*     */ 
/* 147 */     SecretKey localSecretKey = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(new PBEKeySpec(paramString.toCharArray()));
/* 148 */     Cipher localCipher = Cipher.getInstance("PBEWithMD5AndDES");
/* 149 */     localCipher.init(paramInt, localSecretKey, localPBEParameterSpec);
/* 150 */     return localCipher;
/*     */   }
/*     */ 
/*     */   private JScrollPane getUpdateNews() {
/* 154 */     if (this.scrollPane != null) return this.scrollPane;
/*     */     try
/*     */     {
/* 157 */       final JTextPane local7 = new JTextPane()
/*     */       {
/*     */         private static final long serialVersionUID = 1L;
/*     */       };
/* 161 */       local7.setEditable(false);
/* 162 */       local7.setMargin(null);
/* 163 */       local7.setBackground(Color.DARK_GRAY);
/* 164 */       local7.setContentType("text/html");
/* 165 */       local7.setText("<html><body><font color=\"#808080\"><br><br><br><br><br><br><br><center><h1>Loading update news..</h1></center></font></body></html>");
/* 166 */       local7.addHyperlinkListener(EXTERNAL_HYPERLINK_LISTENER);
/*     */ 
/* 168 */       new Thread()
/*     */       {
/*     */         public void run() {
/*     */           try {
/* 172 */             local7.setPage(new URL("http://niflheimcraft.tumblr.com/"));
/*     */           } catch (Exception localException) {
/* 174 */             localException.printStackTrace();
/* 175 */             local7.setText("<html><body><font color=\"#808080\"><br><br><br><br><br><br><br><center><h1>Failed to update news</h1><br>" + localException.toString() + "</center></font></body></html>");
/*     */           }
/*     */         }
/*     */       }
/* 168 */       .start();
/*     */ 
/* 181 */       this.scrollPane = new JScrollPane(local7);
/* 182 */       this.scrollPane.setBorder(null);
/*     */ 
/* 184 */       this.scrollPane.setBorder(new MatteBorder(0, 0, 2, 0, Color.BLACK));
/*     */     } catch (Exception localException) {
/* 186 */       localException.printStackTrace();
/*     */     }
/*     */ 
/* 189 */     return this.scrollPane;
/*     */   }
/*     */ 
/*     */   private JPanel buildMainLoginPanel() {
/* 193 */     TransparentPanel localTransparentPanel = new TransparentPanel(new BorderLayout());
/* 194 */     localTransparentPanel.add(getUpdateNews(), "Center");
/*     */ 
/* 196 */     TexturedPanel localTexturedPanel = new TexturedPanel();
/* 197 */     localTexturedPanel.setLayout(new BorderLayout());
/* 198 */     localTexturedPanel.add(new LogoPanel(), "West");
/* 199 */     localTexturedPanel.add(new TransparentPanel(), "Center");
/* 200 */     localTexturedPanel.add(center(buildLoginPanel()), "East");
/* 201 */     localTexturedPanel.setPreferredSize(new Dimension(100, 100));
/*     */ 
/* 203 */     localTransparentPanel.add(localTexturedPanel, "South");
/* 204 */     return localTransparentPanel;
/*     */   }
/*     */ 
/*     */   private JPanel buildLoginPanel() {
/* 208 */     TransparentPanel localTransparentPanel1 = new TransparentPanel();
/* 209 */     localTransparentPanel1.setInsets(4, 0, 4, 0);
/*     */ 
/* 211 */     BorderLayout localBorderLayout = new BorderLayout();
/* 212 */     localBorderLayout.setHgap(0);
/* 213 */     localBorderLayout.setVgap(8);
/* 214 */     localTransparentPanel1.setLayout(localBorderLayout);
/*     */ 
/* 216 */     GridLayout localGridLayout1 = new GridLayout(0, 1);
/* 217 */     localGridLayout1.setVgap(2);
/* 218 */     GridLayout localGridLayout2 = new GridLayout(0, 1);
/* 219 */     localGridLayout2.setVgap(2);
/* 220 */     GridLayout localGridLayout3 = new GridLayout(0, 1);
/* 221 */     localGridLayout3.setVgap(2);
/*     */ 
/* 223 */     TransparentPanel localTransparentPanel2 = new TransparentPanel(localGridLayout1);
/* 224 */     TransparentPanel localTransparentPanel3 = new TransparentPanel(localGridLayout2);
/*     */ 
/* 226 */     localTransparentPanel2.add(new TransparentLabel("Username:", 4));
/* 227 */     localTransparentPanel2.add(new TransparentLabel("Password:", 4));
/* 228 */     localTransparentPanel2.add(new TransparentLabel("", 4));
/*     */ 
/* 230 */     localTransparentPanel3.add(this.userName);
/* 231 */     localTransparentPanel3.add(this.password);
/* 232 */     localTransparentPanel3.add(this.rememberBox);
/*     */ 
/* 234 */     localTransparentPanel1.add(localTransparentPanel2, "West");
/* 235 */     localTransparentPanel1.add(localTransparentPanel3, "Center");
/*     */ 
/* 237 */     TransparentPanel localTransparentPanel4 = new TransparentPanel(new BorderLayout());
/*     */ 
/* 239 */     TransparentPanel localTransparentPanel5 = new TransparentPanel(localGridLayout3);
/* 240 */     localTransparentPanel2.setInsets(0, 0, 0, 4);
/* 241 */     localTransparentPanel5.setInsets(0, 10, 0, 10);
/*     */ 
/* 243 */     localTransparentPanel5.add(this.optionsButton);
/* 244 */     localTransparentPanel5.add(this.launchButton);
/*     */     try
/*     */     {
/*     */       Object localObject;
/* 246 */       if (this.outdated) {
/* 247 */         localObject = getUpdateLink();
/* 248 */         localTransparentPanel5.add((Component)localObject);
/*     */       } else {
/* 250 */         localObject = new TransparentLabel("Need account?") {
/*     */           private static final long serialVersionUID = 0L;
/*     */ 
/*     */           public void paint(Graphics paramGraphics) {
/* 255 */             super.paint(paramGraphics);
/*     */ 
/* 257 */             int i = 0;
/* 258 */             int j = 0;
/*     */ 
/* 262 */             FontMetrics localFontMetrics = paramGraphics.getFontMetrics();
/* 263 */             int k = localFontMetrics.stringWidth(getText());
/* 264 */             int m = localFontMetrics.getHeight();
/*     */ 
/* 266 */             if (getAlignmentX() == 2.0F) i = 0;
/* 267 */             else if (getAlignmentX() == 0.0F) i = getBounds().width / 2 - k / 2;
/* 268 */             else if (getAlignmentX() == 4.0F) i = getBounds().width - k;
/* 269 */             j = getBounds().height / 2 + m / 2 - 1;
/*     */ 
/* 271 */             paramGraphics.drawLine(i + 2, j, i + k - 2, j);
/*     */           }
/*     */ 
/*     */           public void update(Graphics paramGraphics)
/*     */           {
/* 276 */             paint(paramGraphics);
/*     */           }
/*     */         };
/* 280 */         ((TransparentLabel)localObject).setCursor(Cursor.getPredefinedCursor(12));
/* 281 */         ((TransparentLabel)localObject).addMouseListener(new MouseAdapter()
/*     */         {
/*     */           public void mousePressed(MouseEvent paramMouseEvent) {
/*     */             try {
/* 285 */               Util.openLink(new URL("http://www.tunacraft.com/login/register.php?sn=NiflHeimCraft").toURI());
/*     */             } catch (Exception localException) {
/* 287 */               localException.printStackTrace();
/*     */             }
/*     */           }
/*     */         });
/* 291 */         ((TransparentLabel)localObject).setForeground(LINK_COLOR);
/* 292 */         localTransparentPanel5.add((Component)localObject);
/*     */       }
/*     */     }
/*     */     catch (Error localError) {
/*     */     }
/* 297 */     localTransparentPanel4.add(localTransparentPanel5, "Center");
/* 298 */     localTransparentPanel1.add(localTransparentPanel4, "East");
/*     */ 
/* 300 */     this.errorLabel.setFont(new Font(null, 2, 16));
/* 301 */     this.errorLabel.setForeground(new Color(16728128));
/* 302 */     this.errorLabel.setText("");
/* 303 */     localTransparentPanel1.add(this.errorLabel, "North");
/*     */ 
/* 305 */     return (JPanel)localTransparentPanel1;
/*     */   }
/*     */ 
/*     */   private TransparentLabel getUpdateLink() {
/* 309 */     TransparentLabel local11 = new TransparentLabel("You need to update the launcher!") {
/*     */       private static final long serialVersionUID = 0L;
/*     */ 
/*     */       public void paint(Graphics paramGraphics) {
/* 314 */         super.paint(paramGraphics);
/*     */ 
/* 316 */         int i = 0;
/* 317 */         int j = 0;
/*     */ 
/* 321 */         FontMetrics localFontMetrics = paramGraphics.getFontMetrics();
/* 322 */         int k = localFontMetrics.stringWidth(getText());
/* 323 */         int m = localFontMetrics.getHeight();
/*     */ 
/* 325 */         if (getAlignmentX() == 2.0F) i = 0;
/* 326 */         else if (getAlignmentX() == 0.0F) i = getBounds().width / 2 - k / 2;
/* 327 */         else if (getAlignmentX() == 4.0F) i = getBounds().width - k;
/* 328 */         j = getBounds().height / 2 + m / 2 - 1;
/*     */ 
/* 330 */         paramGraphics.drawLine(i + 2, j, i + k - 2, j);
/*     */       }
/*     */ 
/*     */       public void update(Graphics paramGraphics)
/*     */       {
/* 335 */         paint(paramGraphics);
/*     */       }
/*     */     };
/* 339 */     local11.setCursor(Cursor.getPredefinedCursor(12));
/* 340 */     local11.addMouseListener(new MouseAdapter()
/*     */     {
/*     */       public void mousePressed(MouseEvent paramMouseEvent) {
/*     */         try {
/* 344 */           Util.openLink(new URL("http://www.tunacraft.com/download/").toURI());
/*     */         } catch (Exception localException) {
/* 346 */           localException.printStackTrace();
/*     */         }
/*     */       }
/*     */     });
/* 350 */     local11.setForeground(LINK_COLOR);
/* 351 */     return local11;
/*     */   }
/*     */ 
/*     */   private JPanel buildMainOfflinePanel(boolean paramBoolean) {
/* 355 */     TransparentPanel localTransparentPanel = new TransparentPanel(new BorderLayout());
/* 356 */     localTransparentPanel.add(getUpdateNews(), "Center");
/*     */ 
/* 358 */     TexturedPanel localTexturedPanel = new TexturedPanel();
/* 359 */     localTexturedPanel.setLayout(new BorderLayout());
/* 360 */     localTexturedPanel.add(new LogoPanel(), "West");
/* 361 */     localTexturedPanel.add(new TransparentPanel(), "Center");
/* 362 */     localTexturedPanel.add(center(buildOfflinePanel(paramBoolean)), "East");
/* 363 */     localTexturedPanel.setPreferredSize(new Dimension(100, 100));
/*     */ 
/* 365 */     localTransparentPanel.add(localTexturedPanel, "South");
/* 366 */     return localTransparentPanel;
/*     */   }
/*     */ 
/*     */   private Component center(Component paramComponent) {
/* 370 */     TransparentPanel localTransparentPanel = new TransparentPanel(new GridBagLayout());
/* 371 */     localTransparentPanel.add(paramComponent);
/* 372 */     return localTransparentPanel;
/*     */   }
/*     */ 
/*     */   private TransparentPanel buildOfflinePanel(boolean paramBoolean) {
/* 376 */     TransparentPanel localTransparentPanel1 = new TransparentPanel();
/* 377 */     localTransparentPanel1.setInsets(0, 0, 0, 20);
/*     */ 
/* 379 */     BorderLayout localBorderLayout = new BorderLayout();
/* 380 */     localTransparentPanel1.setLayout(localBorderLayout);
/*     */ 
/* 382 */     TransparentPanel localTransparentPanel2 = new TransparentPanel(new BorderLayout());
/*     */ 
/* 384 */     GridLayout localGridLayout = new GridLayout(0, 1);
/* 385 */     localGridLayout.setVgap(2);
/* 386 */     TransparentPanel localTransparentPanel3 = new TransparentPanel(localGridLayout);
/* 387 */     localTransparentPanel3.setInsets(0, 8, 0, 0);
/*     */ 
/* 389 */     if (paramBoolean)
/* 390 */       this.offlineButton.setText("Play Demo");
/*     */     else {
/* 392 */       this.offlineButton.setText("Play Offline");
/*     */     }
/*     */ 
/* 395 */     localTransparentPanel3.add(this.retryButton);
/* 396 */     localTransparentPanel3.add(this.offlineButton);
/*     */ 
/* 398 */     localTransparentPanel2.add(localTransparentPanel3, "East");
/*     */ 
/* 400 */     boolean bool = (this.launcherFrame.canPlayOffline(this.userName.getText())) || (paramBoolean);
/* 401 */     this.offlineButton.setEnabled(bool);
/* 402 */     if (!bool) {
/* 403 */       localTransparentPanel2.add(new TransparentLabel("(Not downloaded)", 4), "South");
/*     */     }
/* 405 */     localTransparentPanel1.add(localTransparentPanel2, "Center");
/*     */ 
/* 407 */     TransparentPanel localTransparentPanel4 = new TransparentPanel(new GridLayout(0, 1));
/* 408 */     this.errorLabel.setFont(new Font(null, 2, 16));
/* 409 */     this.errorLabel.setForeground(new Color(16728128));
/* 410 */     localTransparentPanel4.add(this.errorLabel);
/* 411 */     if (this.outdated) {
/* 412 */       TransparentLabel localTransparentLabel = getUpdateLink();
/* 413 */       localTransparentPanel4.add(localTransparentLabel);
/*     */     }
/*     */ 
/* 416 */     localTransparentPanel2.add(localTransparentPanel4, "Center");
/*     */ 
/* 418 */     return localTransparentPanel1;
/*     */   }
/*     */ 
/*     */   public void setError(String paramString) {
/* 422 */     removeAll();
/* 423 */     add(buildMainLoginPanel(), "Center");
/* 424 */     this.errorLabel.setText(paramString);
/* 425 */     validate();
/*     */   }
/*     */ 
/*     */   public void loginOk() {
/* 429 */     writeUsername();
/*     */   }
/*     */ 
/*     */   public void setLoggingIn() {
/* 433 */     removeAll();
/* 434 */     JPanel localJPanel = new JPanel(new BorderLayout());
/* 435 */     localJPanel.add(getUpdateNews(), "Center");
/*     */ 
/* 437 */     TexturedPanel localTexturedPanel = new TexturedPanel();
/* 438 */     localTexturedPanel.setLayout(new BorderLayout());
/* 439 */     localTexturedPanel.add(new LogoPanel(), "West");
/* 440 */     localTexturedPanel.add(new TransparentPanel(), "Center");
/* 441 */     TransparentLabel localTransparentLabel = new TransparentLabel("Logging in...                      ", 0);
/* 442 */     localTransparentLabel.setFont(new Font(null, 1, 16));
/* 443 */     localTexturedPanel.add(center(localTransparentLabel), "East");
/* 444 */     localTexturedPanel.setPreferredSize(new Dimension(100, 100));
/*     */ 
/* 446 */     localJPanel.add(localTexturedPanel, "South");
/*     */ 
/* 448 */     add(localJPanel, "Center");
/* 449 */     validate();
/*     */   }
/*     */ 
/*     */   public void setNoNetwork(boolean paramBoolean) {
/* 453 */     this.playOfflineAsDemo = paramBoolean;
/*     */ 
/* 455 */     removeAll();
/* 456 */     add(buildMainOfflinePanel(paramBoolean), "Center");
/* 457 */     validate();
/*     */   }
/*     */ 
/*     */   public void setOutdated() {
/* 461 */     this.outdated = true;
/*     */   }
/*     */ }