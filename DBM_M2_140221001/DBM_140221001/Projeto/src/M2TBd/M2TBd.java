package M2TBd;

import metamodels.BD_Type;
import metamodels.Model;

/**
 * Created by pctm on 08/05/2017.
 */
public interface M2TBd {

    public void convert(Model model, String path);

    public BD_Type getBdTyp();

    public String getIncludeClassFileName();

    public String getImportFunctionsFileName();

}
