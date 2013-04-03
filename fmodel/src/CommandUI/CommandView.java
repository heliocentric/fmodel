/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CommandUI;

import com.github.heliocentric.fmodel.View;
import com.github.heliocentric.fmodel.ViewMessage;

/**
 *
 * @author helio
 */
public class CommandView extends View {

	@Override
	public void Enqueue(ViewMessage message) {
		switch (message.Type) {
			case View_Debug:
				System.out.println("DEBUG " + message.getInteger().toString() + ": " + message.getString());
				break;
			default:
		}
	}
	
}
