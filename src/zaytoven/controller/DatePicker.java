package zaytoven.controller;

/*
 * BeanTest.java
 *
 * Created on August 21, 2003, 9:20 PM
 */

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 *
 * @author  root
 */
public class DatePicker extends kiwi.ui.DateChooserField implements java.io.Serializable {
    
    private static final String PROP_SAMPLE_PROPERTY = "SampleProperty";
    
    private String sampleProperty;
    
    private PropertyChangeSupport propertySupport;
    
    /** Creates new BeanTest */
    public DatePicker() {
        super("dd MMM yyyy");
        java.util.Calendar cal = java.util.Calendar.getInstance();
        java.util.Date todayDate = cal.getTime();
        this.setDate(todayDate);
        propertySupport = new PropertyChangeSupport( this );
    }
    
    public String getSampleProperty() {
        return sampleProperty;
    }
    
    public void setSampleProperty(String value) {
        String oldValue = sampleProperty;
        sampleProperty = value;
        propertySupport.firePropertyChange(PROP_SAMPLE_PROPERTY, oldValue, sampleProperty);
    }
    
    
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
//        propertySupport.addPropertyChangeListener(listener);
    }
    
    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
//        propertySupport.removePropertyChangeListener(listener);
    }
    
}
