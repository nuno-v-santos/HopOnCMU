package Web;

import metamodels.Model;

/**
 * Created by pctm on 01/06/2017.
 */
public interface Web {

    public final String PATH_DEFAULT = "src/proj/";

    public void build(Model model);

    public void build(Model model, String path);

}
