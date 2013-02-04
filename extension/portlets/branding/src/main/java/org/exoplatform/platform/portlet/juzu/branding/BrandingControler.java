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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.inject.Inject;
import juzu.Path;
import juzu.Resource;
import juzu.Response;
import juzu.Route;
import juzu.View;
import juzu.io.Stream;
import juzu.plugin.ajax.Ajax;
import juzu.request.HttpContext;
import juzu.template.Template;
import org.apache.commons.fileupload.FileItem;
import org.exoplatform.commons.api.settings.SettingService;
import org.exoplatform.commons.api.settings.SettingValue;
import org.exoplatform.commons.api.settings.data.Context;
import org.exoplatform.commons.api.settings.data.Scope;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.platform.portlet.juzu.branding.models.DataStorageService;

/**
 * Created by The eXo Platform SAS Author : Nguyen Viet Bang
 * bangnv@exoplatform.com Jan 28, 2013
 */

public class BrandingControler {

  public static String BAR_NAVIGATION_STYLE_KEY = "bar_navigation_style";

  public static String fileName;

  public static String style                    = "";

  @Inject
  @Path("index.gtmpl")
  Template             index;

  @Inject
  SettingService       settingService;

  @Inject
  DataStorageService   dataStorageService;

  @Ajax
  @Resource
  public Response.Content getStyleValue() {

    String style = "";
    if (settingService.get(Context.GLOBAL, Scope.GLOBAL, BAR_NAVIGATION_STYLE_KEY) == null) {

      style = "Light";

    } else {
      style = (String) settingService.get(Context.GLOBAL, Scope.GLOBAL, BAR_NAVIGATION_STYLE_KEY)
                                     .getValue();
    }
    return Response.ok(style);
  }

  @Resource
  public Response.Content save(HttpContext httpContext, FileItem file, String style) throws IOException {
    if (file != null && file.getContentType().startsWith("image/")) {
      dataStorageService.saveFile(file);
    }
    if (style != null && style != "") {
      settingService.set(Context.GLOBAL,
                         Scope.GLOBAL,
                         BAR_NAVIGATION_STYLE_KEY,
                         SettingValue.create(style));
      this.style = style;
    }
    return getResource(httpContext);
  }

  @View
  @Route("/")
  public Response index(HttpContext httpContext) {

    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put("url", BrandingControler_.save(null));
    parameters.put("imageUrl", getLogoUrl(httpContext));
    return index.render(parameters);
  }

  public boolean isExiste(String logoUrl) {
    int code;
    try {
      URL u = new URL(logoUrl);
      HttpURLConnection huc = (HttpURLConnection) u.openConnection();
      huc.setRequestMethod("GET"); // OR huc.setRequestMethod ("HEAD");
      huc.connect();
      code = huc.getResponseCode();
    } catch (Exception e) {
      return false;
    }
    return code == 200;
  }

  @Ajax
  @Resource
  public Response.Content getLogoUrlByAjax(HttpContext httpContext) {
    String logoUrl = getLogoUrl(httpContext);
    return Response.ok(logoUrl);
  }

  public String getLogoUrl(HttpContext httpContext) {
    String portalName = ExoContainerContext.getCurrentContainer()
                                           .getContext()
                                           .getPortalContainerName();
    String logoUrl = httpContext.getScheme() + "://" + httpContext.getServerName() + ":"
        + httpContext.getServerPort() + "/" + portalName
        + "/rest/jcr/repository/collaboration/Application%20Data/logos/logo.png?"
        + System.currentTimeMillis();

    if (!isExiste(logoUrl)) {
      logoUrl = "/eXoPlatformResources/skin/platformSkin/UIToolbarContainer/background/HomeIcon.png";
    }
    return logoUrl;
  }

  @Ajax
  @Resource
  public Response.Content<Stream.Char> getResource(HttpContext httpContext) {
    Map<String, String> result = new HashMap<String, String>();
    String style = "";
    if (settingService.get(Context.GLOBAL, Scope.GLOBAL, BAR_NAVIGATION_STYLE_KEY) == null) {

      style = "Light";
    } else {
      style = (String) settingService.get(Context.GLOBAL, Scope.GLOBAL, BAR_NAVIGATION_STYLE_KEY)
                                     .getValue();
    }
    result.put("style", style);
    result.put("logoUrl", getLogoUrl(httpContext));
    return createJSON(result);
  }

  private Response.Content<Stream.Char> createJSON(final Map<String, String> data) {
    Response.Content<Stream.Char> json = new Response.Content<Stream.Char>(200, Stream.Char.class) {

      @Override
      public String getMimeType() {
        return "application/json";
      }

      @Override
      public void send(Stream.Char stream) throws IOException {
        stream.append("{");
        Iterator<Map.Entry<String, String>> i = data.entrySet().iterator();
        while (i.hasNext()) {
          Map.Entry<String, String> entry = i.next();
          stream.append("\"" + entry.getKey() + "\"");
          stream.append(":");
          stream.append("\"" + entry.getValue() + "\"");
          if (i.hasNext()) {
            stream.append(",");
          }
        }
        stream.append("}");
      }
    };
    return json;
  }

  // @Ajax
  // @Resource
  // public Response.Content saveParameter(String style) throws IOException {
  //
  // }
}
