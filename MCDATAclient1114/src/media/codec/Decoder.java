package media.codec;

import java.io.IOException;

public interface Decoder {
  int getSampleCount(int paramInt);
  
  int decode(short[] paramArrayOfshort, byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException;
}


/* Location:              C:\Users\ethan\Downloads\User.jar!\media\codec\Decoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */