/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.easychat.server;

import ch.bfh.easychat.common.EasyMessage;

/**
 *
 * @author Samuel
 */
public class MessageFilter implements InputFilter{

    private final MessageProvider provider;
    
    public MessageFilter(MessageProvider provider){
        this.provider = provider;
    }
    
    @Override
    public String filter(String input) {
        EasyMessage message = EasyMessage.load(input);
        if(message != null){
            provider.broadcast(message);
        }
        
        return "";
    }
}
