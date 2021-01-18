package org.example.pdf;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;

import java.io.IOException;

public class PdfDemo {

    public static void main(String[] args) throws Exception {

        PdfReader reader = new PdfReader("C:\\test.pdf");
        int pageNum = reader.getNumberOfPages();

        PdfReaderContentParser parser = new PdfReaderContentParser(reader);
        for (int i=1; i <= pageNum; i++) {
            parser.processContent(i, new RenderListener() {
                @Override
                public void beginTextBlock() {
                    System.out.println("begin....................................................................");
                }
                @Override
                public void renderText(TextRenderInfo textRenderInfo) {
                    System.out.println("TextRenderInfo：" + textRenderInfo.getText());
                }
                @Override
                public void endTextBlock() {
                    System.out.println("end....................................................................");
                }
                @Override
                public void renderImage(ImageRenderInfo imageRenderInfo) {
                    try {
                        System.out.println("ImageRenderInfo：" + imageRenderInfo.getImage().getFileType());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }

}
