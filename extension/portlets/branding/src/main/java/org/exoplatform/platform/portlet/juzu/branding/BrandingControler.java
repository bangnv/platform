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
 * Created by The eXo Platform SAS
 * Author : Canh Pham Van
 *          canhpv@exoplatform.com
 * Nov 26, 2012  
 */
public class BrandingControler {
  
public static  String fileName;  
  private String style="";
  @Inject
  @Path("index.gtmpl")
  Template index;
  
  @Inject
  DataStorageService dataStorageService_;
  
  @Ajax
  @Resource
  @Route("/save")
  public Response.Content  saveParameter(String style) throws IOException  { 
    this.style=style;
    System.out.println("-------------save style");
    return Response.ok("Changes in branding settings have been saved "); 
  }
    
 
  @Resource
  @Route("/resource")
  public Response.Content  resource(FileItem file) throws IOException  { 
    System.out.println("-------------save file");
    if(file.getName()!=null && file.getName()!="") {
    fileName= file.getName();
    }
    dataStorageService_.saveFile(file);
    return Response.ok("yes"); 
  }
//  /rest/jcr/repository/collaboration/Application%20Data/logos/error.jpg
  @View
  @Route("/")
  public Response index(HttpContext httpContext) {
    String portalName=ExoContainerContext.getCurrentContainer().getContext().getPortalContainerName();
    String urlPrefix=httpContext.getScheme()+"://"+ httpContext.getServerName()+":"+httpContext.getServerPort()+"/"+portalName+"/rest/jcr/repository/collaboration/Application%20Data/logos/";
    
    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put("url", BrandingControler_.resource());
    parameters.put("style", style);
    parameters.put("imageUrl", urlPrefix+fileName);
    System.out.println("urlImage"+parameters.get("imageUrl"));
    return index.render(parameters);
  }
}
