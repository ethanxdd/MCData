package MCDATASipMessageHandler;

import sipMessageHandle.RequestObject;

public interface MessageHandler {
    void handle(RequestObject requestObj);
}