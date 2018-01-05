package im.jizhu.com.loginmodule.protobuf.base;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * 数据缓冲区对象(ChannelBuffer)
 *
 * @author Nana
 */
public class DataBuffer {

	private static final String TAG = "DataBuffer";

	public ByteBuffer buffer;

	public DataBuffer() {
		buffer = ByteBuffer.allocate(256);
	}

	public DataBuffer(ByteBuffer binaryBuffer) {
		buffer = binaryBuffer;
	}

	public DataBuffer(byte[] bytes,int len) {
		buffer = ByteBuffer.wrap(bytes,0,len);
	}

	public DataBuffer(int len) {
		buffer = ByteBuffer.allocate(len);
	}

	public byte[] array() {
		return buffer.array();
	}

	public void setOrignalBuffer(ByteBuffer buffer) {
		this.buffer = buffer;
	}

	public ByteBuffer getOrignalBuffer() {
		return buffer;
	}

	public void writeByte(int value) {
		buffer.put((byte) value);
	}

	public byte readByte() {
		return buffer.get();
	}

	public void writeBytes(byte[] bytes) {
		buffer.put(bytes);
	}

	public byte[] readBytes(int length) {
		byte[] bytes = new byte[length];
		buffer.get(bytes);
		return bytes;
	}

	public void writeInt(int value) {
		buffer.putInt(value);
	}

	public int readInt() {
		return buffer.getInt();
	}

	public void writeShort(short value) {
		buffer.putShort(value);
	}

	public short readShort() {
		return buffer.getShort();
	}

	public void writeChar(char c) {
		buffer.putChar(c);
	}

	public char readChar() {
		return buffer.getChar();
	}

	public void writeLong(long value) {
		buffer.putLong(value);
	}

	public long readLong() {
		return buffer.getLong();
	}

	public void writeDouble(double value) {
		buffer.putDouble(value);
	}

	public double readDouble() {
		return buffer.getDouble();
	}


	/**
	 * 读取一个字符串
	 *
	 * @return 格式：前导length表示字符串的byte数 length(4字节)string(length字节)
	 */
	public String readString() {
		int length = readInt();
		byte[] bytes = readBytes(length);
		return new String(bytes, Charset.forName("utf8"));
	}

	public String readString(int length) {
		byte[] bytes = readBytes(length);
		return new String(bytes);
	}

	public void writeSourceBytes(byte[] bytes) {
		writeInt(bytes.length);
		buffer.put(bytes);
	}

	/**
	 * 读取int数组
	 *
	 * @return 格式：前导count表示数组中有多少个元素 count(4字节)int1(4字节)...intCount(4字节)
	 */
	public int[] readIntArray() {
		int count = readInt();
		int[] intArray = new int[count];
		for (int i = 0; i < count; i++) {
			intArray[i] = readInt();
		}
		return intArray;
	}

	/**
	 * 写入int数组
	 *
	 * @param intArray 格式见readIntArray()
	 */
	public void writeIntArray(int[] intArray) {
		int count = intArray.length;
		writeInt(count);
		for (int i = 0; i < count; i++) {
			writeInt(intArray[i]);
		}
	}

//    /**
//     * 获取有效(可读取)的byte数
//     *
//     * @return
//     */
//    public int readableBytes() {
//        return buffer.readableBytes();
//    }
//
//    public DataBuffer readDataBuffer() {
//        if (null == buffer || buffer.readableBytes() == 0) {
//            return new DataBuffer(0);
//        }
//        int length = readInt();
//        DataBuffer dataBuffer = new DataBuffer(0);
//        dataBuffer.setOrignalBuffer(buffer.readBytes(length));
//        return dataBuffer;
//    }

	public void writeByteBuffer(ByteBuffer buffer) {
		if (buffer == null) {
			return;
		}
		this.buffer = buffer;
	}

	public void clear(){
		this.buffer.clear();
	}
}
