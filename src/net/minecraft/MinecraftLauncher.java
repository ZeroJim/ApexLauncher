/*    */ package net.minecraft;
/*    */ 
/*    */ import java.net.URI;
/*    */ import java.net.URL;
/*    */ import java.security.CodeSource;
/*    */ import java.security.ProtectionDomain;
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public class MinecraftLauncher
/*    */ {
/*    */   private static final int MIN_HEAP = 511;
/*    */   private static final int RECOMMENDED_HEAP = 1024;
/*    */ 
/*    */   public static void main(String[] paramArrayOfString)
/*    */     throws Exception
/*    */   {
/* 10 */     float f = (float)(Runtime.getRuntime().maxMemory() / 1024L / 1024L);
/*    */ 
/* 12 */     if (f > 511.0F)
/* 13 */       LauncherFrame.main(paramArrayOfString);
/*    */     else
/*    */       try {
/* 16 */         String str = MinecraftLauncher.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
/*    */ 
/* 18 */         ArrayList localArrayList = new ArrayList();
/*    */ 
/* 20 */         if (Util.getPlatform().equals(Util.OS.WINDOWS))
/* 21 */           localArrayList.add("javaw");
/*    */         else {
/* 23 */           localArrayList.add("java");
/*    */         }
/* 25 */         localArrayList.add("-Xmx1024m");
/* 26 */         localArrayList.add("-Dsun.java2d.noddraw=true");
/* 27 */         localArrayList.add("-Dsun.java2d.d3d=false");
/* 28 */         localArrayList.add("-Dsun.java2d.opengl=false");
/* 29 */         localArrayList.add("-Dsun.java2d.pmoffscreen=false");
/*    */ 
/* 31 */         localArrayList.add("-classpath");
/* 32 */         localArrayList.add(str);
/* 33 */         localArrayList.add("net.minecraft.LauncherFrame");
/* 34 */         ProcessBuilder localProcessBuilder = new ProcessBuilder(localArrayList);
/* 35 */         Process localProcess = localProcessBuilder.start();
/* 36 */         if (localProcess == null) throw new Exception("!");
/* 37 */         System.exit(0);
/*    */       } catch (Exception localException) {
/* 39 */         localException.printStackTrace();
/* 40 */         LauncherFrame.main(paramArrayOfString);
/*    */       }
/*    */   }
/*    */ }