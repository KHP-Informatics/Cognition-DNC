/*
        Cognition-DNC (Dynamic Name Concealer)
        Binary to text document converter and database pseudonymiser.

        Copyright (C) 2015 Ismail E. Kartoglu

        This program is free software: you can redistribute it and/or modify
        it under the terms of the GNU General Public License as published by
        the Free Software Foundation, either version 3 of the License, or
        (at your option) any later version.

        This program is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
        GNU General Public License for more details.

        You should have received a copy of the GNU General Public License
        along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
        File pdfFile = null;
        File tiffFile = null;
        File convertedTiff = null;
        try {
            pdfFile = File.createTempFile(coordinate.getFileName(), ".pdf");
            FileUtils.writeByteArrayToFile(pdfFile, bytes);
            tiffFile = File.createTempFile(coordinate.getFileName(), ".tiff");
            Optional<File> tiffFileOpt = makeTiffFromPDF(pdfFile, tiffFile);
            if (! tiffFileOpt.isPresent()) {
                throw new CanNotProcessCoordinateException("Could not convert PDF to Tiff: " + coordinate);
            }
            convertedTiff = tiffFileOpt.get();
            return getOCRResultFromTiff(convertedTiff);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            try {
                pdfFile.delete();
                tiffFile.delete();
                convertedTiff.delete();
            } catch (Exception ex) {}
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
        } finally {
            IOUtils.closeQuietly(out);
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
