/*
        Copyright (c) 2015 King's College London

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	    http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/

package uk.ac.kcl.iop.brc.core.pipeline.common.service;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.IOUtils;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.external.ExternalParser;
import org.apache.tika.parser.ocr.TesseractOCRParser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.ToXMLContentHandler;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import uk.ac.kcl.iop.brc.core.pipeline.common.exception.CanNotProcessCoordinateException;
import uk.ac.kcl.iop.brc.core.pipeline.common.model.DNCWorkCoordinate;
import uk.ac.kcl.iop.brc.core.pipeline.common.utils.StringTools;

import java.io.*;
import java.util.Optional;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

@Service
public class DocumentConversionService {

    private Logger logger = Logger.getLogger(DocumentConversionService.class);
    public Boolean imageMagickInstalled = null;

    public String convertToText(byte[] data) {
        return convertWithHandler(data, new BodyContentHandler(-1));
    }

    public String convertToXHTML(byte[] data) {
        return convertWithHandler(data, new ToXMLContentHandler());
    }

    private String convertWithHandler(byte[] data, DefaultHandler handler) {
        AutoDetectParser parser = new AutoDetectParser();

        Metadata metadata = new Metadata();
        try (InputStream stream = new ByteArrayInputStream(data)) {
            parser.parse(stream, handler, metadata);
            return handler.toString();
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return "";
    }

    public String tryOCRByConvertingToTiff(DNCWorkCoordinate coordinate, byte[] bytes) throws CanNotProcessCoordinateException {
        validateImageMagick();
        try {
            File pdfFile = File.createTempFile(coordinate.getFileName(), ".pdf");
            FileUtils.writeByteArrayToFile(pdfFile, bytes);
            File tiffFile = File.createTempFile(coordinate.getFileName(), ".tiff");
            Optional<File> tiffFileOpt = makeTiffFromPDF(pdfFile, tiffFile);

            if (! tiffFileOpt.isPresent()) {
                throw new CanNotProcessCoordinateException("Could not convert PDF to Tiff: " + coordinate);
            }
            tiffFile = tiffFileOpt.get();
            String result = getOCRResultFromTiff(tiffFile);
            pdfFile.deleteOnExit(); tiffFile.deleteOnExit();
            pdfFile.delete(); tiffFile.delete();
            return result;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        throw new CanNotProcessCoordinateException("Could not convert PDF to Tiff: " + coordinate);
    }

    private String getOCRResultFromTiff(File tiffFile) throws IOException, TikaException, SAXException {
        TesseractOCRParser tesseractOCRParser = new TesseractOCRParser();
        ToXMLContentHandler handler = new ToXMLContentHandler();
        tesseractOCRParser.parse(new FileInputStream(tiffFile), handler, new Metadata());
        return StringTools.getFirstHtmlWithContent(handler.toString());
    }


    private void validateImageMagick() {
        if (! hasImageMagick()) {
            throw new IllegalArgumentException("ImageMagick could not be found! Please install imagemagick package.");
        }
    }

    private boolean hasImageMagick() {
        if (Boolean.FALSE.equals(imageMagickInstalled)) {
            return false;
        }
        if (Boolean.TRUE.equals(imageMagickInstalled)) {
            return true;
        }

        String[] checkCmd = { getImageMagickProg() };
        try {
            imageMagickInstalled = ExternalParser.check(checkCmd);
        } catch (NoClassDefFoundError e) {
            imageMagickInstalled = false;
        }
        return imageMagickInstalled;
    }


    private String getImageMagickProg() {
        return System.getProperty("os.name").startsWith("Windows") ? "convert.exe" : "convert";
    }

    private Optional<File> makeTiffFromPDF(File input, File output) throws IOException, TikaException {
        String[] cmd = {getImageMagickProg(),"-density", "300", input.getPath(), "-depth", "8", "-quality", "1", output.getPath()};
        Process process = new ProcessBuilder(cmd).start();
        process.getOutputStream().close();
        InputStream out = process.getInputStream();
        logStream(out);
        FutureTask<Integer> waitTask = new FutureTask<>(() -> process.waitFor());
        Thread waitThread = new Thread(waitTask);
        waitThread.start();
        try {
            waitTask.get(240, TimeUnit.SECONDS);
            return Optional.of(output);
        } catch (Exception e) {
            logger.error(e.getMessage());
            waitThread.interrupt();
            process.destroy();
            Thread.currentThread().interrupt();
        }
        return Optional.empty();
    }

    /**
     * Starts a thread that reads the contents of the standard output or error
     * stream of the given process to not block the process. The stream is
     * closed once fully processed.
     */
    private void logStream(final InputStream stream) {
        new Thread() {
            public void run() {
                Reader reader = new InputStreamReader(stream, IOUtils.UTF_8);
                StringBuilder out = new StringBuilder();
                char[] buffer = new char[1024];
                try {
                    for (int n = reader.read(buffer); n != -1; n = reader.read(buffer)) {
                        out.append(buffer, 0, n);
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage());
                } finally {
                    IOUtils.closeQuietly(stream);
                }
                logger.debug(out.toString());
            }
        }.start();
    }

}
