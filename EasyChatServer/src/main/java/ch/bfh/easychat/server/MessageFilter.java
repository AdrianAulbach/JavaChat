/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.easychat.server;

import ch.bfh.easychat.common.EasyMessage;
import ch.bfh.easychat.server.core.PlainInput;

/**
 *
 * @author Samuel
 */
public class MessageFilter implements InputFilter {

    private final MessageProvider provider;

    public MessageFilter(MessageProvider provider) {
        this.provider = provider;
    }

    @Override
    public String filter(PlainInput input) {
        EasyMessage message = EasyMessage.load(input.getPainInput());
        if (message != null) {
            provider.broadcast(message);
            System.out.println("Received message; " + message.getMessage());
            input.onHandled();
        }

        return "";
    }
}
