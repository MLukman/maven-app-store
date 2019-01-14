package my.com.unifi.myunifi.appstore.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import org.apache.maven.artifact.repository.metadata.Metadata;
import org.apache.maven.artifact.repository.metadata.io.xpp3.MetadataXpp3Reader;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

public class RemoteMavenPackage {

    protected String repoUrl;
    protected boolean release = true;
    protected String downloadUrl;
    protected String artifactId;
    protected String latest;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss Z")
    protected Date updated;
    protected List<String> versions;
    protected String extension;

    public RemoteMavenPackage(String repositoryUrl, String packaging) throws MalformedURLException, IOException, XmlPullParserException {
        repoUrl = repositoryUrl;
        if (!repoUrl.endsWith("/")) {
            repoUrl += "/";
        }
        URL packageBaseUrl = new URL(repoUrl);
        MetadataXpp3Reader metaReader = new MetadataXpp3Reader();
        Metadata meta = metaReader.read(new URL(packageBaseUrl, "maven-metadata.xml").openStream());
        latest = meta.getVersioning().getRelease();
        versions = meta.getVersioning().getVersions();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            updated = sdf.parse(meta.getVersioning().getLastUpdated());
        } catch (ParseException ex) {
            updated = new Date(0);
        }
        String version = latest;
        if (latest == null) {
            release = false;
            latest = meta.getVersioning().getLatest();
            meta = metaReader.read(new URL(packageBaseUrl, latest + "/maven-metadata.xml").openStream());
            version = meta.getVersioning().getSnapshotVersions().get(0).getVersion();
        }
        MavenXpp3Reader pomReader = new MavenXpp3Reader();
        Model pom = pomReader.read(new URL(packageBaseUrl, String.format("%s/%s-%s.pom", latest, meta.getArtifactId(), version)).openStream());
        artifactId = pom.getArtifactId();
        extension = (packaging != null ? packaging : pom.getPackaging());
        downloadUrl = packageBaseUrl + String.format("%s/%s-%s.%s", latest, artifactId, version, extension);
    }

    public RemoteMavenPackage(String repositoryUrl) throws MalformedURLException, IOException, XmlPullParserException {
        this(repositoryUrl, null);
    }

    public String getRepoUrl() {
        return repoUrl;
    }

    public boolean isRelease() {
        return release;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public String getDownloadUrl(String version) throws MalformedURLException, IOException, XmlPullParserException {
        if (version == null || version.isEmpty()) {
            return getDownloadUrl();
        }

        MetadataXpp3Reader metaReader = new MetadataXpp3Reader();
        MavenXpp3Reader pomReader = new MavenXpp3Reader();
        try {
            if (release) {
                Model pom = pomReader.read(new URL(repoUrl + String.format("%s/%s-%s.pom", version, artifactId, version)).openStream());
                return repoUrl + String.format("%s/%s-%s.%s", version, artifactId, version, extension);
            } else {
                Metadata meta = metaReader.read(new URL(repoUrl + version + "/maven-metadata.xml").openStream());
                String versioning = meta.getVersioning().getSnapshotVersions().get(0).getVersion();
                return repoUrl + String.format("%s/%s-%s.%s", version, artifactId, versioning, extension);
            }
        } catch (FileNotFoundException ex) {
            return getDownloadUrl();
        }
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getExtension() {
        return extension;
    }

    public String getLatest() {
        return latest;
    }

    public Date getUpdated() {
        return updated;
    }

    public List<String> getVersions() {
        return versions;
    }

}
