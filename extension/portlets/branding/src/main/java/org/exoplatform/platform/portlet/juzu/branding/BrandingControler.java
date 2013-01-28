/*
 * Copyright (C) 2003-2012 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.exoplatform.platform.portlet.juzu.branding;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import juzu.Path;
import juzu.Resource;
import juzu.Response;
import juzu.Route;
import juzu.View;
import juzu.plugin.ajax.Ajax;
import juzu.request.HttpContext;
import juzu.template.Template;
import org.apache.commons.fileupload.FileItem;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.platform.portlet.juzu.branding.models.DataStorageService;

/**
 * Created by The eXo Platform SAS Author : Canh Pham Van canhpv@exoplatform.com
 * Nov 26, 2012
 */

public class BrandingControler {

	public static String BAR_NAVIGATION_STYLE_KEY = "bar_navigation_style";
	public static String fileName;
	public static String style = "";

	@Inject
	@Path("index.gtmpl")
	Template index;

	// @Inject
	// SettingService settingService;

	@Inject
	DataStorageService dataStorageService;

	@Ajax
	@Resource
	@Route("/save")
	public Response.Content saveParameter(String style) throws IOException {
		this.style = style;
		System.out.println("-------------save style");
		return Response.ok("Changes in branding settings have been saved ");
	}

	@Ajax
	@Resource
	public Response.Content getStyleValue() {
		if (this.style == null || this.style == "") {
			this.style = "dark";
		}
		return Response.ok(style);
	}

	@Resource
	@Route("/resource")
	public Response.Content resource(String style, FileItem file)
			throws IOException {
		// if (style != null && style != "") {
		// settingService.set(Context.GLOBAL, Scope.GLOBAL,
		// BAR_NAVIGATION_STYLE_KEY, SettingValue.create(style));
		// }
		System.out.println("controler style value " + style);
		if (style != null && style != "") {
			System.out.println("controler save style value " + style);
			this.style = style;
		}
		System.out.println("style--" + style);
		System.out.println("file---" + file);
		System.out.println("-------------save file");

		// to do add validate file and save file name in the setting
		if (file != null && file.getName() != null) {
			this.fileName = file.getName();
			dataStorageService.saveFile(file);
		}
		return Response.ok("Changes in branding settings have been saved ");
	}

	// /rest/jcr/repository/collaboration/Application%20Data/logos/error.jpg
	@View
	@Route("/")
	public Response index(HttpContext httpContext) {
		String portalName = ExoContainerContext.getCurrentContainer()
				.getContext().getPortalContainerName();
		String urlPrefix = httpContext.getScheme()
				+ "://"
				+ httpContext.getServerName()
				+ ":"
				+ httpContext.getServerPort()
				+ "/"
				+ portalName
				+ "/rest/jcr/repository/collaboration/Application%20Data/logos/";
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("url", BrandingControler_.resource(null));
		// parameters.put("style", settingService.get(Context.GLOBAL,
		// Scope.GLOBAL,
		// BAR_NAVIGATION_STYLE_KEY).getValue());
		System.out.println("controler render style value " + style);
		if (fileName == null || fileName == "") {
			parameters
					.put("imageUrl",
							"/eXoPlatformResources/skin/platformSkin/UIToolbarContainer/background/HomeIcon.png");
		} else {
			parameters.put("imageUrl", urlPrefix + fileName);
		}
		parameters.put("style", style);

		System.out.println("urlImage" + parameters.get("imageUrl"));
		return index.render(parameters);
	}
}
