package MCDATASipMessageHandler;

import sipMessageHandle.RequestObject;

public class FDOneToOneMessageHandler implements MessageHandler {
	@Override
    public void handle(RequestObject requestObj) {
        
        System.out.println("生成的 SIP 请求:\n" );
    }
}
