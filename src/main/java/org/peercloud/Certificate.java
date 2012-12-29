package org.peercloud;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 12/30/12
 * Time: 4:35 AM
 */
public class Certificate {
    public class Sign {
        Pattern pattern = Pattern.compile("^([a-zA-Z0-9]+)\\[([0-9:.]*)-([0-9:.]*)\\]([0-9a-f]+)$");
        String author;
        Date from;
        Date to;
        public Sign(String source) {
            Matcher matcher = pattern.matcher(source);
            if(matcher.find()) {
                author = matcher.group(1);
                String from_string = matcher.group(2);
                String to_string = matcher.group(3);
                String sign = matcher.group(4);
                //if(from_string.isEmpty())
                    //from = new Date()
                // ...
            }
        }
    }
    String source;
    SortedMap<String, String> fields = new TreeMap<>();
    List<Sign> signs;

    public Certificate(String source) {
        StringReader reader = new StringReader(source);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String line = null;
        try {
            line = bufferedReader.readLine();
            while (line != null) {
                String[] tokens = line.split(":\\s*", 2);
                if(tokens.length == 2) {
                    if(tokens[0].equals("sign"))
                        signs.add(new Sign(tokens[1]));
                    else
                        fields.put(tokens[0], tokens[1]);
                }
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.print(getSignedString());
    }

    private String getSignedString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(SortedMap.Entry<String, String> e: fields.entrySet()) {
            stringBuilder.append(e.getKey());
            stringBuilder.append(':');
            stringBuilder.append(e.getValue());
            stringBuilder.append('\n');
        }
        return stringBuilder.toString();
    }

}
