package filter;



import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * @author BruceYang
 * 字节数组编解码工厂
 */
public class ByteArrayCodecFactory implements ProtocolCodecFactory {
  
    private ByteArrayEncoder encoder;
    private ByteArrayDecode decoder;
    
    public ByteArrayCodecFactory() {
    	encoder = new ByteArrayEncoder();
    	decoder= new ByteArrayDecode();
    	
    }

    @Override
    public ProtocolDecoder getDecoder(IoSession session) throws Exception {
    	return decoder;
    }

    @Override
    public ProtocolEncoder getEncoder(IoSession session) throws Exception {
        return encoder;
    }

}