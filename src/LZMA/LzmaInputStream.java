/*     */ package LZMA;
/*     */ 
/*     */ 
/*     */ 
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ public class LzmaInputStream extends FilterInputStream
/*     */ {
/*     */   boolean isClosed;
/*     */   CRangeDecoder RangeDecoder;
/*     */   byte[] dictionary;
/*     */   int dictionarySize;
/*     */   int dictionaryPos;
/*     */   int GlobalPos;
/*     */   int rep0;
/*     */   int rep1;
/*     */   int rep2;
/*     */   int rep3;
/*     */   int lc;
/*     */   int lp;
/*     */   int pb;
/*     */   int State;
/*     */   boolean PreviousIsMatch;
/*     */   int RemainLen;
/*     */   int[] probs;
/*     */   byte[] uncompressed_buffer;
/*     */   int uncompressed_size;
/*     */   int uncompressed_offset;
/*     */   long GlobalNowPos;
/*     */   long GlobalOutSize;
/*     */   static final int LZMA_BASE_SIZE = 1846;
/*     */   static final int LZMA_LIT_SIZE = 768;
/*     */   static final int kBlockSize = 65536;
/*     */   static final int kNumStates = 12;
/*     */   static final int kStartPosModelIndex = 4;
/*     */   static final int kEndPosModelIndex = 14;
/*     */   static final int kNumFullDistances = 128;
/*     */   static final int kNumPosSlotBits = 6;
/*     */   static final int kNumLenToPosStates = 4;
/*     */   static final int kNumAlignBits = 4;
/*     */   static final int kAlignTableSize = 16;
/*     */   static final int kMatchMinLen = 2;
/*     */   static final int IsMatch = 0;
/*     */   static final int IsRep = 192;
/*     */   static final int IsRepG0 = 204;
/*     */   static final int IsRepG1 = 216;
/*     */   static final int IsRepG2 = 228;
/*     */   static final int IsRep0Long = 240;
/*     */   static final int PosSlot = 432;
/*     */   static final int SpecPos = 688;
/*     */   static final int Align = 802;
/*     */   static final int LenCoder = 818;
/*     */   static final int RepLenCoder = 1332;
/*     */   static final int Literal = 1846;
/*     */ 
/*     */   public LzmaInputStream(InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/*  61 */     super(paramInputStream);
/*     */ 
/*  63 */     this.isClosed = false;
/*     */ 
/*  65 */     readHeader();
/*     */ 
/*  67 */     fill_buffer();
/*     */   }
/*     */ 
/*     */   private void LzmaDecode(int paramInt) throws IOException
/*     */   {
/*  72 */     int j = (1 << this.pb) - 1;
/*  73 */     int k = (1 << this.lp) - 1;
/*     */ 
/*  75 */     this.uncompressed_size = 0;
/*     */ 
/*  77 */     if (this.RemainLen == -1)
/*  78 */       return;
/*     */     int m;
/*  81 */     while ((this.RemainLen > 0) && (this.uncompressed_size < paramInt)) {
/*  82 */       m = this.dictionaryPos - this.rep0;
/*  83 */       if (m < 0)
/*  84 */         m += this.dictionarySize;
/*     */       byte tmp103_102 = this.dictionary[m]; this.dictionary[this.dictionaryPos] = tmp103_102; this.uncompressed_buffer[(this.uncompressed_size++)] = tmp103_102;
/*  86 */       if (++this.dictionaryPos == this.dictionarySize)
/*  87 */         this.dictionaryPos = 0;
/*  88 */       this.RemainLen -= 1;
/*     */     }
/*     */     int i;
/*  90 */     if (this.dictionaryPos == 0)
/*  91 */       i = this.dictionary[(this.dictionarySize - 1)];
/*     */     else {
/*  93 */       i = this.dictionary[(this.dictionaryPos - 1)];
/*     */     }
/*  95 */     while (this.uncompressed_size < paramInt) {
/*  96 */       m = this.uncompressed_size + this.GlobalPos & j;
/*     */       int n;
/*     */       int i1;
/*  98 */       if (this.RangeDecoder.BitDecode(this.probs, 0 + (this.State << 4) + m) == 0) {
/*  99 */         n = 1846 + 768 * (((this.uncompressed_size + this.GlobalPos & k) << this.lc) + ((i & 0xFF) >> 8 - this.lc));
/*     */ 
/* 103 */         if (this.State < 4)
/* 104 */           this.State = 0;
/* 105 */         else if (this.State < 10)
/* 106 */           this.State -= 3;
/*     */         else
/* 108 */           this.State -= 6;
/* 109 */         if (this.PreviousIsMatch) {
/* 110 */           i1 = this.dictionaryPos - this.rep0;
/* 111 */           if (i1 < 0)
/* 112 */             i1 += this.dictionarySize;
/* 113 */           byte b = this.dictionary[i1];
/*     */ 
/* 115 */           i = this.RangeDecoder.LzmaLiteralDecodeMatch(this.probs, n, b);
/* 116 */           this.PreviousIsMatch = false;
/*     */         } else {
/* 118 */           i = this.RangeDecoder.LzmaLiteralDecode(this.probs, n);
/*     */         }
/*     */ 
/* 121 */         this.uncompressed_buffer[(this.uncompressed_size++)] = (byte) i;
/*     */ 
/* 123 */         this.dictionary[this.dictionaryPos] = (byte) i;
/* 124 */         if (++this.dictionaryPos == this.dictionarySize)
/* 125 */           this.dictionaryPos = 0;
/*     */       }
/*     */       else {
/* 128 */         this.PreviousIsMatch = true;
/* 129 */         if (this.RangeDecoder.BitDecode(this.probs, 192 + this.State) == 1) {
/* 130 */           if (this.RangeDecoder.BitDecode(this.probs, 204 + this.State) == 0) {
/* 131 */             if (this.RangeDecoder.BitDecode(this.probs, 240 + (this.State << 4) + m) == 0)
/*     */             {
/* 133 */               if (this.uncompressed_size + this.GlobalPos == 0) {
/* 134 */                 throw new LzmaException("LZMA : Data Error");
/*     */               }
/* 136 */               this.State = (this.State < 7 ? 9 : 11);
/*     */ 
/* 138 */               n = this.dictionaryPos - this.rep0;
/* 139 */               if (n < 0)
/* 140 */                 n += this.dictionarySize;
/* 141 */               i = this.dictionary[n];
/* 142 */               this.dictionary[this.dictionaryPos] = (byte) i;
/* 143 */               if (++this.dictionaryPos == this.dictionarySize) {
/* 144 */                 this.dictionaryPos = 0;
/*     */               }
/* 146 */               this.uncompressed_buffer[(this.uncompressed_size++)] = (byte) i;
/* 147 */               continue;
/*     */             }
/*     */           }
/*     */           else {
/* 151 */             if (this.RangeDecoder.BitDecode(this.probs, 216 + this.State) == 0) {
/* 152 */               n = this.rep1;
/*     */             } else {
/* 154 */               if (this.RangeDecoder.BitDecode(this.probs, 228 + this.State) == 0) {
/* 155 */                 n = this.rep2;
/*     */               } else {
/* 157 */                 n = this.rep3;
/* 158 */                 this.rep3 = this.rep2;
/*     */               }
/* 160 */               this.rep2 = this.rep1;
/*     */             }
/* 162 */             this.rep1 = this.rep0;
/* 163 */             this.rep0 = n;
/*     */           }
/* 165 */           this.RemainLen = this.RangeDecoder.LzmaLenDecode(this.probs, 1332, m);
/* 166 */           this.State = (this.State < 7 ? 8 : 11);
/*     */         } else {
/* 168 */           this.rep3 = this.rep2;
/* 169 */           this.rep2 = this.rep1;
/* 170 */           this.rep1 = this.rep0;
/* 171 */           this.State = (this.State < 7 ? 7 : 10);
/* 172 */           this.RemainLen = this.RangeDecoder.LzmaLenDecode(this.probs, 818, m);
/* 173 */           n = this.RangeDecoder.BitTreeDecode(this.probs, 432 + ((this.RemainLen < 4 ? this.RemainLen : 3) << 6), 6);
/*     */ 
/* 176 */           if (n >= 4) {
/* 177 */             i1 = (n >> 1) - 1;
/* 178 */             this.rep0 = ((0x2 | n & 0x1) << i1);
/* 179 */             if (n < 14) {
/* 180 */               this.rep0 += this.RangeDecoder.ReverseBitTreeDecode(this.probs, 688 + this.rep0 - n - 1, i1);
/*     */             }
/*     */             else {
/* 183 */               this.rep0 += (this.RangeDecoder.DecodeDirectBits(i1 - 4) << 4);
/*     */ 
/* 185 */               this.rep0 += this.RangeDecoder.ReverseBitTreeDecode(this.probs, 802, 4);
/*     */             }
/*     */           } else {
/* 188 */             this.rep0 = n;
/* 189 */           }this.rep0 += 1;
/*     */         }
/* 191 */         if (this.rep0 == 0)
/*     */         {
/* 193 */           this.RemainLen = -1;
/* 194 */           break;
/*     */         }
/* 196 */         if (this.rep0 > this.uncompressed_size + this.GlobalPos)
/*     */         {
/* 201 */           throw new LzmaException("LZMA : Data Error");
/*     */         }
/* 203 */         this.RemainLen += 2;
/*     */         do
/*     */         {
/* 206 */           n = this.dictionaryPos - this.rep0;
/* 207 */           if (n < 0)
/* 208 */             n += this.dictionarySize;
/* 209 */           i = this.dictionary[n];
/* 210 */           this.dictionary[this.dictionaryPos] = (byte) i;
/* 211 */           if (++this.dictionaryPos == this.dictionarySize) {
/* 212 */             this.dictionaryPos = 0;
/*     */           }
/* 214 */           this.uncompressed_buffer[(this.uncompressed_size++)] = (byte) i;
/* 215 */           this.RemainLen -= 1;
/* 216 */         }while ((this.RemainLen > 0) && (this.uncompressed_size < paramInt));
/*     */       }
/*     */     }
/*     */ 
/* 220 */     this.GlobalPos += this.uncompressed_size;
/*     */   }
/*     */ 
/*     */   private void fill_buffer() throws IOException {
/* 224 */     if (this.GlobalNowPos < this.GlobalOutSize) {
/* 225 */       this.uncompressed_offset = 0;
/* 226 */       long l = this.GlobalOutSize - this.GlobalNowPos;
/*     */       int i;
/* 228 */       if (l > 65536L)
/* 229 */         i = 65536;
/*     */       else {
/* 231 */         i = (int)l;
/*     */       }
/* 233 */       LzmaDecode(i);
/*     */ 
/* 235 */       if (this.uncompressed_size == 0)
/* 236 */         this.GlobalOutSize = this.GlobalNowPos;
/*     */       else
/* 238 */         this.GlobalNowPos += this.uncompressed_size;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void readHeader() throws IOException
/*     */   {
/* 244 */     byte[] arrayOfByte = new byte[5];
/*     */ 
/* 246 */     if (5 != this.in.read(arrayOfByte)) {
/* 247 */       throw new LzmaException("LZMA header corrupted : Properties error");
/*     */     }
/* 249 */     this.GlobalOutSize = 0L;
/* 250 */     for (int i = 0; i < 8; i++) {
/* 251 */       int j = this.in.read();
/* 252 */       if (j == -1)
/* 253 */         throw new LzmaException("LZMA header corrupted : Size error");
/* 254 */       this.GlobalOutSize += (j << i * 8);
/*     */     }
/*     */ 
/* 257 */     if (this.GlobalOutSize == -1L) this.GlobalOutSize = 9223372036854775807L;
/*     */ 
/* 259 */     int i = arrayOfByte[0] & 0xFF;
/* 260 */     if (i >= 225) {
/* 261 */       throw new LzmaException("LZMA header corrupted : Properties error");
/*     */     }
/*     */ 
/* 264 */     for (this.pb = 0; i >= 45; i -= 45) this.pb += 1;
/*     */ 
/* 266 */     for (this.lp = 0; i >= 9; i -= 9) this.lp += 1;
/*     */ 
/* 268 */     this.lc = i;
/*     */ 
/* 270 */     int j = 1846 + (768 << this.lc + this.lp);
/*     */ 
/* 272 */     this.probs = new int[j];
/*     */ 
/* 274 */     this.dictionarySize = 0;
/* 275 */     for (int k = 0; k < 4; k++)
/* 276 */       this.dictionarySize += ((arrayOfByte[(1 + k)] & 0xFF) << k * 8);
/* 277 */     this.dictionary = new byte[this.dictionarySize];
/* 278 */     if (this.dictionary == null) {
/* 279 */       throw new LzmaException("LZMA : can't allocate");
/*     */     }
/*     */ 
/* 282 */     int k = 1846 + (768 << this.lc + this.lp);
/*     */ 
/* 284 */     this.RangeDecoder = new CRangeDecoder(this.in);
/* 285 */     this.dictionaryPos = 0;
/* 286 */     this.GlobalPos = 0;
/* 287 */     this.rep0 = (this.rep1 = this.rep2 = this.rep3 = 1);
/* 288 */     this.State = 0;
/* 289 */     this.PreviousIsMatch = false;
/* 290 */     this.RemainLen = 0;
/* 291 */     this.dictionary[(this.dictionarySize - 1)] = 0;
/* 292 */     for (int m = 0; m < k; m++) {
/* 293 */       this.probs[m] = 1024;
/*     */     }
/* 295 */     this.uncompressed_buffer = new byte[65536];
/* 296 */     this.uncompressed_size = 0;
/* 297 */     this.uncompressed_offset = 0;
/*     */ 
/* 299 */     this.GlobalNowPos = 0L;
/*     */   }
/*     */ 
/*     */   public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException {
/* 303 */     if (this.isClosed) {
/* 304 */       throw new IOException("stream closed");
/*     */     }
/* 306 */     if ((paramInt1 | paramInt2 | paramInt1 + paramInt2 | paramArrayOfByte.length - (paramInt1 + paramInt2)) < 0) {
/* 307 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 309 */     if (paramInt2 == 0) {
/* 310 */       return 0;
/*     */     }
/* 312 */     if (this.uncompressed_offset == this.uncompressed_size)
/* 313 */       fill_buffer();
/* 314 */     if (this.uncompressed_offset == this.uncompressed_size) {
/* 315 */       return -1;
/*     */     }
/* 317 */     int i = Math.min(paramInt2, this.uncompressed_size - this.uncompressed_offset);
/* 318 */     System.arraycopy(this.uncompressed_buffer, this.uncompressed_offset, paramArrayOfByte, paramInt1, i);
/* 319 */     this.uncompressed_offset += i;
/* 320 */     return i;
/*     */   }
/*     */ 
/*     */   public void close() throws IOException {
/* 324 */     this.isClosed = true;
/* 325 */     super.close();
/*     */   }
/*     */ }