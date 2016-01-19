/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.easychat.server.core;

/**
 *
 * @author Samuel Egger
 */
public class PlainInput {

    private String painInput;
    private boolean handled;

    /**
     * @return the painInput
     */
    public String getPainInput() {
        return painInput;
    }

    /**
     * @param painInput the painInput to set
     */
    public void setPainInput(String painInput) {
        this.painInput = painInput;
    }

    /**
     * @return the handled
     */
    public boolean isHandled() {
        return handled;
    }

    /**
     * @param handled the handled to set
     */
    public void setHandled(boolean handled) {
        this.handled = handled;
    }

    public PlainInput(String plainInput) {
        this.painInput = plainInput;
    }

    public void onHandled() {
        this.handled = true;
    }
}
