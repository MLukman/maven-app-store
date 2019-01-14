package my.com.unifi.myunifi.appstore.resource;

import io.dropwizard.views.View;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import my.com.unifi.myunifi.appstore.Configuration;
import my.com.unifi.myunifi.appstore.model.AppConfiguration;
import my.com.unifi.myunifi.appstore.model.RemoteMavenPackage;
import my.com.unifi.myunifi.appstore.view.AppInfoView;
import my.com.unifi.myunifi.appstore.view.AppListView;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

@Path("/app/")
public class AppResource {

    @Inject
    public Configuration configuration;

    @Context
    ResourceContext resourceContext;

    @GET
    public View index() {
        return resourceContext.getResource(AppListView.class);
    }

    @GET
    @Path("/{app}")
    public Response appInfo(@PathParam("app") String appId) throws IOException, MalformedURLException, XmlPullParserException {
        if (!configuration.apps.containsKey(appId)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        AppConfiguration appConfig = configuration.apps.get(appId);
        RemoteMavenPackage pack = new RemoteMavenPackage(appConfig.repository);

        return Response.ok(resourceContext.initResource(new AppInfoView(appId, appConfig, pack))).build();
    }

    @GET
    @Path("/{app}/dl")
    public Response download(@PathParam("app") String appId) {
        if (!configuration.apps.containsKey(appId)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        AppConfiguration appConfig = configuration.apps.get(appId);
        return downloadFromMaven(appConfig.repository, null);
    }

    @GET
    @Path("/{app}/{version}")
    public Response download(@PathParam("app") String appId, @PathParam("version") String version) {
        if (!configuration.apps.containsKey(appId)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        AppConfiguration appConfig = configuration.apps.get(appId);
        return downloadFromMaven(appConfig.repository, version);
    }

    protected Response downloadFromMaven(String repository, String version) {
        RemoteMavenPackage pack;
        URLConnection connection;
        try {
            pack = new RemoteMavenPackage(repository);
            connection = new URL(pack.getDownloadUrl(version)).openConnection();
        } catch (IOException | XmlPullParserException ex) {
            throw new WebApplicationException("Unable to contact application storage", ex, Response.Status.BAD_GATEWAY);
        }

        StreamingOutput fileStream = new StreamingOutput() {
            @Override
            public void write(java.io.OutputStream output) throws EOFException {
                try (InputStream input = connection.getInputStream()) {
                    byte[] data = new byte[1024000];
                    int len;
                    while ((len = input.read(data)) > 0) {
                        output.write(data, 0, len);
                        if (Thread.interrupted()) {
                            throw new InterruptedException();
                        }
                    }
                } catch (InterruptedException ex) {
                    throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
                } catch (EOFException ex) {
                    // client closed/cancelled connection
                } catch (IOException ex) {
                    throw new WebApplicationException(Response.Status.BAD_GATEWAY);
                }
            }
        };

        return Response.ok(fileStream, MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", String.format("attachment; filename = %s.%s", pack.getArtifactId(), pack.getExtension()))
                .build();
    }
}
