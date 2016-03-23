package cn.clo.common;

public class CRC16 {
    private short[] crcTable = new short[256];
    private int gPloy = 0x1021; // ���ɶ���ʽ
    public CRC16() {
        computeCrcTable();
    }
    private short getCrcOfByte(int aByte) {
        int value = aByte << 8;
        for (int count = 7; count >= 0; count--) {
            if ((value & 0x8000) != 0) { // �ߵ�16λΪ1�����԰�λ���
                value = (value << 1) ^ gPloy;
            } else {
                value = value << 1; // ��λΪ0������
            }
        }
        value = value & 0xFFFF; // ȡ��16λ��ֵ
        return (short)value;
    }
    /*
     * ����0 - 255��Ӧ��CRC16У����
     */
    private void computeCrcTable() {
        for (int i = 0; i < 256; i++) {
            crcTable[i] = getCrcOfByte(i);
        }
    }
    public short getCrc(byte[] data) {
        int crc = 0;
        int length = data.length;
        for (int i = 0; i < length; i++) {
            crc = ((crc & 0xFF) << 8) ^ crcTable[(((crc & 0xFF00) >> 8) ^ data[i]) & 0xFF];
        }
        crc = crc & 0xFFFF;
        return (short)crc;
    }
}
 
final class CodecUtil {
    static CRC16 crc16 = new CRC16();
    private CodecUtil() {
    }
    public static byte[] short2bytes(short s) {
        byte[] bytes = new byte[2];
        for (int i = 1; i >= 0; i--) {
            bytes[i] = (byte)(s % 256);
            s >>= 8;
        }
        return bytes;
    }
    public static short bytes2short(byte[] bytes) {
        short s = (short)(bytes[1] & 0xFF);
        s |= (bytes[0] << 8) & 0xFF00;
        return s;
    }
    /*
     * ��ȡcrcУ���byte��ʽ
     */
    public static byte[] crc16Bytes(byte[] data) {
        return short2bytes(crc16Short(data));
    }
    /*
     * ��ȡcrcУ���short��ʽ
     */
    public static short crc16Short(byte[] data) {
        return crc16.getCrc(data);
    }
}