/*
 * DBObject.java
 *
 * Created on January 8, 2004, 8:36 PM
 */

package zaytoven.controller;

/**
 *
 * @author  root
 */
public class DBObject {
    
    /** Creates a new instance of DBObject */
    public DBObject() {
    }
    
    public String getDBObject(Object queryResultObject, String defaultString){
     
        if (queryResultObject == null) {
            
            return defaultString;
            
        } else {
            
            return queryResultObject.toString();
            
        }
        
    }
    /** A Polite way to call object {@link #toString() }
     * @param value to return as a string
     * @param defaultValue the default value to return in case the value is null
     * @return  */
    public static String toString(Object value,String defaultValue){
        if(value==null) return defaultValue;
      return value.toString();
    }
    
}
