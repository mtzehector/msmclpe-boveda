/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.boveda.exception;

import mx.gob.imss.dpes.common.exception.BusinessException;

/**
 *
 * @author eduardo.loyo
 */
public class BovedaException extends BusinessException {

    public final static String CREATE = "msg058";
    public final static String LOAD = "msg059";
    public final static String UPDATE = "msg060"; 
    public final static String FILENULL = "msg061";

    public BovedaException(String causa) {
        super(causa);
    }

}
