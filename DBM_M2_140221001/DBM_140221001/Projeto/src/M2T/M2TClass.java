package M2T;

import M2TBd.M2TBd;
import metamodels.Model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pctm on 08/05/2017.
 */
public interface M2TClass {


    public void convert(Model model, M2TBd m2TBd, String path);



    public static String getProtectedAreas(String filePath) {

        File f = new File(filePath);
        boolean reading = false;
        StringBuilder st = new StringBuilder();
        List<String> protectedAreas = new ArrayList<>();

        try {
            FileReader fs = new FileReader(f);
            BufferedReader r = new BufferedReader(fs);
            String line;
            line = r.readLine();
            while (line != null) {

                if (line.contains("//@Protected End")) {
                    reading = false;
                    protectedAreas.add(st.toString());
                    st = new StringBuilder();
                }

                if (reading) {
                    st.append(line + "\n");
                }

                if (line.contains("//@Protected Start")) {
                    reading = true;
                }

                line = r.readLine();
            }
        } catch (FileNotFoundException e) {

            return null;
        } catch (IOException e) {
            return null;
        }

        String result = "";

        for(String s : protectedAreas){
            result += s;
        }

        return result.isEmpty()? null : result;

    }

}
