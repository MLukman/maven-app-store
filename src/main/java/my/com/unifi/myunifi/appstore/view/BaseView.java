package my.com.unifi.myunifi.appstore.view;

import io.dropwizard.views.View;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import org.codehaus.plexus.util.StringUtils;

public abstract class BaseView extends View {

    String pageTitle = "";

    @Context
    UriInfo uriInfo;

    protected BaseView(String templateName) {
        super(templateName);
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public int getPathLevel() {
        return StringUtils.countMatches(uriInfo.getPath(), "/");
    }

}
