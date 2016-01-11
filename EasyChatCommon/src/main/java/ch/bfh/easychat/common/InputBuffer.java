/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.easychat.common;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Samuel
 */
public class InputBuffer {
    
    private final List<Byte> bytes = new ArrayList<>();
    
    public void buffer(byte b){
        bytes.add(b);
    }
    
    public String asString(String charset) throws UnsupportedEncodingException{
        byte[] raw = new byte[bytes.size()];
        for (int i = 0; i < bytes.size(); i++) {
            raw[i] = bytes.get(i);
        }
        
        return new String(raw, charset);
    }
    
    public void reset(){
        bytes.clear();
    }
    
    public boolean isEmpty(){
        return bytes.isEmpty();
    }
}
