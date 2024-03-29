/*
 * Copyright 2008 Claudius Hauptmann
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.claudiushauptmann.gwt.maps.gxt.samples.client;

import com.claudiushauptmann.gwt.maps.gxt.client.DefaultOverlayTip;
import com.claudiushauptmann.gwt.maps.gxt.client.MapGXTController;
import com.claudiushauptmann.gwt.maps.gxt.client.MenuProvider;
import com.claudiushauptmann.gwt.maps.gxt.client.PolygonGXTController;
import com.claudiushauptmann.gwt.maps.gxt.client.TipProvider;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.tips.Tip;
import com.google.gwt.maps.client.event.PolygonMouseOutHandler;
import com.google.gwt.maps.client.event.PolygonMouseOverHandler;
import com.google.gwt.maps.client.event.PolygonRemoveHandler;
import com.google.gwt.maps.client.overlay.Polygon;

public class MyPolygonEditController {
	private MapGXTController mapGxtController;
	private Polygon polygon;
	private PolygonGXTController polygonGXTController;

	private MyTipProvider myTipProvider;
	private DefaultOverlayTip overlayTip;
	private String tipTitle;
	private String tipText;

	private PolygonMenuProvider polygonMenuProvider;
	private Menu polygonMenu;
	private MenuItem polygonDeleteMenuItem;
	private MyDeletePolygonHandler polygonDeleteMenuItemHandler;

	private VertexMenuProvider vertexMenuProvider;
	private Menu vertexMenu;
	private MenuItem vertexDeleteMenuItem;
	private VertexDeleteMenuItemHandler vertexDeleteMenuItemHandler;

	@SuppressWarnings("unused")
	private MyPolygonHandlers myPolygonHandlers;

	public MyPolygonEditController(MapGXTController mapGxtController,
			Polygon polygon, String tipTitle, String tipText) {
		this.polygon = polygon;
		this.mapGxtController = mapGxtController;
		this.tipTitle = tipTitle;
		this.tipText = tipText;

		polygonGXTController = new PolygonGXTController(mapGxtController,
				polygon);

		myTipProvider = new MyTipProvider();
		polygonGXTController.setTipProvider(myTipProvider);

		polygonMenuProvider = new PolygonMenuProvider();
		polygonGXTController.setStandardMenuProvider(polygonMenuProvider);

		vertexMenuProvider = new VertexMenuProvider();
		polygonGXTController.setVertexMenuProvider(vertexMenuProvider);

		myPolygonHandlers = new MyPolygonHandlers();
	}

	private class MyTipProvider implements TipProvider {
		public Tip getTip() {
			if (overlayTip == null) {
				overlayTip = new DefaultOverlayTip();
				overlayTip.setTitle(tipTitle);
				overlayTip.setDescription(tipText);
			}

			return overlayTip;
		}
	}

	private class PolygonMenuProvider implements MenuProvider {
		public Menu getMenu() {
			if (polygonMenu == null) {
				polygonMenu = new Menu();

				polygonDeleteMenuItem = new MenuItem();
				polygonDeleteMenuItem.setText("Delete Polygon");
				polygonDeleteMenuItemHandler = new MyDeletePolygonHandler();
				polygonDeleteMenuItem
						.addSelectionListener(polygonDeleteMenuItemHandler);
				polygonMenu.add(polygonDeleteMenuItem);
			}

			return polygonMenu;
		}
	}

	private class VertexMenuProvider implements MenuProvider {
		public Menu getMenu() {
			if (vertexMenu == null) {
				vertexMenu = new Menu();

				vertexDeleteMenuItem = new MenuItem();
				vertexDeleteMenuItem.setText("Delete Vertex");
				vertexDeleteMenuItemHandler = new VertexDeleteMenuItemHandler();
				vertexDeleteMenuItem
						.addSelectionListener(vertexDeleteMenuItemHandler);
				vertexMenu.add(vertexDeleteMenuItem);
			}

			vertexDeleteMenuItem.setEnabled(polygon.getVertexCount() > 4);

			return vertexMenu;
		}
	}

	private class MyDeletePolygonHandler extends SelectionListener<MenuEvent> {

		@Override
		public void componentSelected(MenuEvent ce) {
			mapGxtController.getMapWidget().removeOverlay(polygon);
		}

	}

	private class VertexDeleteMenuItemHandler extends
			SelectionListener<MenuEvent> {

		@Override
		public void componentSelected(MenuEvent me) {
			polygon.deleteVertex(polygonGXTController.getLastClickedVertex());
		}

	}

	private class MyPolygonHandlers implements PolygonMouseOverHandler,
			PolygonMouseOutHandler, PolygonRemoveHandler {

		public MyPolygonHandlers() {
			polygon.addPolygonMouseOverHandler(this);
			polygon.addPolygonMouseOutHandler(this);
			polygon.addPolygonRemoveHandler(this);
		}

		public void onMouseOver(PolygonMouseOverEvent event) {
			polygon.setEditingEnabled(true);
		}

		public void onMouseOut(PolygonMouseOutEvent event) {
			polygon.setEditingEnabled(false);
		}

		public void onRemove(PolygonRemoveEvent event) {
			polygon.removePolygonMouseOverHandler(this);
			polygon.removePolygonMouseOutHandler(this);
			polygon.removePolygonRemoveHandler(this);
		}

	}
}
