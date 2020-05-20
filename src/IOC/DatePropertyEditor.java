package IOC;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by CCCQQF on 2020/5/21.
 */
public class DatePropertyEditor extends PropertyEditorSupport {
    private String datePattern;

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
        Date date = null;
        try {
            date = sdf.parse(text);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        setValue(date);
    }

    public String getDatePattern() {
        return datePattern;
    }

    public void setDatePattern(String datePattern) {
        this.datePattern = datePattern;
    }
}


class  DatePropertyEditorRegistrar implements PropertyEditorRegistrar{
    private DatePropertyEditor datePropertyEditor;
    @Override
    public void registerCustomEditors(PropertyEditorRegistry propertyEditorRegistry) {
        propertyEditorRegistry.registerCustomEditor(java.util.Date.class,getDatePropertyEditor());
    }

    public DatePropertyEditor getDatePropertyEditor() {
        return datePropertyEditor;
    }

    public void setDatePropertyEditor(DatePropertyEditor datePropertyEditor) {
        this.datePropertyEditor = datePropertyEditor;
    }
}
