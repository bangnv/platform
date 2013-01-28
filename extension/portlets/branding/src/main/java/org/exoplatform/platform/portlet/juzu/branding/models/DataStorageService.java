/*
 * Copyright (C) 2003-2013 eXo Platform SAS.
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
package org.exoplatform.platform.portlet.juzu.branding.models;

import java.util.Calendar;

import javax.jcr.Node;
import javax.jcr.Session;

import org.apache.commons.fileupload.FileItem;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.platform.portlet.juzu.branding.BrandingControler;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.jcr.ext.common.SessionProvider;



/**
 * Created by The eXo Platform SAS Author : Nguyen Viet Bang
 * bangnv@exoplatform.com Jan 22, 2013
 */
public class DataStorageService {
  // final static String fileName="logo";
  RepositoryService repositoryService;

  public DataStorageService( ) {
     repositoryService = (RepositoryService) ExoContainerContext.getCurrentContainer().getComponentInstanceOfType(RepositoryService.class);
  }

  public void saveFile(FileItem item) {
  
    String filename = item.getName();
    SessionProvider sessionProvider = SessionProvider.createSystemProvider();
    try {
      // get info
      Session session = sessionProvider.getSession("collaboration",
                                                   repositoryService.getCurrentRepository());
      Node rootNode = session.getRootNode();
      if (!rootNode.hasNode("Application Data")) {
        rootNode.addNode("Application Data", "nt:folder");
        session.save();
      }
      Node applicationDataNode = rootNode.getNode("Application Data");
       if (!applicationDataNode.hasNode("logos")) {
       applicationDataNode.addNode("logos", "nt:folder");
       session.save();
       }
       Node logosNode=applicationDataNode.getNode("logos");
     
      Node fileNode = null;
      if (logosNode.hasNode(filename)) {
        System.out.println("filename " + filename);
        fileNode = logosNode.getNode(filename);
        fileNode.remove();
        session.save();
      }
      fileNode = logosNode.addNode(filename, "nt:file");
      Node jcrContent = fileNode.addNode("jcr:content", "nt:resource");
      jcrContent.setProperty("jcr:data", item.getInputStream());
      jcrContent.setProperty("jcr:lastModified", Calendar.getInstance());
      jcrContent.setProperty("jcr:encoding", "UTF-8");
      jcrContent.setProperty("jcr:mimeType", item.getContentType());
      logosNode.save();
      session.save();
    } catch (Exception e) {
      System.out.println("JCR::" + e.getMessage());
    } finally {
      sessionProvider.close();
    }
  }

}