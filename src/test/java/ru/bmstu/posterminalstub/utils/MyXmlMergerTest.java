package ru.bmstu.posterminalstub.utils;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MyXmlMergerTest {

    @Test
    public void combineTest() throws IOException, ParserConfigurationException, SAXException, TransformerException {
        BufferedReader br = new BufferedReader(new FileReader(new File("src/test/java/ru/bmstu/posterminalstub/utils/test1.xml")));
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line.trim());
        }

        BufferedReader br2 = new BufferedReader(new FileReader(new File("src/test/java/ru/bmstu/posterminalstub/utils/test2.xml")));
        String line2;
        StringBuilder sb2 = new StringBuilder();
        while ((line2 = br2.readLine()) != null) {
            sb2.append(line2.trim());
        }

        Assert.assertNotNull(sb);
        Assert.assertNotNull(sb2);

        String result = MyXmlMerger.merge(sb.toString(), sb2.toString());
        System.out.println(result);
        Assert.assertNotNull(result);
    }
}
