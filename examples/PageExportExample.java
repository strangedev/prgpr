import com.prgpr.Page;
import com.prgpr.PageExport;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by kito on 10/26/16.
 */
public class PageExportExample {
    public static void main(String[] args) {
        Set<Page> pages = new LinkedHashSet<>();
        Set<String> categories = new LinkedHashSet<>();
        categories.add("Amiga-Computer");
        categories.add("Heimcomputer");

        pages.add(new Page(17724, 0, "Amiga 500", categories));
        pages.add(new Page(17725, 0, "Amiga 501"));

        PageExport.exportToXml(pages, "external_files/personal_stash/output.xml");
    }
}
