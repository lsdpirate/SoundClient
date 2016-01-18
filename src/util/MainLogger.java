/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lsdpirate
 */
public class MainLogger {
    private static Logger logger = Logger.getGlobal();
    private static final Handler handler = new ConsoleHandler();
    
    public MainLogger(){
        logger.addHandler(handler);
        logger.setLevel(Level.ALL);
        handler.setLevel(Level.ALL);
    }

    public static void log(Level level, String msg) {
        logger.log(level, msg);
    }

    public static void logp(Level level, String sourceClass, String sourceMethod, String msg) {
        logger.logp(level, sourceClass, sourceMethod, msg);
    }

    public static void logp(Level level, String sourceClass, String sourceMethod, String msg, Object[] params) {
        logger.logp(level, sourceClass, sourceMethod, msg, params);
    }

    public static void log(Level level, String msg, Throwable thrown) {
        logger.log(level, msg, thrown);
    }

    public static void log(Level level, String msg, Object param1) {
        logger.log(level, msg, param1);
    }

    public static void log(Level level, String msg, Object[] params) {
        logger.log(level, msg, params);
    }
    
    
    
}
