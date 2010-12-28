/*
 * Copyright 2000-2005 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

package org.exoplatform.imageio.plugins.gif;

import java.io.IOException;
import java.util.Locale;
import java.util.Iterator;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadataFormat;
import javax.imageio.metadata.IIOMetadataFormatImpl;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;

/**
 * version: openjdk-7-ea-src-b35-11_sep_2008
 */
public class GIFImageReaderSpi extends ImageReaderSpi {

    private static final String vendorName = "Sun Microsystems, Inc.";

    private static final String version = "1.0";

    private static final String[] names = { "gif", "GIF" };

    private static final String[] suffixes = { "gif" };

    private static final String[] MIMETypes = { "image/gif" };

    private static final String readerClassName =
        "org.exoplatform.imageio.plugins.gif.GIFImageReader";

    private static final String[] writerSpiNames = {
        "org.exoplatform.imageio.plugins.gif.GIFImageWriterSpi"
    };

    public GIFImageReaderSpi() {
        super(vendorName,
              version,
              names,
              suffixes,
              MIMETypes,
              readerClassName,
              STANDARD_INPUT_TYPE,
              writerSpiNames,
              true,
              GIFStreamMetadata.nativeMetadataFormatName,
              "org.exoplatform.imageio.plugins.gif.GIFStreamMetadataFormat",
              null, null,
              true,
              GIFImageMetadata.nativeMetadataFormatName,
              "org.exoplatform.imageio.plugins.gif.GIFImageMetadataFormat",
              null, null
              );
    }

    public String getDescription(Locale locale) {
        return "Standard GIF image reader";
    }

    public boolean canDecodeInput(Object input) throws IOException {
        if (!(input instanceof ImageInputStream)) {
            return false;
        }

        ImageInputStream stream = (ImageInputStream)input;
        byte[] b = new byte[6];
        stream.mark();
        stream.readFully(b);
        stream.reset();

        return b[0] == 'G' && b[1] == 'I' && b[2] == 'F' && b[3] == '8' &&
            (b[4] == '7' || b[4] == '9') && b[5] == 'a';
    }

    public ImageReader createReaderInstance(Object extension) {
        return new GIFImageReader(this);
    }

}
