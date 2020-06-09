package user.minicrm.zk.util;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.CustomConstraint;

public class UserNameConstraint implements Constraint,CustomConstraint {
    public void validate(Component comp, Object value) throws WrongValueException  {
        if (value == null || ((String)value).equals(""))
        	throw new WrongValueException(comp, " ");	
    }

	public void showCustomError(Component comp, WrongValueException ex) {
		if(ex!=null)
		Clients.showNotification("Kérem adjon meg értéket!","info",comp,"end_before",800);
	}
}