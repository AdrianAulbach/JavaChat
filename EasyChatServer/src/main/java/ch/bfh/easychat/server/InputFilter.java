package ch.bfh.easychat.server;

import java.io.OutputStream;

/**
 *
 * @author Samuel Egger
 */
public interface InputFilter {
    
    String filter(String input);
}
