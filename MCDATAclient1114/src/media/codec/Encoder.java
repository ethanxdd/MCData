package media.codec;

public interface Encoder {
  int getSampleCount(int paramInt);
  
  int encode(short[] paramArrayOfshort, int paramInt1, byte[] paramArrayOfbyte, int paramInt2);
}


/* Location:              C:\Users\ethan\Downloads\User.jar!\media\codec\Encoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */