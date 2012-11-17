/*     */ package net.minecraft;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Frame;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.imageio.ImageIO;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JPasswordField;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.UIManager;
/*     */ 
/*     */ public class LauncherFrame extends Frame
/*     */ {
/*     */   public static final int VERSION = 13;
/*     */   private static final long serialVersionUID = 1L;
/*  14 */   public Map<String, String> customParameters = new HashMap();
/*     */   public Launcher launcher;
/*     */   public LoginForm loginForm;
/*     */ 
/*     */   public LauncherFrame()
/*     */   {
/*  20 */     super("ApexLauncher");
/*     */ 
/*  22 */     setBackground(Color.BLACK);
/*  23 */     this.loginForm = new LoginForm(this);
/*  24 */     JPanel localJPanel = new JPanel();
/*  25 */     localJPanel.setLayout(new BorderLayout());
/*  26 */     localJPanel.add(this.loginForm, "Center");
/*     */ 
/*  28 */     localJPanel.setPreferredSize(new Dimension(854, 480));
/*     */ 
/*  30 */     setLayout(new BorderLayout());
/*  31 */     add(localJPanel, "Center");
/*     */ 
/*  33 */     pack();
/*  34 */     setLocationRelativeTo(null);
/*     */     try
/*     */     {
/*  37 */       setIconImage(ImageIO.read(LauncherFrame.class.getResource("favicon.png")));
/*     */     } catch (IOException localIOException) {
/*  39 */       localIOException.printStackTrace();
/*     */     }
/*     */ 
/*  42 */     addWindowListener(new WindowAdapter()
/*     */     {
/*     */       public void windowClosing(WindowEvent paramWindowEvent) {
/*  45 */         new Thread()
/*     */         {
/*     */           public void run() {
/*     */             try {
/*  49 */               Thread.sleep(30000L);
/*     */             } catch (InterruptedException localInterruptedException) {
/*  51 */               localInterruptedException.printStackTrace();
/*     */             }
/*  53 */             System.out.println("FORCING EXIT!");
/*  54 */             System.exit(0);
/*     */           }
/*     */         }
/*  45 */         .start();
/*     */ 
/*  58 */         if (LauncherFrame.this.launcher != null) {
/*  59 */           LauncherFrame.this.launcher.stop();
/*  60 */           LauncherFrame.this.launcher.destroy();
/*     */         }
/*  62 */         System.exit(0);
/*     */       } } );
/*     */   }
/*     */ 
/*     */   public void playCached(String paramString, boolean paramBoolean) {
/*     */     try {
/*  69 */       if ((paramString == null) || (paramString.length() <= 0)) {
/*  70 */         paramString = "Player";
/*     */       }
/*  72 */       this.launcher = new Launcher();
/*  73 */       this.launcher.customParameters.putAll(this.customParameters);
/*  74 */       this.launcher.customParameters.put("userName", paramString);
/*  75 */       this.launcher.customParameters.put("demo", "" + paramBoolean);
/*  76 */       this.launcher.customParameters.put("sessionId", "1");
/*  77 */       this.launcher.init();
/*  78 */       removeAll();
/*  79 */       add(this.launcher, "Center");
/*  80 */       validate();
/*  81 */       this.launcher.start();
/*  82 */       this.loginForm = null;
/*  83 */       setTitle("Apex");
/*     */     } catch (Exception localException) {
/*  85 */       localException.printStackTrace();
/*  86 */       showError(localException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void login(String paramString1, String paramString2) {
/*     */     try {
/*  92 */       HashMap localHashMap = new HashMap();
/*  93 */       localHashMap.put("user", paramString1);
/*  94 */       localHashMap.put("password", paramString2);
/*  95 */       localHashMap.put("version", Integer.valueOf(13));
/*  96 */       String str = Util.executePost("http://login.tunacraft.com/", localHashMap);
/*  97 */       if (str == null) {
/*  98 */         showError("Can't connect to Apex servers");
/*  99 */         this.loginForm.setNoNetwork(false);
/* 100 */         return;
/*     */       }
/* 102 */       if (!str.contains(":")) {
/* 103 */         boolean bool = false;
/*     */ 
/* 105 */         if (str.trim().equals("Bad login")) {
/* 106 */           showError("Login failed");
/* 107 */         } else if (str.trim().equals("Old version")) {
/* 108 */           this.loginForm.setOutdated();
/* 109 */           showError("Outdated launcher");
/* 110 */         } else if (str.trim().equals("User not premium")) {
/* 111 */           showError(str);
/* 112 */           bool = true;
/*     */         } else {
/* 114 */           showError(str);
/*     */         }
/* 116 */         this.loginForm.setNoNetwork(bool);
/* 117 */         return;
/*     */       }
/* 119 */       String[] arrayOfString = str.split(":");
/*     */ 
/* 121 */       this.launcher = new Launcher();
/* 122 */       this.launcher.customParameters.putAll(this.customParameters);
/* 123 */       this.launcher.customParameters.put("userName", paramString1);
/* 124 */       this.launcher.customParameters.put("latestVersion", arrayOfString[0].trim());
/* 125 */       this.launcher.customParameters.put("downloadTicket", arrayOfString[1].trim());
/* 126 */       this.launcher.customParameters.put("sessionId", arrayOfString[3].trim());
/* 127 */       this.launcher.init();
/*     */ 
/* 129 */       removeAll();
/* 130 */       add(this.launcher, "Center");
/* 131 */       validate();
/* 132 */       this.launcher.start();
/* 133 */       this.loginForm.loginOk();
/* 134 */       this.loginForm = null;
/* 135 */       setTitle("Apex");
/*     */     } catch (Exception localException) {
/* 137 */       localException.printStackTrace();
/* 138 */       showError(localException.toString());
/* 139 */       this.loginForm.setNoNetwork(false);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void showError(String paramString) {
/* 144 */     removeAll();
/* 145 */     add(this.loginForm);
/* 146 */     this.loginForm.setError(paramString);
/* 147 */     validate();
/*     */   }
/*     */ 
/*     */   public boolean canPlayOffline(String paramString) {
/* 151 */     Launcher localLauncher = new Launcher();
/* 152 */     localLauncher.customParameters.putAll(this.customParameters);
/* 153 */     localLauncher.init(paramString, null, null, "1");
/* 154 */     return localLauncher.canPlayOffline();
/*     */   }
/*     */ 
/*     */   public static void main(String[] paramArrayOfString) {
/*     */     try {
/* 159 */       UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
/*     */     }
/*     */     catch (Exception localException) {
/*     */     }
/* 163 */     
/* 164 */     System.setProperty("java.net.preferIPv4Stack", "true");
/* 165 */     System.setProperty("java.net.preferIPv6Addresses", "false");
/*     */ 
/* 167 */     LauncherFrame localLauncherFrame = new LauncherFrame();
/* 168 */     localLauncherFrame.setVisible(true);
/* 169 */     localLauncherFrame.customParameters.put("stand-alone", "true");
/*     */ 
/* 171 */     String str1 = null;
/* 172 */     String str2 = null;
/*     */ 
/* 174 */     for (String str4 : paramArrayOfString) {
/* 175 */       if ((str4.startsWith("-u=")) || (str4.startsWith("--user="))) {
/* 176 */         str1 = getArgValue(str4);
/* 177 */         localLauncherFrame.customParameters.put("username", str1);
/* 178 */         localLauncherFrame.loginForm.userName.setText(str1);
/* 179 */       } else if ((str4.startsWith("-p=")) || (str4.startsWith("--password="))) {
/* 180 */         str2 = getArgValue(str4);
/* 181 */         localLauncherFrame.customParameters.put("password", str2);
/* 182 */         localLauncherFrame.loginForm.password.setText(str2);
/* 183 */       } else if (str4.startsWith("--noupdate")) {
/* 184 */         localLauncherFrame.customParameters.put("noupdate", "true");
/*     */       }
/*     */     }
/*     */ 
/* 188 */     if (paramArrayOfString.length >= 3) {
/* 189 */       String AAA = paramArrayOfString[2];
/* 190 */       String str3 = "25565";
/* 191 */       if (((String)AAA).contains(":")) {
/* 192 */         String[] arrayOfString = ((String)AAA).split(":");
/* 193 */         AAA = arrayOfString[0];
/* 194 */         str3 = arrayOfString[1];
/*     */       }
/*     */ 
/* 197 */       localLauncherFrame.customParameters.put("server", AAA);
/* 198 */       localLauncherFrame.customParameters.put("port", str3);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static String getArgValue(String paramString) {
/* 203 */     int i = paramString.indexOf('=');
/* 204 */     if (i < 0) {
/* 205 */       return "";
/*     */     }
/* 207 */     return paramString.substring(i + 1);
/*     */   }
/*     */ }