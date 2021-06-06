package com.jmletona;

import lombok.Data;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.util.ArrayList;


@Data
public class MakePDF {
    public String getName() {
        return name;
    }

    String name;

    public String getPath() {
        return path;
    }

    String path;

    public MakePDF(String Name, String Path) {
        this.name = Name;
        this.path = Path;
    }

    public static void main(String[] args) {

    }

    public void addArrayList(ArrayList<String> alReport){
        try (PDDocument document = new PDDocument()) {
            PDPageContentStream contentStream;
            final int LINE_PER_PAGE = 33;
            int totalLines = 0;//complete document
            while(totalLines<alReport.size()) {//for each page
                PDPage page = new PDPage(PDRectangle.A6);
                document.addPage(page);
                contentStream = new PDPageContentStream(document, page);
                contentStream.setFont(PDType1Font.COURIER, 7);
                contentStream.beginText();
                contentStream.newLineAtOffset(220, page.getMediaBox().getHeight() - 20);
                contentStream.showText("Page: "+((totalLines/LINE_PER_PAGE)+1)+" / "+ ((alReport.size()/LINE_PER_PAGE)+1));
                contentStream.endText();

                int lineSpacing =0;
                int lines = 0;

                for(int  i=totalLines; i<alReport.size(); i++) {
                    contentStream.beginText();
                    contentStream.newLineAtOffset(20, page.getMediaBox().getHeight() - 52 - lineSpacing);
                    contentStream.showText(alReport.get(i));
                    contentStream.endText();
                    lineSpacing+=10;
                    lines++; // lines in each page
                    totalLines++; // lines in complete document
                    if(lines == LINE_PER_PAGE) break;
                }

                contentStream.close();
                document.save(this.getPath()+this.getName());

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
