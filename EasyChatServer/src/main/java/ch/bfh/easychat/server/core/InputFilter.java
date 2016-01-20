package ch.bfh.easychat.server.core;

import ch.bfh.easychat.server.core.PlainInput;

/**
 *
 * @author Samuel Egger
 */
public interface InputFilter {
    
    String filter(PlainInput input);
}
