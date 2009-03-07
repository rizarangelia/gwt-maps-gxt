package com.claudiushauptmann.gwt.maps.gxt.client;

import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.client.Window;

public class MarkerGXTController extends MarkerMenuTipController {
	private OverlayTip overlayTip;
	private Menu menu;
	
	public MarkerGXTController(MapWidget mapWidget, Marker marker) {
		super(mapWidget, marker);
	}

	public OverlayTip getOverlayTip() {
		return overlayTip;
	}

	public void setOverlayTip(OverlayTip overlayTip) {
		this.overlayTip = overlayTip;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}
	
	@Override
	protected void showOverlayTip() {
		overlayTip.showAt(currentMousePosition.getX() + 20, currentMousePosition.getY()+20);
		updateOverlayTip();
	}
	
	@Override
	protected void updateOverlayTip() {
		int x = currentMousePosition.getX() + 20;
		int y = currentMousePosition.getY() + 20;
		
		int width = overlayTip.getWidth();
		int height = overlayTip.getHeight();

		if ((x + width) > Window.getClientWidth() + Window.getScrollLeft() - 10) {
			x = currentMousePosition.getX() - 20 - width;
		}
		if ((y + height) > Window.getClientHeight() + Window.getScrollTop() - 10) {
			y = currentMousePosition.getY() - 20 - height;
		}

		overlayTip.setPosition(x, y);
	}
	
	@Override
	protected void hideOverlayTip() {
		overlayTip.hide();
	}

	@Override
	protected void showMenu() {
		GwtMapsGxt.MenuTimer.showMenu(menu, currentMousePosition);
		GwtMapsGxt.get().setCurrentMenu(menu);
	}

	@Override
	protected boolean isMenuVisible() {
		return menu.isVisible();
	}

	@Override
	protected void hideMenu() {
		if ((menu != null) && (menu.isVisible())) {
			menu.hide();
		}
	}	
}