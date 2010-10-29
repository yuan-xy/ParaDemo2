package parallelizetest.views;

import java.awt.BorderLayout;
import java.awt.Frame;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class FlowGraphView extends ViewPart {

	private Frame canvasFrame;
	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		Composite drawarea;
		drawarea = new Composite(parent, SWT.EMBEDDED);
		drawarea.setVisible(true);
		canvasFrame = SWT_AWT.new_Frame(drawarea);
		canvasFrame.setVisible(false);
		// TODO Auto-generated method stub
		canvasFrame.add(new FlowGraph(),BorderLayout.CENTER);
		canvasFrame.setVisible(true);

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
